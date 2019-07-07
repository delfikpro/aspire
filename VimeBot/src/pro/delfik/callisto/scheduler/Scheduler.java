package pro.delfik.callisto.scheduler;

import pro.delfik.callisto.Callisto;
import pro.delfik.callisto.MotdManager;
import pro.delfik.callisto.vimeworld.ExperienceChecker;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Scheduler extends Thread {
    
    private final Set<Task> waitingTasks = ConcurrentHashMap.newKeySet();
    public static Scheduler instance;
    
    public Scheduler() {
        super("Планировщик");
        instance = this;
    }
    
    public static void schedule(Task task) {
        instance.waitingTasks.add(task);
    }
    
    @Override
    public void run() {
        Callisto.fine("[Планировщик] Успешная инициализация.");
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
        for (Task task : waitingTasks)
            if (task.tick()) {
                completed.add(task);
                task.execute();
            }
        completed.forEach(waitingTasks::remove);
    }
    
    private static volatile boolean checkedThisHour = false;
    
    private void checkForNewDay() {
        if (checkedThisHour)
            return;
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour != 1)
            return;
        checkedThisHour = true;
        newDay(Callisto.getDayInYear());
    }
    
    public void newDay(int day) {
        Callisto.warn("Начался новый день! Сбрасываем счётчик опыта и коинов за день...");
        MotdManager.newDay(false);
        schedule(new Task(() -> checkedThisHour = false, 61));
        if (day < 360 && day > 4 && day % 3 == 0) {
            Callisto.warn("Сегодня день фильтрации игроков. Начинаем очистку!");
            ExperienceChecker.check();
            ExperienceChecker.save();
        }
    }
    
    public Iterable<Task> getTasks() {
        return waitingTasks;
    }
}
