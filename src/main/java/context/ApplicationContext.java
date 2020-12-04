package context;

import dao.LevelDAO;
import dao.XmlLevelDAO;
import entity.Level;
import enums.LevelID;
import util.converter.StringToLevelConverter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class ApplicationContext {

    private final LevelDAO levelDAO = new XmlLevelDAO();
    private Level level = StringToLevelConverter.of(LevelID.ONE.getId());

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);

    public void restoreLastLevel(){
        this.level = StringToLevelConverter.of(level.getLevelID().getId());
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public LevelDAO getLevelDAO() {
        return levelDAO;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }
}
