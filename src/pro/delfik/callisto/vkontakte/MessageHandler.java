package pro.delfik.callisto.vkontakte;

import pro.delfik.callisto.Callisto;
import pro.delfik.callisto.DataIO;
import pro.delfik.callisto.Utils;
import pro.delfik.callisto.VimeBot;
import pro.delfik.callisto.scheduler.TimedHashMap;
import pro.delfik.callisto.vimeworld.API;
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
		if (Callisto.os == Callisto.OS.WIN) {
			text = Callisto.os == Callisto.OS.WIN ? Utils.to8(message) : message;
			System.out.println(message + " -> " + text);
		} else text = message;
		if (text.startsWith("Авторизация ")) {
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
			if (player == null) return new String[] {"Вы ещё не авторизовались в нашей системе. Для авторизации введите команду:", "Авторизация %5BНик на VimeWorld%5D"};
			Player p = API.getPlayer(player);
			if (!p.getSession().isOnline()) return new String[] {player + " должен находиться на сервере."};
			if (p.getGuildID() != 0) return new String[] {player + " уже состоит в гильдии. Напишите '/g leave' в игре, чтобы покинуть гильдию."};
			VimeBot.queue("/g i " + player);
			return new String[] {"Запрос на вступление в гильдию отправлен игроку " + player};
		}
		if (text.equalsIgnoreCase("список")) {
			String[] array = new String[userVks.size()];
			int line = 0;
			for (Map.Entry<String, String> entry : userVks.entrySet()) {
				array[line] = ++line + ". *id" + entry.getKey() + " (" + entry.getValue() + ")";
			}
			return array;
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
