package pro.delfik.vimebot.virtualplayer;

import pro.delfik.vimebot.Bot;
import pro.delfik.vimebot.BotUtil;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.awt.event.KeyEvent.*;

/**
 * Реализация VirtualPlayer, которая будет отправлять команды в чат Minecraft.
 * Окно Minecraft должно быть открыто и доступно боту.
 */
public class MinecraftVirtualPlayer extends VirtualPlayer {
    
    private final MinecraftReconnector reconnector;
    
    public MinecraftVirtualPlayer(Bot bot) {
        super(bot);
        reconnector = new MinecraftReconnector(this.bot);
    }
    
    public MinecraftVirtualPlayer(Bot bot, File logFile, Function<String, String> logFilter, Consumer<String> loggerCallback) {
        super(bot, logFile, logFilter, loggerCallback);
        reconnector = new MinecraftReconnector(this.bot);
    }
    
    @Override
    protected void execute(String command) {
        
        while (reconnector.tryEnterChat()) {
            bot.log("§4Reconnecting...");
            reconnector.reconnect();
            BotUtil.sleep(5000);
        }
        
        BotUtil.copyToClipboard(command);
        
        BotUtil.sleep(50);
        robot.keyPress(VK_CONTROL);
        BotUtil.sleep(50);
        robot.keyPress(VK_V);
        robot.keyRelease(VK_V);
        BotUtil.sleep(50);
        robot.keyRelease(VK_CONTROL);
        BotUtil.sleep(50);
        robot.keyPress(VK_ENTER);
        robot.keyRelease(VK_ENTER);
        
    }
    
    
}