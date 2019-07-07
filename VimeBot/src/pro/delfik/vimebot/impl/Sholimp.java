package pro.delfik.vimebot.impl;

import pro.delfik.vimebot.AutoTasker;
import pro.delfik.vimebot.Command;
import pro.delfik.vimebot.CommandPattern;
import pro.delfik.vimebot.Config;
import pro.delfik.vimebot.Console;
import pro.delfik.vimebot.PriorityVirtualPlayer;
import pro.delfik.vimebot.Util;
import pro.delfik.vimebot.VimeBot;
import pro.delfik.vimebot.VirtualPlayer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class Sholimp {
    
    public static Sholimp instance;
    private static CommandPattern WARP = new CommandPattern("/warp invite %s %s", Pattern.compile("(You have invited .*|[^:]* is already invited.*|[^:]* is the creator.*)"));
    private static CommandPattern LIST = new CommandPattern("/list", Pattern.compile("Сейчас [0-9]+ игрок.+ на сервере."));
    
    private final VimeBot vimeBot;
    private String[] warps;
    private Set<String> invited = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private Set<String> blacklist = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private String[] ads;
    private BufferedWriter invitedWriter, blacklistWriter;
    private volatile boolean advertising;
    
    public Sholimp(File workingDir, String encoding) {
        Console console = new Console(encoding);
        Config config = new Config(new File(workingDir, "config.yml"));
        VirtualPlayer virtualPlayer = new PriorityVirtualPlayer(new File(workingDir, "output-client.log"), this::handleLogLine);
        AutoTasker autoTasker = new AutoTasker(this::advertise, 180_000, () -> advertising);
        this.vimeBot = VimeBot.builder().console(console).config(config).virtualPlayer(virtualPlayer).autoTasker(autoTasker).build();
        
        Util.readFile(workingDir, "invited.txt", invited);
        Util.readFile(workingDir, "blacklist.txt", blacklist);
        ads = Util.readFile(workingDir, "ads.txt", new ArrayList<>()).toArray(new String[0]);
        
        invitedWriter = Util.openWriter(workingDir, "invited.txt");
        blacklistWriter = Util.openWriter(workingDir, "blacklist.txt");
        
    }
    
    private void advertise() {
        if (ads.length != 0) {
            String ad = ads[(int) (ads.length * Math.random())];
            vimeBot.getVirtualPlayer().queueCommand(CommandPattern.CHAT, null, ad);
        }
        vimeBot.getVirtualPlayer().queueCommand(LIST, null);
    }
    
    private void start() {
        vimeBot.start();
        this.warps = vimeBot.getConfig().get("warps").split(", ");
        Console.log("&aУспешная инициализация. Работаем с варпами: &e" + Arrays.toString(warps) + "\n");
    }
    
    private static final String[] GROUPS = {"default: ", "Premium: ", "Helper: ", "PHelper: ", "Pro: ", "VIP: ", "Moderator: ", "ServerModer: "};
    
    private void handleLogLine(String line) {
        if (line.startsWith("Sends a private"))
            advertising = !advertising;
        if (line.startsWith("Request to teleport"))
            vimeBot.getVirtualPlayer().unlock();
        if (line.startsWith("You have uninvited "))
            handleUnInvite(line);
        boolean b = false;
        for (String group : GROUPS) {
            if (!line.startsWith(group))
                continue;
            b = true;
            break;
        }
        if (!b)
            return;
        
        String[] players = line.substring(9).split(", ");
        List<String> list = new ArrayList<>(Arrays.asList(players));
        for (String player : list) {
            player = player.replace("[Отошел]", "");
            if (invited.contains(player))
                continue;
            if (blacklist.contains(player))
                continue;
            Console.log("&7Обнаружен недобавленный игрок &6" + player + "&7.\n");
            
            requestInvite(player);
            
        }
    }
    
    private void handleUnInvite(String line) {
        String player = line.split(" ")[3];
        if (!blacklist.add(player))
            return;
        try {
            blacklistWriter.write(player);
            blacklistWriter.newLine();
            blacklistWriter.flush();
            Console.log("&e" + player + " добавлен в чёрный список.\n&7");
        } catch (IOException e) {
            Console.log("&4Ошибка при записи &6" + player + "&c в чёрный список.\n&c");
            e.printStackTrace();
        }
    }
    
    private void requestInvite(String player) {
        for (String warp : warps)
            vimeBot.getVirtualPlayer().queueCommand(WARP, this::successInvite, player, warp);
    }
    
    private void successInvite(Command command) {
        String player = command.getArgs()[0];
        if (!invited.add(player))
            return;
        try {
            invitedWriter.write(player);
            invitedWriter.newLine();
            invitedWriter.flush();
        } catch (IOException e) {
            Console.log("&4Ошибка при записи &6" + player + "&c в список приглашённых.\n&c");
            e.printStackTrace();
        }
    }
    
    public static void main(String... args) {
        
        //		WinDef.HWND hwnd = Reconnector.User32.INSTANCE.FindWindow(null, "VimeWorld.ru");
        //		Reconnector.User32.INSTANCE.SendMessage(hwnd, new WinDef.UINT(WinUser.WM_KEYDOWN), new WinDef.WPARAM(KeyEvent.VK_T), new WinDef.LPARAM(0x002C0001));
        
        //		WinAPI winAPI = new WinAPI("Безымянный — Блокнот");
        if (args.length < 2) {
            System.out.println("Usage: vimebot [encoding] [workingDir]");
            return;
        }
        instance = new Sholimp(new File(args[1]), args[0]);
        instance.start();
    }
    
}
