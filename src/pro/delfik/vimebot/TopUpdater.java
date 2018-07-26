package pro.delfik.vimebot;

public class TopUpdater extends Thread {
	@Override
	public void run() {
		while (true) {
			try {
				sleep(1800000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
			Start.execute("");
		}
	}
}
