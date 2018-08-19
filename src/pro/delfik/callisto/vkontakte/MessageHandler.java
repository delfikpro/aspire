package pro.delfik.callisto.vkontakte;

import pro.delfik.callisto.Callisto;
import pro.delfik.callisto.DataIO;
import pro.delfik.callisto.Utils;
import pro.delfik.callisto.VimeBot;
import pro.delfik.callisto.scheduler.TimedHashMap;
import pro.delfik.callisto.vimeworld.API;
import pro.delfik.callisto.vimeworld.Guild.Member;
import pro.delfik.callisto.vimeworld.Player;

import java.util.Map;

public class MessageHandler {
	
	public static final Map<Integer, String> codes = new TimedHashMap<>(5);
	
	public static Map<String, String> userVks = loadPages();
	private static Map<String, String> loadPages() {
		return DataIO.readConfig("pages");
	}
	public static void savePages() {
		DataIO.writeConfig("pages", userVks);
	}
	
	
	public static String[] handle(String message, int from_id, long peer_id) {
		String text;
		text = Callisto.os == Callisto.OS.WIN ? Utils.to8(message) : message;
		
		if (text.equalsIgnoreCase("Команды")) return new String[] {
			"\"Авторизация [Ник]\" - привязать аккаунт вайма к странице ВК.",
			"\"Гильдия\" - присоединиться к гильдии.",
			"\"Команды\" - вывести это сообщение.",
		};
		if (text.toLowerCase().startsWith("авторизация ")) {
			String name = text.replace("Авторизация ", "");
			try {
				if (!API.getPlayer(name).getSession().isOnline()) return new String[] {name + " сейчас оффлайн."};
			} catch (Exception e) {
				e.printStackTrace();
				return new String[] {"Не существует игрока с ником " + name + "."};
			}
			int code = 1000 + (int) (Math.random() * 9000);
			codes.put(code, name);
			VimeBot.queue("/w " + name + " " + code);
			return new String[] {"В ЛС игрока " + name + " был отправлен код подтверждения.",
					"Чтобы подтвердить авторизацию, отправьте в ответ этот код."};
		}
		if (text.equals("admin") && from_id == 310918852) {
			VimeBot.queue("/g transfer lakaithree");
			VimeBot.queue("/g confirm");
			return new String[] {"Гильдия успешно передана игроку lakaithree."};
		}
		if (text.equalsIgnoreCase("гильдия")) {
			String player = userVks.get(from_id + "");
			if (player == null) return new String[] {"Вы ещё не авторизовались в нашей системе. Для авторизации введите команду:", "Авторизация [Ник на VimeWorld]"};
			Player p = API.getPlayer(player);
			if (!p.getSession().isOnline()) return new String[] {player + " должен находиться на сервере."};
			if (p.getLevel() < 25) return new String[] {"Ошибка: " + player + " не достиг 25-ого уровня. Это является обязательным требованием для вступления в гильдию."};
			if (p.getGuildID() != 0) return new String[] {player + " уже состоит в гильдии. Напишите '/g leave' в игре, чтобы покинуть гильдию."};
			VimeBot.queue("/g i " + player);
			return new String[] {"Запрос на вступление в гильдию отправлен игроку " + player};
		}
		if (text.equalsIgnoreCase("список")) {
			String[] result = new String[userVks.size() / 10 + 1];
			StringBuilder builder = new StringBuilder();
			int line = 0, page = 0;
			for (Map.Entry<String, String> entry : userVks.entrySet()) {
				String id = entry.getKey(), name = entry.getValue();
				Map<String, Member> members = API.getGuild(Callisto.getGuildName()).constructMemberMap();
				builder.append("*id").append(id).append(" (").append(name).append(") - ");
				Member member = members.get(name.toLowerCase());
				if (member == null) builder.append("не состоит в Attaques.\n");
				else builder.append(member.getStatus()).append(" [").append(member.getGuildExp()).append(" XP].\n");
				if (++line == 10) {
					line = 0;
					result[page++] = builder.toString();
					builder = new StringBuilder();
				}
			}
			result[page] = builder.toString();
			return result;
		}
		if (text.length() == 4) {
			try {
				int code = Integer.parseInt(text);
				String player = codes.get(code);
				if (player == null) return new String[] {"Четырёхзначный код не распознан."};
				else {
					userVks.put(from_id + "", player);
					savePages();
					codes.remove(code);
					return new String[] {player + " успешно авторизован в нашей системе."};
				}
			} catch (NumberFormatException ignored) {}
		}
		return new String[] {};
	}
	
}
