package pro.delfik.vimebot;

public abstract class AutomaticUnit extends Thread {
    
    public AutomaticUnit(String s) {
        super(s);
    }
    
    @Override
    public abstract void run();
    
}
