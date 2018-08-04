package pro.delfik.callisto.scheduler;

import java.util.HashMap;
import java.util.Map;

public class TimedHashMap<K, V> extends HashMap<K, V> {
	private final int timeout;
	
	public TimedHashMap(int timeout) {
		super();
		this.timeout = timeout;
	}
	
	public TimedHashMap(Map<? extends K, ? extends V> m, int timeout) {
		super(m);
		this.timeout = timeout;
	}
	
	@Override
	public V put(K key, V value) {
		Scheduler.schedule(new Task(() -> remove(key), timeout));
		return super.put(key, value);
	}
	
	public int getTimeout() {
		return timeout;
	}
}
