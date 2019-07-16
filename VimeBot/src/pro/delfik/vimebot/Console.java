package pro.delfik.vimebot;

import lombok.RequiredArgsConstructor;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Console {
    
    private final PrintStream stream;
    private final boolean useColors;
    private final String encodingFrom, encodingTo;
    
    @RequiredArgsConstructor
    public enum Preset {
        WINDOWS(false, "CP1251", "CP1251"),
        WINDOWS_CMDCOLOR(true, "CP866", "CP1251");
        
        private final boolean useColors;
        private final String encodingFrom;
        private final String encodingTo;
    
        public static Preset get(String name) {
            try {
                return valueOf(name.toUpperCase());
            } catch (IllegalArgumentException ex) {
                System.out.println("Encoding '" + name + "' wasn't found, defaulting to WINDOWS.");
                return WINDOWS;
            }
        }
    
        public Console create(PrintStream stream) {
            return new Console(stream, useColors, encodingFrom, encodingTo);
        }
        
    }
    
    public Console(PrintStream stream, boolean useColors, String from, String to) {
        this.stream = stream;
        this.useColors = useColors;
        this.encodingFrom = from;
        this.encodingTo = to;
    }
    
    public void print(String string) {
        String s;
        try {
            s = new String(string.getBytes(encodingFrom), encodingTo);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (!useColors) {
            stream.println(s);
            return;
        }
        char[] charArray = s.toCharArray();
        for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
            char c = charArray[i];
            if (c != '&') {
                stream.print(c);
                continue;
            }
            int a = i - 1;
            if (charArrayLength <= a)
                break;
            char r = charArray[++i];
            
            int d = "f42615378cae9db0".indexOf(r);
            boolean bright = d >= 8;
            if (bright)
                d -= 8;
            r = (char) (d + '0');
            char[] chars = d == -1 ?
                new char[]{'\u001b', '[', '0', 'm'} : bright ?
                new char[]{'\u001b', '[', '9', r, 'm'} :
                new char[]{'\u001b', '[', '3', r, 'm'};
            stream.print(chars);
            
        }
    }
    
}
