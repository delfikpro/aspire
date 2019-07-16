package pro.delfik.vimebot;

import java.util.function.BooleanSupplier;

public class AutoTasker extends AutomaticUnit {
    
    private final long delay;
    private final BooleanSupplier condition;
    private final Runnable runnable;
    
    public AutoTasker(Bot bot, Runnable runnable, long delay, BooleanSupplier condition) {
        super(bot, "Auto Tasker");
        this.runnable = runnable;
        this.delay = delay;
        this.condition = condition;
    }
    
    
    @Override
    public final void run() {
        while (BotUtil.sleep(1000)) {
            if (!condition.getAsBoolean())
                continue;
            runnable.run();
            BotUtil.sleep(delay);
        }
    }
    
    
}
