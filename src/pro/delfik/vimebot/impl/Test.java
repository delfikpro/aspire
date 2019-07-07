package pro.delfik.vimebot.impl;

import pro.delfik.vimebot.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Test {

	private static Test i;
	private final VimeBot vimeBot;

	public Test() {
		Console console = new Console("321");
		VirtualPlayer virtualPlayer = new VirtualPlayer(null, null);
		this.vimeBot = VimeBot.builder().console(console).virtualPlayer(virtualPlayer).build();
		vimeBot.start();
		Scanner sc = new Scanner(System.in);
		sc.useDelimiter("\n");
		for (;;) {
			String next = sc.next();
			if (next.equals("==")) System.exit(0);
			virtualPlayer.unlock();
			virtualPlayer.queueCommand(CommandPattern.CHAT, null, next);
			virtualPlayer.unlock();
			System.out.println("Queued '" + next + "'");
		}
	}

	public static void main(String[] args) {
		i = new Test();
	}

}
