package command;

import context.ApplicationContext;
import enums.LevelID;
import exception.CannotAccessLevelException;

import java.util.Objects;

public final class ChangeLevelCommandImpl implements ChangeLevelCommand{

    private final ApplicationContext applicationContext;

    public ChangeLevelCommandImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void change(String levelID) {
        try {
            synchronized (applicationContext) {
                for(LevelID neuLevelID: LevelID.values()){
                    if(Objects.equals(neuLevelID.getId(), levelID)){
                        applicationContext.setLevel(applicationContext.getLevelDAO().getLevel(neuLevelID));
                        return;
                    }
                }
                throw new IllegalArgumentException("Unknown level id: " + levelID);
            }
        } catch (CannotAccessLevelException e) {
            e.printStackTrace();
        }
    }
}
