package pro.delfik.callisto.scheduler;

import pro.delfik.callisto.MotdManager;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Scheduler extends Thread {
	
	private final Set<Task> waitingTasks = ConcurrentHashMap.newKeySet();
	public static Scheduler instance;
	
	public Scheduler() {
		super("Scheduler");
		instance = this;
	}
	
	public static void schedule(Task task) {
		instance.waitingTasks.add(task);
	}
	
	@Override
	public void run() {
		System.out.println("[Scheduler] Initialized.");
		while (true) {
			tick();
			try {
				sleep(60000);
			} catch (InterruptedException ex) {
				System.err.println("[Scheduler] Terminated.");
				break;
			}
		}
	}
	
	public void tick() {
		checkForNewDay();
		Set<Task> completed = new HashSet<>();
		for (Task task : waitingTasks) if (task.tick()) {
			completed.add(task);
			task.execute();
		}
		completed.forEach(waitingTasks::remove);
	}
	
	private static volatile boolean checkedThisHour = false;
	private void checkForNewDay() {
		if (checkedThisHour) return;
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour == 1) {
			checkedThisHour = true;
			System.out.println("[Sheduler] New day started! Resetting daily xp and coins!");
			MotdManager.newDay(false);
			schedule(new Task(() -> checkedThisHour = false, 60));
		}
	}
	
	public Iterable<Task> getTasks() {
		return waitingTasks;
	}
}
