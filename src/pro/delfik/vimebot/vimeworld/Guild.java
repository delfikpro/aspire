package pro.delfik.vimebot.vimeworld;

import org.json.JSONException;
import org.json.JSONObject;
import pro.delfik.vimebot.Utils;

import java.util.Collection;

public class Guild {

    private final int id, level;
    private final long createdAt, totalCoins, levelPercentage;
    private final String name, tag, color;
    private final Collection<Member> members;


    public Guild(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.name = json.getString("name");
            this.tag = json.getString("tag");
            this.color = json.getString("color");
            this.createdAt = json.getLong("created");
            this.totalCoins = json.getLong("totalCoins");
            this.level = json.getInt("level");
            this.levelPercentage = (int) (json.getDouble("levelPercentage") * 100);
            this.members = Utils.transform(Utils.toJavaList(json.getJSONArray("members")), Member::new);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Member {
        private final String name;
        private final Status status;
        private final long joinedAt;
        private final int guildCoins;
        private final long guildExp;

        Member(JSONObject json) {
            try {
                this.name = json.getJSONObject("user").getString("name");
                this.status = Status.valueOf(json.getString("status"));
                this.joinedAt = json.getLong("joined");
                this.guildCoins = json.getInt("guildCoins");
                this.guildExp = json.getLong("guildExp");
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }

        public enum Status {
            LEADER("Лидер"),
            OFFICER("Офицер"),
            MEMBER("Участник");
            private final String description;

            Status(String description) {
                this.description = description;
            }

            @Override
            public String toString() {
                return description;
            }
        }

        public String getName() {
            return name;
        }

        public int getGuildCoins() {
            return guildCoins;
        }

        public long getGuildExp() {
            return guildExp;
        }

        public long getJoinedAt() {
            return joinedAt;
        }

        public Status getStatus() {
            return status;
        }
    }

    public Collection<Member> getMembers() {
        return members;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getLevelPercentage() {
        return levelPercentage;
    }

    public long getTotalCoins() {
        return totalCoins;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }
}
