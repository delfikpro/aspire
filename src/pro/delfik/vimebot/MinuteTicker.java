package pro.delfik.vimebot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MinuteTicker extends Thread {

	public static final HashMap<Integer, String> map = new HashMap<>();
	public static List<Unit> units = new ArrayList<>();
	public static volatile List<Integer> unitsToRemove = new ArrayList<>();

	public static class Unit {
		private volatile int minutesLeft;
		private final Integer object;

		public Unit(int minutes, Integer object) {
			this.minutesLeft = minutes;
			this.object = object;
			units.add(this);
		}

		public void decrease() {
			if (--minutesLeft == 0) unitsToRemove.add(object);
		}
	}

	public static void add(Integer key, String value) {
		map.put(key, value);
		new Unit(5, key);
	}

	@Override
	public void run() {
		try {
			while (true) {
				sleep(60000);
				units.forEach(Unit::decrease);
				unitsToRemove.forEach(map::remove);
				unitsToRemove = new ArrayList<>();
			}
		} catch (InterruptedException e) {
			System.out.println("Thread-ticker is dead.");
		}
	}
}
