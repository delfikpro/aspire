package pro.delfik.vimebot;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.File;
import java.util.function.Consumer;

import static java.awt.event.KeyEvent.*;

public class StealthVirtualPlayer extends VirtualPlayer {
    
    private final WinAPI winAPI = new WinAPI("VimeWorld.ru", "Discover VimeWorld.ru");
    private final Robot robot = Util.createRobot();
    
    public StealthVirtualPlayer(File logFile, Consumer<String> loggerCallback) {
        super(logFile, loggerCallback);
    }
    
    @Override
    protected void execute(String line) {
        
        Point location = MouseInfo.getPointerInfo().getLocation();
        
        int m = 2;
        
        winAPI.keyDown(VK_T);
        Util.sleep(50 * m);
        winAPI.keyUp(VK_T);
        
        robot.mouseMove(location.x, location.y);
        Util.sleep(25 * m);
        
        Util.copyToClipboard(line);
        
        Util.sleep(50 * m);
        winAPI.keyDown(VK_CONTROL);
        Util.sleep(25 * m);
        winAPI.keyDown(VK_V);
        Util.sleep(50 * m);
        winAPI.keyUp(VK_V);
        Util.sleep(25 * m);
        winAPI.keyUp(VK_CONTROL);
        
        Util.sleep(50 * m);
        winAPI.keyDown(0x0D);
        Util.sleep(50 * m);
        winAPI.keyUp(0x0D);
        
    }
    
}