package pro.delfik.vimebot;

import lombok.Getter;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static java.awt.event.KeyEvent.*;

public class VirtualPlayer extends Thread {
    
    private static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    
    @Getter
    private final Robot robot;
    private final LogInterceptor logInterceptor;
    private final WinAPI winAPI = new WinAPI("VimeWorld.ru", "Discover VimeWorld.ru");
    
    private final Queue<Command> commands = new ConcurrentLinkedQueue<>();
    private volatile Command currentCommand;
    private volatile boolean lock;
    
    public VirtualPlayer(File logFile, Consumer<String> loggerCallback) {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        if (logFile == null)
            logInterceptor = null;
        else
            logInterceptor = new LogInterceptor(logFile, s -> handleLog(s, loggerCallback));
    }
    
    private void handleLog(String s, Consumer<String> loggerCallback) {
        loggerCallback.accept(s);
        if (currentCommand == null)
            return;
        Pattern pattern = currentCommand.getPattern().getResponse();
        if (pattern.matcher(s).matches()) {
            lock = false;
            if (currentCommand.getCallback() != null)
                currentCommand.getCallback().accept(currentCommand);
            currentCommand = null;
        }
    }
    
    @Override
    public synchronized void start() {
        super.start();
        if (logInterceptor != null)
            logInterceptor.start();
    }
    
    @Override
    public void run() {
        while (Util.sleep(850)) {
            if (lock)
                continue;
            Command cmd = commands.poll();
            if (cmd == null)
                continue;
            currentCommand = cmd;
            chat(cmd.getLine());
            lock = true;
        }
    }
    
    public void queueCommand(CommandPattern pattern, Consumer<Command> callback, String... args) {
        commands.offer(new Command(pattern, callback, args));
    }
    
    
    private void chat(String line) {
        
        //		winAPI.test();
        Point location = MouseInfo.getPointerInfo().getLocation();
        
        int m = 2;
        
        winAPI.keyDown(VK_T);
        Util.sleep(50 * m);
        winAPI.keyUp(VK_T);
        
        robot.mouseMove(location.x, location.y);
        Util.sleep(25 * m);
        
        StringSelection stringSelection = new StringSelection(line);
        clipboard.setContents(stringSelection, stringSelection);
        
        Util.sleep(50 * m);
        winAPI.keyDown(VK_CONTROL);
        Util.sleep(25 * m);
        winAPI.keyDown(VK_V);
        Util.sleep(50 * m);
        winAPI.keyUp(VK_V);
        Util.sleep(25 * m);
        winAPI.keyUp(VK_CONTROL);
        
        //		Util.sleep(25 * m);
        //		winAPI.paste();
        
        Util.sleep(50 * m);
        winAPI.keyDown(0x0D);
        Util.sleep(50 * m);
        winAPI.keyUp(0x0D);
        
        //		robot.keyPress(VK_T);
        //		robot.keyRelease(VK_T);
        //		Util.sleep(50);
        
        //		robot.keyPress(KeyEvent.VK_CONTROL);
        //		Util.sleep(50);
        //		robot.keyPress(KeyEvent.VK_V);
        //		robot.keyRelease(KeyEvent.VK_V);
        //		Util.sleep(50);
        //		robot.keyRelease(KeyEvent.VK_CONTROL);
        //		Util.sleep(50);
        //		robot.keyPress(VK_ENTER);
        //		robot.keyRelease(VK_ENTER);
    }
    
    
    public void unlock() {
        lock = false;
    }
    
}
