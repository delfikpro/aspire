package pro.delfik.callisto;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import static pro.delfik.callisto.Callisto.TEST;

public class VimeBot extends Thread {
	
	public static VimeBot instance;
	
	private Robot handle;
	private final Queue<String> queue = new PriorityBlockingQueue<>();
	
	public VimeBot() {
		super("VimeBot");
		if (Callisto.TEST)
			Callisto.fine("[VimeBot] Бот успешно запущен в тестовом режиме. Он не будет печатать, будет лишь выводить команды в консоль.");
		else try {
			handle = new Robot();
			Callisto.fine("[VimeBot] Успешная инициализация.");
		} catch (AWTException e) {
			Callisto.error("[Ошибка] Невозможно создать инстанцию робота. Обратитесь к разработчику.");
			e.printStackTrace();
		}
		instance = this;
	}
	
	public Collection<String> getQueuedCommands() {
		return queue;
	}
	
	public static void queue(String command) {
		instance.queueCmd(command);
	}
	
	public void queueCmd(String command) {
		if (TEST) System.out.println("[VimeBotOutput] " + command);
		else queue.add(command);
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				sleep(500);
				String command = queue.remove();
				writeText(command + '\n');
				sleep(1000);
				writeText("t");
				sleep(3500);
			}
			catch (NoSuchElementException ignored) {}
			catch (InterruptedException ex) {break;}
		}
	}
	
	protected void writeText(String text) {
		char[] charArray = text.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			int key;
			boolean shift = Character.isUpperCase(text.codePointAt(i));
			switch (c) {
				case '&':
					shift = true;
					key = KeyEvent.VK_7;
					break;
				case '_':
					shift = true;
					key = KeyEvent.VK_MINUS;
					break;
				case ':':
					shift = true;
					key = KeyEvent.VK_SEMICOLON;
					break;
				case '>':
					shift = true;
					key = KeyEvent.VK_PERIOD;
					break;
				case '%':
					shift = true;
					key = KeyEvent.VK_5;
					break;
				case '+':
					shift = true;
					key = KeyEvent.VK_EQUALS;
					break;
				case '\n':
					key = KeyEvent.VK_ENTER;
					break;
				default:
					key = KeyEvent.getExtendedKeyCodeForChar(c);
					break;
			}
			if (shift) handle.keyPress(KeyEvent.VK_SHIFT);
			handle.keyPress(key);
			handle.keyRelease(key);
			if (shift) handle.keyRelease(KeyEvent.VK_SHIFT);
		}
	}
}
