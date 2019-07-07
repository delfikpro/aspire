package pro.delfik.callisto.vimeworld;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TopUnit implements Comparable<TopUnit> {
    private final String name;
    private final long points;
    
    public TopUnit(String name, long points) {
        this.name = name;
        this.points = points;
    }
    
    
    public String getName() {
        return name;
    }
    
    public long getPoints() {
        return points;
    }
    
    public static TopUnit guildMemberExp(Guild.Member member) {
        return new TopUnit(member.getName(), (int) member.getGuildExp());
    }
    
    public static TopUnit guildMemberCoins(Guild.Member member) {
        return new TopUnit(member.getName(), member.getGuildCoins());
    }
    
    public static TopUnit guildMemberJoined(Guild.Member member) {
        return new TopUnit(member.getName(), member.getJoinedAt());
    }
    
    @Override
    public int compareTo(TopUnit o) {
        long l = o.points - points;
        return l > 0 ? 1 : l < 0 ? -1 : 0;
    }
    
    public static TopSet toTopSet(Collection<TopUnit> top) {
        TopSet map = new TopSet();
        for (TopUnit topUnit : top)
            map.put(topUnit.getName(), (int) topUnit.getPoints());
        return map;
    }
    
    public static Map<String, String> toStringStringMap(Iterable<TopUnit> top) {
        Map<String, String> map = new HashMap<>();
        for (TopUnit topUnit : top)
            map.put(topUnit.getName(), String.valueOf(topUnit.getPoints()));
        return map;
    }
    
    
    public static class TopSet extends HashMap<String, Integer> {}
}
