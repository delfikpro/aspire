package pro.delfik.vimebot;

import lombok.Getter;

import java.awt.Robot;
import java.util.HashSet;
import java.util.Set;

public class Bot {
    
    private final Set<AutomaticUnit> units = new HashSet<>();
    
    @Getter
    private final WinAPI winAPI;
    
    @Getter
    private final Robot robot;
    
    @Getter
    private final Console console;
    
    public Bot(Console console, String... windowNameVariants) {
        this.console = console;
        this.winAPI = new WinAPI(windowNameVariants);
        this.robot = BotUtil.createRobot();
    }
    
    public void start() {
        for (AutomaticUnit unit : units) {
            log("&aГотовим к запуску модуль &7" + unit.getName());
            unit.start();
            log("&aМодуль &7" + unit.getName() + "&a успешно запущен.");
        }
    }
    
    public void cover(AutomaticUnit unit) {
        this.units.add(unit);
    }
    
    public void log(String s) {
        console.print(s + "\n");
    }
    
}
