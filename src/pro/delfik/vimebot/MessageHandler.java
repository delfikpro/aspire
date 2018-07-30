package pro.delfik.vimebot;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class MessageHandler {
	public static String[] handle(String message, int from_id, long peer_id) throws UnsupportedEncodingException {
		String text = to8(message);
		System.out.println(message + " -> " + text);
		if (text.startsWith("Авторизация ")) {
			String name = text.replace("Авторизация ", "");
			try {
				if (VimeApi.isOffline(name)) return new String[] {name + " сейчас оффлайн."};
			} catch (Exception e) {
				e.printStackTrace();
				return new String[] {"Не существует игрока с ником " + name + "."};
			}
			int code = 1000 + (int) (Math.random() * 9000);
			MinuteTicker.add(code, name);
			Start.sendPrivateMessage(name, "" + code);
			return new String[] {"В ЛС игрока " + name + " был отправлен код подтверждения.",
					"Чтобы подтвердить авторизацию, отправьте в ответ этот код."};
		}
		if (text.equals("admin") && from_id == 310918852) {
			Start.execute("/g transfer lakaithree");
			Start.execute("/g confirm");
			return new String[] {"Гильдия успешно передана игроку lakaithree."};
		}
		if (text.equalsIgnoreCase("гильдия")) {
			String player = Start.userVks.get(from_id + "");
			if (player == null) return new String[] {"Вы ещё не авторизовались.", "Для авторизации введите команду\nАвторизация [Ник на VimeWorld]"};
			if (VimeApi.isOffline(player)) return new String[] {player + " должен находиться на сервере."};
			Start.addInGuild(player);
			return new String[] {"Запрос на вступление в гильдию отправлен игроку " + player};
		}
		if (text.equalsIgnoreCase("список")) {
			String[] array = new String[Start.userVks.size()];
			int line = 0;
			for (Map.Entry<String, String> entry : Start.userVks.entrySet()) {
				array[line] = ++line + ". *id" + entry.getKey() + " (" + entry.getValue() + ")";
			}
			return array;
		}
		if (text.length() == 4) {
			try {
				int code = Integer.parseInt(text);
				String player = MinuteTicker.map.get(code);
				if (player == null) return new String[] {"Четырёхзначный код не распознан."};
				else {
					Start.userVks.put(from_id + "", player);
					Start.savePages();
					MinuteTicker.map.remove(code);
					return new String[] {player + " успешно авторизован в нашей системе."};
				}
			} catch (NumberFormatException ignored) {}
		}
		return new String[] {};
	}

	public static String to8(String string) {
		try {
			return new String(string.getBytes("windows-1251"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return string;
		}
	}

	public static String to1251(String string) {
		try {
			return new String(string.getBytes("UTF-8"), "windows-1251");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return string;
		}
	}
}
