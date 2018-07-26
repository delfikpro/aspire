package pro.delfik.vimebot;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Scanner;

public class Start {
	private static Robot robot;
	public static Map<String, String> userVks = loadPages();

	private static Map<String, String> loadPages() {
		return DataIO.readConfig("pages");
	}
	static void savePages() {
		DataIO.writeConfig("pages", userVks);
	}

	public static void main(String[] args) {
//		System.out.println(VK.get("http://api.vime.world/online/staff"));
		VKBot.start();
		Scanner scanner = new Scanner(System.in);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		new MinuteTicker().start();
		new TopUpdater().start();
		while (true) {
			String input = scanner.next();
			if (input.equals("stop")) {
				savePages();
				System.exit(0);
			}
		}
	}

	public static void addInGuild(String player) {
		execute("/g i " + player);
	}

	public static void sendPrivateMessage(String player, String message) {
		execute("/w " + player + " " + message);
	}

	public static void execute(String commandline) {
		writeText(commandline + "\n");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		writeText("T");
	}

	public static void writeText(String text) {
		for (char c : text.toUpperCase().toCharArray()) {
			int key;
			boolean shift = false;
			if (c == '&') {
				shift = true;
				key = KeyEvent.VK_7;
			}
			else if (c == '_') {
				shift = true;
				key = KeyEvent.VK_MINUS;
			}
			else if (c == '\n') key = KeyEvent.VK_ENTER;
			else key = KeyEvent.getExtendedKeyCodeForChar(c);
			if (shift) robot.keyPress(KeyEvent.VK_SHIFT);
			robot.keyPress(key);
			robot.keyRelease(key);
			if (shift) robot.keyRelease(KeyEvent.VK_SHIFT);
		}
	}
}
