package pro.delfik.vimebot;

import lombok.experimental.UtilityClass;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.*;

@UtilityClass
public class Util {

	public static boolean sleep(long ms) {
		try {
			Thread.sleep(ms);
			return true;
		} catch (InterruptedException ex) {
			return false;
		}
	}

	public static BufferedWriter openWriter(File workingDir, String file) {
		try {
			return new BufferedWriter(new FileWriter(new File(workingDir, file), true));
		} catch (IOException ex) {
			Console.log("&cНе удалось открыть поток записи &e" + file + "\n&e");
			throw new RuntimeException(ex);
		}

	}

	public static Collection<String> readFile(File workingDir, String file, Collection<String> collection) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(workingDir, file)));
			int i;
			while ((i = reader.read()) != -1) collection.add((char) i + reader.readLine());
			reader.close();
		} catch (IOException ex) {
			Console.log("&fФайлик с приглашёнными юзерами не найден. Вы запускаете меня впервые?\n&r");
		}
		return collection;
	}

	private static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

	public static void copyToClipboard(String string) {
		StringSelection transferable = new StringSelection(string);
		clipboard.setContents(transferable, transferable);
	}

	public static Robot createRobot() {
		try {
			return new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}

}
