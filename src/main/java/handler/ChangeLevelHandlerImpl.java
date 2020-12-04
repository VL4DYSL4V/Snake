package handler;

import context.ApplicationContext;
import enums.LevelID;
import exception.CannotAccessLevelException;

import java.util.Objects;

public class ChangeLevelHandlerImpl implements ChangeLevelHandler {

    private final ApplicationContext applicationContext;

    public ChangeLevelHandlerImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void change(String choice) {
        try {
            boolean handled = false;
            synchronized (applicationContext) {
                for(LevelID neuLevelID: LevelID.values()){
                    if(Objects.equals(neuLevelID.getId(), choice)){
                        applicationContext.setLevel(applicationContext.getLevelDAO().getLevel(neuLevelID));
                        handled = true;
                        break;
                    }
                }
                if(!handled){
                    throw new IllegalArgumentException();
                }
            }
        } catch (CannotAccessLevelException e) {
            e.printStackTrace();
        }
    }
}
