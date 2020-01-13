package pro.delfik.vimebot.command;

import pro.delfik.vimebot.AutomaticUnit;
import pro.delfik.vimebot.Bot;
import pro.delfik.vimebot.BotUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Перехватчик строк, записываемых в файл с логами.
 * Работает в реальном времени.
 */
public class LogInterceptor extends AutomaticUnit implements AutoCloseable {
    
    /**
     * Фильтр для логов Minecraft 1.6.4
     * Принимает только строчки с фразой [CLIENT] [INFO] [CHAT]
     * При этом обрезает эту фразу и всё, что идёт до неё.
     */
    public static final Function<String, String> MINECRAFT_164_FILTER = s -> {
        String pattern = "[CLIENT] [INFO] [CHAT] ";
        int i = s.indexOf(pattern);
        if (i == -1) return null;
        return s.substring(i + pattern.length());
    };
    /**
     * Фильтр для логов Minecraft.
     * Принимает только строчки с фразой [Client thread/INFO]: [CHAT]
     * При этом обрезает эту фразу и всё, что идёт до неё.
     */
    public static final Function<String, String> MINECRAFT_188_FILTER = s -> {
        String pattern = "[Client thread/INFO]: [CHAT] ";
        int i = s.indexOf(pattern);
        if (i == -1) return null;
        return s.substring(i + pattern.length());
    };
    
    private final BufferedReader reader;
    private final Function<String, String> filter;
    private final Consumer<String> callback;
    
    /**
     * Создание перехватчика логов и его подготовка к запуску (но не сам запуск)
     * @param bot Ссылка на бота
     * @param file Файл, в который записываются логи
     * @param filter Фильтр данных, записываемых в файл
     * @param callback Функция, вызываемая при получении новой строки.
     */
    public LogInterceptor(Bot bot, File file, Function<String, String> filter, Consumer<String> callback) {
        super(bot, "Log Interceptor");
        try {
            this.callback = callback;
            this.filter = filter;
            this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "Windows-1251"));
            long length = file.length();
            reader.skip(length);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Override
    public void run() {
        IOException e;
        while (true) {
            try {
                int c = reader.read();
                if (c == -1) {
                    BotUtil.sleep(300);
                    continue;
                }
                String line = (char) c + reader.readLine();
                String msg = filter.apply(line);
                if (msg == null) continue;
                callback.accept(msg);
            } catch (IOException ex) {
                e = ex;
                break;
            }
        }
        throw new RuntimeException(e);
    }
    
    @Override
    public void close() throws Exception {
        reader.close();
    }
    
}
