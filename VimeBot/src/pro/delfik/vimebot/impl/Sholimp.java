package pro.delfik.vimebot.impl;

import pro.delfik.vimebot.*;
import pro.delfik.vimebot.minecraft.MinecraftVirtualPlayer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class Sholimp {
    
    private CommandPattern WARP = new CommandPattern("/warp invite %s %s", Pattern.compile("(You have invited .*|[^:]* is already invited.*|[^:]* is the creator.*)"));
    private CommandPattern LIST = new CommandPattern("/list", Pattern.compile("Сейчас [0-9]+ игрок.+ на сервере."));
    
    private final Bot bot;
    private final VirtualPlayer virtualPlayer;
    private final Console console;
    private String[] warps;
    private Set<String> invited = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private Set<String> blacklist = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private String[] ads;
    private BufferedWriter invitedWriter, blacklistWriter;
    private final AtomicBoolean advertising = new AtomicBoolean(false);
    
    public Sholimp(File workingDir, String encoding) {
        console = Console.Preset.get(encoding).create(System.out);
        Config config = new Config(new File(workingDir, "config.yml"));
        config.read(console);
        this.warps = config.get("warps").split(", ");
        
        this.bot = new Bot(console, "VimeWorld.ru", "Discover VimeWorld.ru", "Explore VimeWorld.ru");
        
        this.virtualPlayer = new MinecraftVirtualPlayer(bot, new File(workingDir, "output-client.log"), LogInterceptor.MINECRAFT_FILTER, this::handleLogLine);
        AutoTasker autoTasker = new AutoTasker(bot, this::advertise, 180_000, advertising::get);
        
        BotUtil.readFile(workingDir, "invited.txt", invited);
        BotUtil.readFile(workingDir, "blacklist.txt", blacklist);
        ads = BotUtil.readFile(workingDir, "ads.txt", new ArrayList<>()).toArray(new String[0]);
        
        invitedWriter = BotUtil.openWriter(workingDir, "invited.txt");
        blacklistWriter = BotUtil.openWriter(workingDir, "blacklist.txt");
        
    }
    
    private void advertise() {
        if (ads.length != 0) {
            String ad = ads[(int) (ads.length * Math.random())];
            virtualPlayer.queueCommand(CommandPattern.CHAT, null, ad);
        }
        virtualPlayer.queueCommand(LIST, null);
    }
    
    private void start() {
        bot.start();
        console.print("&aУспешная инициализация. Работаем с варпами: &e" + Arrays.toString(warps) + "\n");
    }
    
    private static final String[] GROUPS = {"default: ", "Premium: ", "Helper: ", "PHelper: ", "Pro: ", "VIP: ", "Moderator: ", "ServerModer: "};
    
    private void handleLogLine(String line) {
        if (line.startsWith("Sends a private"))
            advertising.set(!advertising.get());
        if (line.startsWith("Request to teleport"))
            virtualPlayer.unlock();
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
            console.print("&7Обнаружен недобавленный игрок &6" + player + "&7.\n");
            
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
            console.print("&e" + player + " добавлен в чёрный список.\n&7");
        } catch (IOException e) {
            console.print("&4Ошибка при записи &6" + player + "&c в чёрный список.\n&c");
            e.printStackTrace();
        }
    }
    
    private void requestInvite(String player) {
        for (String warp : warps)
            virtualPlayer.queueCommand(WARP, this::successInvite, player, warp);
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
            console.print("&4Ошибка при записи &6" + player + "&c в список приглашённых.\n&c");
            e.printStackTrace();
        }
    }
    
    public static void main(String... args) {
        if (args.length < 2) System.out.println("Usage: vimebot [encodingPreset] [workingDir]");
        else new Sholimp(new File(args[1]), args[0]).start();
    }
    
}
