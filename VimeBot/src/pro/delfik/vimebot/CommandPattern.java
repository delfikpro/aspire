package pro.delfik.vimebot;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.regex.Pattern;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class CommandPattern {
    
    public static final CommandPattern CHAT = new CommandPattern("%s", Pattern.compile(".*"));
    public static final CommandPattern IGNORE_RESPONSE = new CommandPattern("%s", null);
    
    private final String body;
    private final Pattern response;
    
    public String getLine(String... args) {
        Object[] a = args; // Костыль для избежания неоднозначности vararg'а.
        return String.format(body, a);
    }
    
    /**
     * Проверка на соответствие потенциального результата команды паттерну результата.
     *
     * @param response Строчка из логов (потенциальный результат)
     * @return true, если паттерн равен null
     */
    public boolean responseMatches(String response) {
        return this.response == null || this.response.matcher(response).matches();
    }
}
