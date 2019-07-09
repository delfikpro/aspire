package pro.delfik.vimebot;

import java.awt.Robot;
import java.io.File;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.awt.event.KeyEvent.*;

/**
 * Реализация VirtualPlayer, которая будет отправлять команды в чат Minecraft.
 * Окно Minecraft должно быть открыто и доступно боту.
 */
public class MinecraftVirtualPlayer extends VirtualPlayer {
    
    private final Robot robot = BotUtil.createRobot();
    
    public MinecraftVirtualPlayer() {
    }
    
    public MinecraftVirtualPlayer(File logFile, Function<String, String> logFilter, Consumer<String> loggerCallback) {
        super(logFile, logFilter, loggerCallback);
    }
    
    @Override
    protected void execute(String command) {
        
        BotUtil.copyToClipboard(command);
        
        robot.keyPress(VK_T);
        robot.keyRelease(VK_T);
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