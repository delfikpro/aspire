package pro.delfik.vimebot;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.regex.Pattern;

/**
 * Внутриигровая команда.
 * body - строка, в которую будут вставляться аргументы через String.format()
 * response - регекс, по которому будет определятся, пришёл ли ответ от сервера после отправки команды.
 * Пример:
 * body: "/warp invite %s %s"
 * response: "(You have invited .*|[^:]* is already invited.*|[^:]* is the creator.*)"
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class CommandPattern {
    
    /**
     * Паттерн сообщения в чат.
     * Принимает как ответ любое сообщение сервера.
     */
    public static final CommandPattern CHAT = new CommandPattern("%s", Pattern.compile(".*"));
    
    /**
     * Паттерн любого сообщения.
     * Не дожидается ответа сервера.
     * Полезен, если вам всё равно, кикнет вас за спам или нет.
     */
    public static final CommandPattern IGNORE_RESPONSE = new CommandPattern("%s", null);
    
    private final String body;
    private final Pattern response;
    
    /**
     * Возвращает строку команды для представленных аргументов.
     * @param args Аргументы команды
     * @return Команда Minecraft, готовая к отправке в чат.
     */
    public String getLine(String... args) {
        //noinspection UnnecessaryLocalVariable
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
