package pro.delfik.callisto.vimeworld;

import org.json.JSONException;
import org.json.JSONObject;

public class Session {
    private final boolean online;
    private final String message;
    private final String game;
    
    public Session(JSONObject jsonObject) {
        try {
            this.online = jsonObject.getBoolean("value");
            this.message = jsonObject.getString("message");
            this.game = online ? jsonObject.getString("game") : null;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public String getGame() {
        return game;
    }
    
    public String getMessage() {
        return message;
    }
    
    public boolean isOnline() {
        return online;
    }
}
