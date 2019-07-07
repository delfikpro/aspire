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

	private final String body;
	private final Pattern response;

	public String getLine(String... args) {
		Object[] a = args; // Костыль для избежания неоднозначности vararg'а.
		return String.format(body, a);
	}

}
