package pro.delfik.vimebot;

import java.util.function.BooleanSupplier;

public class AutoTasker extends Thread {
    
    private final long delay;
    private final BooleanSupplier condition;
    
    public AutoTasker(long delay, BooleanSupplier condition) {
        this.delay = delay;
        this.condition = condition;
    }
    
    public AutoTasker(Runnable runnable, long delay, BooleanSupplier condition) {
        super(runnable);
        this.delay = delay;
        this.condition = condition;
    }
    
    
    @Override
    public final void run() {
        while (Util.sleep(1000)) {
            if (!condition.getAsBoolean())
                continue;
            super.run();
            Util.sleep(delay);
        }
    }
    
    
}
