package pro.delfik.callisto.vkontakte;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class LongPoll {
	protected static String key;
	protected static String server;
	protected static int ts;
	public static volatile long lastPeer;

	public static void requestLongPollServer() {
		System.out.println("[VKBot] Connecting to VK...");
		String data = VK.query("groups.getLongPollServer", "group_id=169237696");
		try {
			JSONObject obj = new JSONObject(data);
			JSONObject response = obj.getJSONObject("response");
			key = response.getString("key");
			server = response.getString("server");
			ts = response.getInt("ts");
			System.out.println("[VKBot] Successfully connected to " + server);
		} catch (Exception ex) {
			System.out.println("[VKBot] Failed connect to VK.");
			ex.printStackTrace();
		}
	}

	public static String getServer() {
		return server;
	}

	public static String getKey() {
		return key;
	}

	public static int getTs() {
		return ts;
	}

	protected static String getConnectUrl(int ts) {
		return (server.startsWith("http") ? "" : "https://") + server + "?act=a_check&key=" + key +
					   "&ts=" + ts + "&wait=25&mode=2";
	}

	private static volatile byte failed = 0;

	public static void run() {
		requestLongPollServer();
		while (true) {
			String lp = getConnectUrl(ts);
			String data = VK.get(lp);
			try {
				JSONObject response = new JSONObject(data);
				int _ts = response.getInt("ts");
				JSONArray updates = response.getJSONArray("updates");
				if(updates.length() != 0) {
					processEvent(updates, _ts);
				}
				ts = _ts;
				failed = 0;
			} catch (JSONException ex) {
				if (failed > 100) throw new RuntimeException("[ERROR] Couldn't connect to VK.");
				else {
					requestLongPollServer();
					failed++;
				}
			}
		}
	}


	private static void processEvent(JSONArray array, long ts) {
		for (int i = 0; i <  array.length(); ++i) {
			try {
				JSONObject arrayItem = array.getJSONObject(i);
				String eventType = arrayItem.getString("type");
				JSONObject object = arrayItem.getJSONObject("object");


				switch (eventType) {
					case "message_new":
						String text;
						int from_id;
						long peer_id = object.getLong("peer_id");
						try {
							from_id = object.getInt("user_id");
						} catch (Exception ex) {
							from_id = object.getInt("from_id");
						}
						try {
							text = object.getString("body");
						} catch (Exception ex) {
							text = object.getString("text");
						}

						lastPeer = peer_id;

						text = text.replaceAll("\\[.*]", "");
						String name = VK.getUserName(peer_id > 2000000000 ? from_id : (int) peer_id);
						for (String msg : MessageHandler.handle(text, from_id, peer_id)) msg(msg, peer_id);
						break;
				}

			} catch (JSONException ignored) {}
		}
	}

	public static void msg(String message, long peer) {
		System.out.println("[Для " + peer + "] " + message);
		VK.query("messages.send", "message=" + URLEncoder.encode(message) + "&" +
				(peer > 2000000000 ? "chat_id=" + (peer - 2000000000) : "user_id=" + peer));
	}

}
