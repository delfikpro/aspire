package pro.delfik.vimebot;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VimeBot {

	private final VirtualPlayer virtualPlayer;
	private final Config config;
	private final Console console;
	private final AutoTasker autoTasker;

	public void start() {
		if (config != null) config.read();
		if (virtualPlayer != null) virtualPlayer.start();
		if (autoTasker != null) autoTasker.start();
	}

}
