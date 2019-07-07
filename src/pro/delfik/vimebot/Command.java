package pro.delfik.vimebot;

import lombok.Getter;

import java.util.function.Consumer;

@Getter
public class Command {

	private final CommandPattern pattern;
	private final String line;
	private final String[] args;
	private final Consumer<Command> callback;

	public Command(CommandPattern pattern, Consumer<Command> callback, String... args) {
		this.pattern = pattern;
		this.args = args;
		this.callback = callback;
		this.line = pattern.getLine(args);
	}

}
