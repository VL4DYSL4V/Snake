package context;

import entity.Level;
import enums.LevelID;
import util.converter.StringToLevelConverter;

public final class ApplicationContext {

    private Level level = StringToLevelConverter.of(LevelID.ONE.getName());

    public void restoreLastLevel(){
        this.level = StringToLevelConverter.of(level.getLevelID().getName());
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

}
