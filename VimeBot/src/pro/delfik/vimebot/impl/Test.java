package pro.delfik.vimebot.impl;

import pro.delfik.vimebot.CommandPattern;
import pro.delfik.vimebot.Console;
import pro.delfik.vimebot.PriorityVirtualPlayer;
import pro.delfik.vimebot.VimeBot;
import pro.delfik.vimebot.VirtualPlayer;

import java.util.Scanner;

public class Test {
    
    private static Test i;
    private final VimeBot vimeBot;
    
    public Test() {
        Console console = new Console("321");
        VirtualPlayer virtualPlayer = new PriorityVirtualPlayer(null, null);
        this.vimeBot = VimeBot.builder().console(console).virtualPlayer(virtualPlayer).build();
        vimeBot.start();
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\n");
        for (; ; ) {
            String next = sc.next();
            if (next.equals("=="))
                System.exit(0);
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
