package pro.delfik.callisto.vimeworld;

import org.json.JSONException;
import org.json.JSONObject;

public class Player {
    private final int id;
    private final String name;
    private final int level;
    private final Rank rank;
    private final int guildID;

    Player(JSONObject user) {
        try {
            this.id = user.getInt("id");
            this.name = user.getString("username");
            this.level = user.getInt("level");
            this.rank = Rank.valueOf(user.getString("rank"));
            int id = 0;
            try {
                id = user.getJSONObject("guild").getInt("id");
            } catch (JSONException ignored) {}
            guildID = id;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    public enum Rank {
        PLAYER("Игрок", "", 0),
        VIP("VIP", "[V]", 0x00be00),
        PREMIUM("Premium", "[P]", 0x00dada),
        HOLY("Holy", "[H]", 0xffba2d),
        IMMORTAL("Immortal", "[I]",	0xe800d5),
        BUILDER("Билдер", "[Билдер]", 0x009c00),
        MAPLEAD("Главный билдер", "[Гл. билдер]", 0x009c00),
        YOUTUBE("YouTube", "[YouTube]", 0xfe3f3f),
        DEV("Разработчик", "[Dev]",	0x00bebe),
        ORGANIZER("Организатор", "[Организатор]", 0x00bebe),
        MODER("Модератор", "[Модер]", 0x1b00ff),
        WARDEN("Проверенный модератор", "[Модер]", 0x1b00ff),
        CHIEF("Главный модератор", "[Гл. модер]", 0x1b00ff),
        ADMIN("Главный админ", "[Гл. админ]", 0x00bebe);

        private final String title, prefix;
        private final int color;

        Rank(String title, String prefix, int color) {
            this.title = title;
            this.color = color;
            this.prefix = prefix;
        }
    }
    
    public Session getSession() {
        try {
            JSONObject online = new JSONObject(API.readRequest("http://api.vime.world/user/" + id + "/session"));
            return new Session(online.getJSONObject("online"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getId() {
        return id;
    }

    public int getGuildID() {
        return guildID;
    }

    public Rank getRank() {
        return rank;
    }
    
    @Override
    public String toString() {
        return rank.prefix + (rank == Rank.PLAYER ? "" : " ") + name + " [id" + id + ", level " + level + ", guild " + guildID + "]";
    }
}
