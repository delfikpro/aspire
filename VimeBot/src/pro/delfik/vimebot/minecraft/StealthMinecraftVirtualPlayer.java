package pro.delfik.vimebot.minecraft;

import pro.delfik.vimebot.Bot;
import pro.delfik.vimebot.BotUtil;
import pro.delfik.vimebot.VirtualPlayer;

import java.awt.MouseInfo;
import java.awt.Point;
import java.io.File;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.awt.event.KeyEvent.*;

/**
 * Реализация VirtualPlayer, которая будет отправлять команды в чат Minecraft.
 * Поддерживается с версии Minecraft 1.8 и выше.
 * Отправляет команды даже при свёрнутом окне игры.
 */
public class StealthMinecraftVirtualPlayer extends VirtualPlayer {
    
    public StealthMinecraftVirtualPlayer(Bot bot) {
        super(bot);
    }
    
    public StealthMinecraftVirtualPlayer(Bot bot, File logFile, Function<String, String> logFilter, Consumer<String> loggerCallback) {
        super(bot, logFile, logFilter, loggerCallback);
    }
    
    @Override
    protected void execute(String line) {
        
        WinAPI winAPI = bot.getWinAPI();
        Point location = MouseInfo.getPointerInfo().getLocation();
        
        int m = 2;
        
        winAPI.keyDown(VK_T);
        BotUtil.sleep(50 * m);
        winAPI.keyUp(VK_T);
        
        robot.mouseMove(location.x, location.y);
        BotUtil.sleep(25 * m);
        
        BotUtil.copyToClipboard(line);
        
        BotUtil.sleep(50 * m);
        winAPI.keyDown(VK_CONTROL);
        BotUtil.sleep(25 * m);
        winAPI.keyDown(VK_V);
        BotUtil.sleep(50 * m);
        winAPI.keyUp(VK_V);
        BotUtil.sleep(25 * m);
        winAPI.keyUp(VK_CONTROL);
        
        BotUtil.sleep(50 * m);
        winAPI.keyDown(0x0D);
        BotUtil.sleep(50 * m);
        winAPI.keyUp(0x0D);
        
    }
    
}