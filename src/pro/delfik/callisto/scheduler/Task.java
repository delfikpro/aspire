package pro.delfik.callisto.scheduler;

public class Task {
	private final Runnable action;
	private final String thread;
	private int minutesLeft;
	
	public Task(Runnable action, int minutesLeft) {
		this.action = action;
		this.minutesLeft = minutesLeft;
		this.thread = Thread.currentThread().getName();
	}
	
	boolean tick() {
		return --minutesLeft == 0;
	}
	
	public void execute() {
		action.run();
	}
	
	public String getThread() {
		return thread;
	}
	
	public int getMinutesLeft() {
		return minutesLeft;
	}
}
