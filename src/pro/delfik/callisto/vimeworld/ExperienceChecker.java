package pro.delfik.callisto.vimeworld;

import pro.delfik.callisto.Callisto;
import pro.delfik.callisto.DataIO;
import pro.delfik.callisto.VimeBot;

import java.util.Map;
import java.util.Set;

public class ExperienceChecker {
	
	public static volatile boolean cancelled = false;
	
	public static void check() {
		Map<String, String> guild = DataIO.readConfig("guildDump.txt");
		if (guild.isEmpty()) {
			Callisto.error("Не удалось найти данные об опыте игроков три дня назад. Скорее всего, бот не был запущен в час ночи три дня назад.");
			return;
		}
		Guild today = API.getGuild(Callisto.getGuildName());
		Set<TopUnit> toKick = today.calculateInactives(guild, Callisto.getRequiredPeriodXP());
		if (toKick.isEmpty()) {
			Callisto.fine("Похоже, что абсолютно все справились и набрали 2000 опыта за три дня!");
			return;
		}
		for (TopUnit unit : toKick) {
			System.out.println(unit.getName() + " набрал всего " + unit.getPoints() + " XP за три дня, кикаем его!");
			VimeBot.queue("/g kick " + unit.getName());
		}
	}
	
	public static void save() {
		Guild g = API.getGuild(Callisto.getGuildName());
		DataIO.writeConfig("guildDump.txt", TopUnit.toStringStringMap(g.generateLevelTop()));
		Callisto.fine("Текущие данные об игроках гильдии сохранены.");
	}
}
