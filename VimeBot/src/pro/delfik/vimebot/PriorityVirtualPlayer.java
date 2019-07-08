package pro.delfik.vimebot;

import java.awt.Robot;
import java.io.File;
import java.util.function.Consumer;

import static java.awt.event.KeyEvent.*;

public class PriorityVirtualPlayer extends VirtualPlayer {
    
    private final Robot robot = Util.createRobot();
    
    public PriorityVirtualPlayer(File logFile, Consumer<String> loggerCallback) {
        super(logFile, loggerCallback);
    }
    
    @Override
    protected void execute(String command) {
        
        Util.copyToClipboard(command);
        
        robot.keyPress(VK_T);
        robot.keyRelease(VK_T);
        Util.sleep(50);
        robot.keyPress(VK_CONTROL);
        Util.sleep(50);
        robot.keyPress(VK_V);
        robot.keyRelease(VK_V);
        Util.sleep(50);
        robot.keyRelease(VK_CONTROL);
        Util.sleep(50);
        robot.keyPress(VK_ENTER);
        robot.keyRelease(VK_ENTER);
        
    }
    
    
}