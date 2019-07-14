package pro.delfik.vimebot.impl;

import pro.delfik.vimebot.Bot;
import pro.delfik.vimebot.BotUtil;
import pro.delfik.vimebot.CommandPattern;
import pro.delfik.vimebot.Console;
import pro.delfik.vimebot.VirtualPlayer;
import pro.delfik.vimebot.minecraft.MinecraftVirtualPlayer;

import java.util.Scanner;

public class Test {
    
    private static Test i;
    
    public Test() {
        Console console = Console.Preset.WINDOWS_CMDCOLOR.create(System.out);
        Bot bot = new Bot(console, "VimeWorld.ru", "Discover VimeWorld.ru");
        VirtualPlayer virtualPlayer = new MinecraftVirtualPlayer(bot);
        bot.start();
        
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\n");
        
        while (true) {
            
            String next = sc.next();
            if (next.equals("=="))
                System.exit(0);
            BotUtil.sleep(1000);
            virtualPlayer.unlock();
            virtualPlayer.queueCommand(CommandPattern.CHAT, null, next);
            virtualPlayer.unlock();
            console.print("Queued '" + next + "'\n");
            
        }
    }
    
    public static void main(String[] args) {
        i = new Test();
    }
    
}
