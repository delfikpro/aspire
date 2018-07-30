package pro.delfik.vimebot.vimeworld;

import org.json.JSONObject;
import pro.delfik.vimebot.VK;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public final class API {
    private API() {}

    protected static String readRequest(String address) {
        try {
            String server = new String(address.getBytes("UTF-8"), "windows-1251");
            URL url = new URL(server);
            URLConnection connection = url.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = reader.readLine();
            reader.close();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "{}";
        }
    }

    public static Guild getGuild(String guildname) {
        try {
            String rawJson = VK.get("http://api.vime.world/guild/get?name=" + guildname);
            return new Guild(new JSONObject(rawJson));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Player getPlayer(String player) {
        try {
            String rawJson = VK.get("http://api.vime.world/user/name/" + player);
            JSONArray array = new JSONArray(rawJson);
            return array.getJSONObject(0);
        } catch (Exception e) {
            return null;
        }
    }

}
