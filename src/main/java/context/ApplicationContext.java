package context;

import entity.Level;
import enums.LevelName;
import util.converter.StringToLevelConverter;

public final class ApplicationContext {

    private Level level = StringToLevelConverter.of(LevelName.ONE.getName());

    public synchronized Level getLevel() {
        return level;
    }

    public synchronized void setLevel(Level level) {
        this.level = level;
    }

}
