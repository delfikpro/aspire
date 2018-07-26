package pro.delfik.vimebot;

import org.json.JSONArray;
import org.json.JSONObject;

public class VimeApi {
	public static boolean isOnline(String player) {
		JSONObject online = new JSONObject(VK.get("http://api.vime.world/user/" + getUserID(player) + "/session"));
		return online.getJSONObject("online").getBoolean("value");
	}

	public static JSONObject getPlayer(String player) {
		try {
			String rawJson = VK.get("http://api.vime.world/user/name/" + player);
			JSONArray array = new JSONArray(rawJson);
			return array.getJSONObject(0);
		} catch (Exception e) {
			return null;
		}
	}

	public static int getUserID(String player) {
		JSONObject p = getPlayer(player);
		if (p == null) return 0;
		else return p.getInt("id");
	}

}
