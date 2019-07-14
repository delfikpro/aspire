package pro.delfik.vimebot.minecraft;

import pro.delfik.vimebot.AutomaticUnit;
import pro.delfik.vimebot.Bot;
import pro.delfik.vimebot.BotUtil;

import java.awt.Color;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class MinecraftReconnector {
    
    private final Robot robot;
    private final Bot bot;
    
    public MinecraftReconnector(Bot bot) {
        this.bot = bot;
        this.robot = bot.getRobot();
    }
    
    
    /**
     * Попытаться открыть чат Minecraft (Нажатием на кнопку T)
     * @return Нужно ли переподключение
     */
    public boolean tryEnterChat() {
        
        int[] window = bot.getWinAPI().getWindowRect();
        
        int x = window[0], y = window[3];
        Color a = robot.getPixelColor(x + 30, y - 30);
//        robot.mouseMove(x + 30, y - 30);
        robot.keyPress(KeyEvent.VK_T);
        BotUtil.sleep(20);
        robot.keyRelease(KeyEvent.VK_T);
        BotUtil.sleep(200);
    
        Color b = robot.getPixelColor(x + 30, y - 30);
        System.out.println("a=" + a.getRGB() + ", b=" + b.getRGB());
        return a.getRGB() == b.getRGB();
    }
    
    
    public void reconnect() {
        
        int[] rect = bot.getWinAPI().getWindowRect();
        
        int cx = (rect[2] - rect[0]) / 2 + rect[0];
        int cy = (rect[3] - rect[1]) / 2 + rect[1];
        
        for (Action action : Action.values()) {
            if (action.key == -1) {
                robot.mouseMove(cx + action.x, cy + action.y);
                robot.mousePress(InputEvent.BUTTON1_MASK);
                BotUtil.sleep(50);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            } else {
                robot.keyPress(action.key);
                BotUtil.sleep(50);
                robot.keyRelease(action.key);
            }
            BotUtil.sleep(action.delay);
        }
        
    }
    
    public enum Action {
        OPEN_INGAME_MENU(KeyEvent.VK_ESCAPE, 200),
        DISCONNECT_1(0, 40, 2000),
        DISCONNECT_2(0, 80, 2000),
        DISCONNECT_3(0, 120, 2000),
        DISCONNECT_4(0, 160, 2000),
        MULTIPLAYER(0, 30, 2000),
        SELECT_SERVER(0, 0, 1000),
        CONNECT(KeyEvent.VK_ENTER, 1);
        
        private final int x, y, key, delay;
        
        Action(int x, int y, int delay) {
            this.x = x;
            this.y = y;
            this.key = -1;
            this.delay = delay;
        }
        
        Action(int key, int delay) {
            this.key = key;
            this.x = 0;
            this.y = 0;
            this.delay = delay;
        }
    }
    
    
}
