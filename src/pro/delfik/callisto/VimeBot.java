package pro.delfik.callisto;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class VimeBot extends Thread {
	
	public static VimeBot instance;
	
	private Robot handle;
	private final Queue<String> queue = new PriorityBlockingQueue<>();
	
	public VimeBot() {
		super("VimeBot");
		try {
			handle = new Robot();
			System.out.println("[VimeBot] Successfully injected into VimeWorld.");
		} catch (AWTException e) {
			System.out.println("[ERROR] Unable to inject into VimeWorld.");
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
		queue.add(command);
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				String command = queue.remove();
				writeText(command + '\n');
				sleep(150);
				writeText("t");
				sleep(5000);
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
