package pro.delfik.callisto;

import pro.delfik.callisto.scheduler.Scheduler;
import pro.delfik.callisto.scheduler.Task;
import pro.delfik.callisto.vimeworld.API;
import pro.delfik.callisto.vimeworld.Guild;
import pro.delfik.callisto.vimeworld.TopUnit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MotdManager {
	
	public static void update() {
		Guild guild = API.getGuild(Callisto.getGuildName());
		
		Iterable<TopUnit> levelTop = guild.generateLevelTop();
		TopUnit levelBest = levelTop.iterator().next();
		VimeBot.queue("/g motd edit 9 &b&l TOP&9&l XP&7 - &e" + levelBest.getName() + "&f [&9&l" + levelBest.getPoints() + "&f]&7.");
		
		Iterable<TopUnit> coinTop = guild.generateCoinTop();
		TopUnit coinBest = coinTop.iterator().next();
		VimeBot.queue("/g motd edit 10 &b&l TOP&6&l coins&7 - &e" + coinBest.getName() + "&f [&6&l" + coinBest.getPoints() + "&f]&7.");
		
		if (dayStartLevel == -1 || dayStartMoney == -1) newDay(true);
		
		int level = guild.getLevelPoints() - dayStartLevel;
		int money = (int) (guild.getTotalCoins() - dayStartMoney);
		VimeBot.queue("/g motd edit 12 &b&l Daily &9level&b&l increase&7 - &e+&l" + level + "%&7.");
		VimeBot.queue("/g motd edit 13 &b&l Daily &6coins&b&l increase&7 - &e+&l" + money + "&7.");
		
		if (dayStartedAt == null) VimeBot.queue("/g motd edit 11 &f");
		else VimeBot.queue("/g motd edit 11 &c&lInfo is provided at " + dayStartedAt);
		
		Scheduler.schedule(new Task(MotdManager::update, 30));
	}
	
	public static volatile int dayStartLevel = -1;
	public static volatile int dayStartMoney = -1;
	
	private static volatile String dayStartedAt = null;
	
	public static void setDayStartValues(int startMoney, int startLevel) {
		dayStartLevel = startLevel;
		dayStartMoney = startMoney;
	}
	
	public static void updateDayStartValues() {
		Guild guild = API.getGuild(Callisto.getGuildName());
		setDayStartValues((int) guild.getTotalCoins(), guild.getLevelPoints());
	}
	
	public static void newDay(boolean improperly) {
		updateDayStartValues();
		if (improperly) dayStartedAt = new SimpleDateFormat("HH:mm").format(new Date());
		else {
			Callisto.saveDayStartValues();
			dayStartedAt = null;
		}
	}
}
