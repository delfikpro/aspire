package pro.delfik.vimebot;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Console {
    
    public static Console console;
    
    private final PrintStream stream = System.out;
    private final boolean useColors;
    private static final String[] encodings = {"UTF8", "CP1251", "CP866"};
    private final String encoding1, encoding2;
    
    public Console(String encoding) {
        this.encoding1 = encodings[encoding.charAt(0) - '1'];
        this.encoding2 = encodings[encoding.charAt(1) - '1'];
        this.useColors = encoding.charAt(2) == '1';
        console = this;
    }
    
    public static void log(String s) {
        console.print(s);
    }
    
    public void print(String string) {
        String s;
        try {
            s = new String(string.getBytes(encoding1), encoding2);
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
