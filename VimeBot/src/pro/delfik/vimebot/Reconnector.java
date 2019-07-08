package pro.delfik.vimebot;

import com.sun.jna.platform.win32.WinDef;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Reconnector {
    
    public boolean isReconnectRequired(Robot bot) {
        
        int[] window = getWindow();
        
        int x = window[0], y = window[3];
        
        bot.getPixelColor(x + 10, y - 380);
        bot.getPixelColor(x + 10, y - 200);
        bot.getPixelColor(x + 10, y - 10);
        bot.getPixelColor(x + 10, y - 10);
        return false;
    }
    
    
    /**
     * Получение координат прямоугольника окна VimeWorld
     *
     * @return Массив [x1, y1, x2, y2]
     */
    public int[] getWindow() {
        int[] rect = getRect("VimeWorld.ru");
        if (rect == null)
            rect = getRect("Discover VimeWorld.ru");
        if (rect == null)
            throw new RuntimeException("Can't find VimeWorld window");
        return rect;
    }
    
    public void reconnect(Robot bot) {
        
        int[] rect = getWindow();
        
        int cx = (rect[2] - rect[0]) / 2 + rect[0];
        int cy = (rect[3] - rect[1]) / 2 + rect[1];
        
        for (Action action : Action.values()) {
            if (action.key == -1) {
                bot.mouseMove(cx + action.x, cy + action.y);
                bot.mousePress(InputEvent.BUTTON1_MASK);
                Util.sleep(50);
                bot.mouseRelease(InputEvent.BUTTON1_MASK);
            } else {
                bot.keyPress(action.key);
                Util.sleep(50);
                bot.keyRelease(action.key);
            }
            Util.sleep(action.delay);
        }
        
    }
    
    public enum Action {
        OPEN_INGAME_MENU(KeyEvent.VK_ESCAPE, 200),
        DISCONNECT(0, 100, 2000),
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
    
    
    public static int[] getRect(String windowName) {
        WinDef.HWND hwnd = WinAPI.User32.U.FindWindow(null, windowName);
        if (hwnd == null)
            return null;
        
        int[] rect = {0, 0, 0, 0};
        int result = WinAPI.User32.U.GetWindowRect(hwnd, rect);
        if (result == 0)
            throw new RuntimeException("Window rect is unavailable");
        return rect;
    }
    
}
