package pro.delfik.vimebot;

import lombok.experimental.UtilityClass;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BotUtil {
    
    public static boolean sleep(long ms) {
        try {
            Thread.sleep(ms);
            return true;
        } catch (InterruptedException ex) {
            return false;
        }
    }
    
    public static BufferedWriter openWriter(File workingDir, String file) {
        try {
            return new BufferedWriter(new FileWriter(new File(workingDir, file), true));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
    }
    
    public static Collection<String> readFile(File workingDir, String file, Collection<String> collection) {
        try {
            File f = new File(workingDir, file);
            if (!f.exists()) {
                f.createNewFile();
                return new ArrayList<>();
            }
            BufferedReader reader = new BufferedReader(new FileReader(f));
            int i;
            while ((i = reader.read()) != -1)
                collection.add((char) i + reader.readLine());
            reader.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return collection;
    }
    
    private static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    
    public static void copyToClipboard(String string) {
        StringSelection transferable = new StringSelection(string);
        clipboard.setContents(transferable, transferable);
    }
    
    public static Robot createRobot() {
        try {
            return new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
    
    
}
