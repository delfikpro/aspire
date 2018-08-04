package pro.delfik.callisto;

import pro.delfik.callisto.scheduler.Scheduler;
import pro.delfik.callisto.scheduler.Task;
import pro.delfik.callisto.vimeworld.API;
import pro.delfik.callisto.vimeworld.Guild;
import pro.delfik.callisto.vimeworld.TopUnit;
import pro.delfik.callisto.vkontakte.MessageHandler;
import pro.delfik.callisto.vkontakte.VK;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class Callisto {
	private static VimeBot bot;
	
	public static String getGuildName() {
		return "Attaques";
	}
	
	public enum OS {
		MAC, WIN, LINUX, UNKNOWN;
		public static OS get(String name) {
			name = name.toLowerCase();
			if (name.contains("win")) return WIN;
			if (name.contains("linux") || name.contains("unix")) return LINUX;
			if (name.contains("mac")) return MAC;
			System.out.println("Unknown system: " + name);
			return UNKNOWN;
		}
	}
	public static final OS os = OS.get(System.getProperty("os.name"));

	public static void main(String[] args) {
		new Scheduler().start();
		new VimeBot().start();
		VK.start();
		Scanner scanner = new Scanner(System.in);
		loadDayStartValues();
		Scheduler.schedule(new Task(MotdManager::update, 1));
		while (true) {
			String input = scanner.next();
			if (input.equals("stop")) {
				MessageHandler.savePages();
				System.exit(0);
			}
			if (input.equals("top")) {
				System.out.println("\nEnter guild name to create top: ");
				String gname = scanner.next();
				long start = System.currentTimeMillis();
				Guild guild = API.getGuild(gname);
				int position = 1;
				for (TopUnit unit : guild.generateLevelTop()) {
					System.out.println(position++ + ". " + unit.getName() + " - " + unit.getPoints());
				}
				long end = System.currentTimeMillis();
				System.out.println("Top successfuly created in " + (end - start) + " ms.");
			}
			if (input.equals("update")) {
				MotdManager.update();
			}
			if (input.equals("tasks")) {
				int t = 0;
				for (Task task : Scheduler.instance.getTasks())
					System.out.println(++t + ". " + task.getThread() + " task, " + task.getMinutesLeft() + " minutes left.");
			}
			if (input.equals("savedaily")) saveDayStartValues();
		}
	}
	
	private static void loadDayStartValues() {
		try {
			Iterator<Integer> saved = Utils.transform(DataIO.read("dayStart.txt"), Utils::asInt).iterator();
			if (saved.next() != getDayInYear()) throw new Exception();
			MotdManager.setDayStartValues(saved.next(), saved.next());
		} catch (Exception ex) {
			System.out.println("[VimeBot] Cannot load daily values. Daily info in motd will be incorrect.");
			MotdManager.updateDayStartValues();
			MotdManager.newDay(true);
		}
	}
	
	public static void saveDayStartValues() {
		DataIO.write("dayStart.txt", Utils.transform(Arrays.asList(getDayInYear(), MotdManager.dayStartMoney, MotdManager.dayStartLevel), String::valueOf));
	}
	
	private static int getDayInYear() {
		return Utils.asInt(new SimpleDateFormat("DDD").format(new Date()));
	}
	
	public static VimeBot getBot() {
		return bot;
	}
}
