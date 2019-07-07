package pro.delfik.vimebot;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class LogInterceptor extends Thread implements AutoCloseable {
    
    private final BufferedReader reader;
    private final Consumer<String> callback;
    
    @SneakyThrows
    public LogInterceptor(File file, Consumer<String> callback) {
        this.callback = callback;
        this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Windows-1251"));
        long length = file.length();
        reader.skip(length);
    }
    
    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            int c = reader.read();
            if (c == -1) {
                Thread.sleep(300);
                continue;
            }
            String line = (char) c + reader.readLine();
            String pattern = "[CLIENT] [INFO] [CHAT] ";
            int i = line.indexOf(pattern);
            if (i == -1)
                continue;
            String msg = line.substring(i + pattern.length());
            callback.accept(msg);
        }
    }
    
    @Override
    public void close() throws Exception {
        reader.close();
    }
    
}
