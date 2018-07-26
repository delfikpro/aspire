package pro.delfik.vimebot;

public class VKBot {
	
	protected static String token = "5cd7565d245774d08cd43bd8522ed2167a25a1d8338a3d309c5ee91ca7adef94c7a3b1668a2f6b8d87d16";
	
	public static void start() {
		new Thread(LongPoll::run).start();
	}
}
