package pro.delfik.callisto;

import pro.delfik.callisto.scheduler.Scheduler;
import pro.delfik.callisto.scheduler.Task;
import pro.delfik.callisto.vimeworld.API;
import pro.delfik.callisto.vimeworld.ExperienceChecker;
import pro.delfik.callisto.vimeworld.Guild;
import pro.delfik.callisto.vimeworld.TopUnit;
import pro.delfik.callisto.vkontakte.MessageHandler;
import pro.delfik.callisto.vkontakte.VK;

import java.text.SimpleDateFormat;
import java.util.*;

public class Callisto {
	private static VimeBot bot;
	
	private static String guildName;
	private static String vkToken;
	private static String vkGroupID;
	private static String vimeToken;
	private static String requiredPeriodXP;
	
	public static int getRequiredPeriodXP() {return Utils.asInt(requiredPeriodXP);}
	public static String getGuildName() {return guildName;}
	public static String getVkToken() {return vkToken;}
	public static String getVimeToken() {return vimeToken;}
	public static String getVKGroupID() {return vkGroupID;}
	
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
	public static volatile boolean TEST = false;
	
	public static void main(String[] args) {
		
		loadConfig();
		saveConfig();
		
		new Scheduler().start();
		if (args.length != 0 && args[0].contains("test")) TEST = true;
		new VimeBot().start();
		if (!TEST) VK.start();
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
			if (input.equals("parse")) {
				System.out.println("Введите количество запросов");
				int times = Utils.asInt(scanner.next());
				long start = System.currentTimeMillis();
				for (int time = 0; time < times; time++) {
					Integer[] array = new Integer[50];
					for (int i = 0; i < 50; ++i) array[i] = time * 50 + i;
					API.getPlayers(array).forEach(System.out::println);
				}
				long end = System.currentTimeMillis();
				System.out.println("Запросы успешно отправлены за " + (end - start) + " мс.");
			}
			if (input.equals("qwii")) {
				ExperienceChecker.check();
				ExperienceChecker.save();
			}
			if (input.equals("xp")) {
				System.out.print("Введите нижний порог: ");
				int required = Utils.asInt(scanner.next());
				System.out.print("Введите количество опыта три дня назад у всех: ");
				int threeDaysAgoXP = Utils.asInt(scanner.next());
				for (TopUnit topUnit : API.getGuild(getGuildName()).calculateInactives(threeDaysAgoXP, required)) {
					System.out.println(topUnit.getName() + " - " + topUnit.getPoints());
				}
			}
			if (input.equals("today")) System.out.println("Сегодня " + getDayInYear() + "-й день в году.");
			if (input.equals("newday")) Scheduler.instance.newDay(getDayInYear());
		}
	}
	
	private static void loadConfig() {
		Map<String, String> config = DataIO.readConfig("config.txt");
		guildName = ConfigProperty.guildName.extractFrom(config);
		vkToken = ConfigProperty.vkToken.extractFrom(config);
		vkGroupID = ConfigProperty.vkGroupID.extractFrom(config);
		vimeToken = ConfigProperty.vimeToken.extractFrom(config);
		requiredPeriodXP = ConfigProperty.requiredXPPer3Days.extractFrom(config);
	}
	
	private static void saveConfig() {
		Map<String, String> config = new HashMap<>();
		ConfigProperty.guildName.putTo(config, guildName);
		ConfigProperty.vkToken.putTo(config, vkToken);
		ConfigProperty.vimeToken.putTo(config, vimeToken);
		ConfigProperty.vkGroupID.putTo(config, vkGroupID);
		ConfigProperty.requiredXPPer3Days.putTo(config, requiredPeriodXP);
		DataIO.writeConfig("config.txt", config);
	}
	
	private enum ConfigProperty {
		guildName, vkGroupID, vkToken, vimeToken, requiredXPPer3Days;
		
		String extractFrom(Map<String, String> map) {
			String s = map.get(name());
			return s == null ? "" : s;
		}
		
		void putTo(Map<String, String> map, String value) {
			if (value == null || value.equals("")) {
				map.put(name(), "");
				error("В конфигурации не заполнена строчка '" + name() + "'. Бот будет работать неправильно.");
			} else map.put(name(), value);
		}
	}
	
	private static void loadDayStartValues() {
		try {
			Iterator<Integer> saved = Utils.transform(DataIO.read("dayStart.txt"), Utils::asInt).iterator();
			if (saved.next() != getDayInYear()) throw new Exception();
			MotdManager.setDayStartValues(saved.next(), saved.next());
		} catch (Exception ex) {
			error("[VimeBot] Бот не был запущен сегодня в час ночи, опыт и уровень за день будут отображаться некорректно.");
			MotdManager.updateDayStartValues();
			MotdManager.newDay(true);
		}
	}
	
	public static void saveDayStartValues() {
		DataIO.write("dayStart.txt", Utils.transform(Arrays.asList(getDayInYear(), MotdManager.dayStartMoney, MotdManager.dayStartLevel), String::valueOf));
	}
	
	public static int getDayInYear() {
		return Utils.asInt(new SimpleDateFormat("DDD").format(new Date()));
	}
	
	public static VimeBot getBot() {
		return bot;
	}
	
	public static void warn(Object o) {
		System.out.println("\033[33m" + o + "\033[0m");
	}
	public static void fine(Object o) {
		System.out.println("\033[32m" + o + "\033[0m");
	}
	public static void error(Object o) {
		System.out.println("\033[31m" + o + "\033[0m");
	}
}
