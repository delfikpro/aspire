package pro.delfik.vimebot;

public abstract class AutomaticUnit extends Thread {
    
    protected final Bot bot;
    
    public AutomaticUnit(Bot bot, String name) {
        super(name);
        this.bot = bot;
        bot.cover(this);
    }
    
    @Override
    public abstract void run();
    
}
