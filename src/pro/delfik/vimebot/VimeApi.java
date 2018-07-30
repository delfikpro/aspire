package pro.delfik.vimebot;

import org.json.JSONArray;
import org.json.JSONObject;

class VimeApi {
	static boolean isOffline(String player) {
		JSONObject online = new JSONObject(VK.get("http://api.vime.world/user/" + getUserID(player) + "/session"));
		return online.getJSONObject("online").getBoolean("value");
	}


	public static int getPlayerLevel(String player) {
		JSONObject p = getPlayer(player);
		return p.getInt("level");
	}

	public JSONArray getGuildPlayers() {

	}



}
