package pro.delfik.callisto.vimeworld;

import org.json.JSONException;
import org.json.JSONObject;
import pro.delfik.callisto.Callisto;
import pro.delfik.callisto.Utils;
import pro.delfik.callisto.vimeworld.TopUnit.TopSet;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Guild {
    
    private final int id, level;
    private final long createdAt, totalCoins;
    private final float levelPercentage;
    private final String name, tag, color;
    private final List<Member> members;
    
    
    Guild(JSONObject json) {
        try {
            this.id = json.getInt("id");
            this.name = json.getString("name");
            this.tag = json.getString("tag");
            this.color = json.getString("color");
            this.createdAt = json.getLong("created");
            this.totalCoins = json.getLong("totalCoins");
            this.level = json.getInt("level");
            this.levelPercentage = (float) json.getDouble("levelPercentage");
            this.members = Utils.transform(Utils.toJavaList(json.getJSONArray("members")), Member::new);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    
    public int getLevelPoints() {
        return (int) ((level + levelPercentage) * 100);
    }
    
    public Map<String, Member> constructMemberMap() {
        Map<String, Member> map = new HashMap<>();
        for (Member member : members)
            map.put(member.getName().toLowerCase(), member);
        return map;
    }
    
    public Set<TopUnit> calculateInactives(int defaultXP, int requriedXP) {
        return calculateInactives(Utils.mapFilledWith(members, defaultXP + ""), requriedXP);
    }
    
    public Set<TopUnit> calculateInactives(Map<String, String> old, int requiredXP) {
        TopSet difference = new TopSet();
        Iterable<TopUnit> newIter = generateLevelTop();
        Set<String> left = new HashSet<>();
        Set<TopUnit> toKick = new HashSet<>();
        for (TopUnit unit : newIter) {
            if (!old.containsKey(unit.getName())) {
                left.add(unit.getName());
                continue;
            }
            String toCompare = old.get(unit.getName());
            int periodXP = (int) (unit.getPoints() - Utils.asInt(toCompare));
            if (periodXP < requiredXP)
                toKick.add(new TopUnit(unit.getName(), periodXP));
        }
        if (!left.isEmpty()) {
            Callisto.warn("Найдены новые игроки: ");
            System.out.println(String.join(", ", left));
            Callisto.warn("Они не были рассмотрены, так как они в гильдии меньше трёх дней.");
        }
        return toKick;
    }
    
    public static class Member {
        private final String name;
        private final Status status;
        private final long joinedAt;
        private final int guildCoins;
        private final long guildExp;
        
        Member(JSONObject json) {
            try {
                this.name = json.getJSONObject("user").getString("username");
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
            OFFICER("Staff"),
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
        
        public String getLowerCaseName() {
            return name.toLowerCase();
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
        
        @Override
        public String toString() {
            return name;
        }
    }
    
    public List<TopUnit> generateLevelTop() {
        List<TopUnit> unsortedTop = Utils.transform(members, TopUnit::guildMemberExp);
        Collections.sort(unsortedTop);
        return unsortedTop;
    }
    
    public Iterable<TopUnit> generateCoinTop() {
        List<TopUnit> unsortedTop = Utils.transform(members, TopUnit::guildMemberCoins);
        Collections.sort(unsortedTop);
        return unsortedTop;
    }
    
    public Iterable<TopUnit> generateJoinedTop() {
        List<TopUnit> unsortedTop = Utils.transform(members, TopUnit::guildMemberJoined);
        Collections.sort(unsortedTop);
        return unsortedTop;
    }
    
    public List<Member> getMembers() {
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
    
    public float getLevelPercentage() {
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
