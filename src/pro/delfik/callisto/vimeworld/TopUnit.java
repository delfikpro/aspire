package pro.delfik.callisto.vimeworld;

public class TopUnit implements Comparable<TopUnit> {
	private final String name;
	private final int points;
	
	public TopUnit(String name, int points) {
		this.name = name;
		this.points = points;
	}
	
	
	public String getName() {
		return name;
	}
	
	public int getPoints() {
		return points;
	}
	
	public static TopUnit guildMemberExp(Guild.Member member) {
		return new TopUnit(member.getName(), (int) member.getGuildExp());
	}
	
	public static TopUnit guildMemberCoins(Guild.Member member) {
		return new TopUnit(member.getName(), member.getGuildCoins());
	}
	
	@Override
	public int compareTo(TopUnit o) {
		return o.points - points;
	}
}
