package eventHandler;

import context.ApplicationContext;
import controller.GameController;
import enums.Direction;
import enums.LevelID;
import enums.event.GameEventType;
import event.GameEvent;
import exception.CannotAccessLevelException;
import observer.gameEvent.GameEventSubscriber;
import ui.WindowHolder;

public class GameEventHandler implements GameEventSubscriber {

    private final WindowHolder windowHolder;
    private final GameController gameController;
    private final ApplicationContext applicationContext;

    public GameEventHandler(WindowHolder windowHolder, GameController gameController, ApplicationContext applicationContext) {
        this.windowHolder = windowHolder;
        this.gameController = gameController;
        this.applicationContext = applicationContext;
    }

    @Override
    public void react(GameEvent gameEvent) {
        GameEventType gameEventType = gameEvent.getGameEventType();
        switch (gameEventType) {
            case UP_PRESSED:
                gameController.moveSnake(Direction.UP);
                break;
            case DOWN_PRESSED:
                gameController.moveSnake(Direction.DOWN);
                break;
            case RIGHT_PRESSED:
                gameController.moveSnake(Direction.RIGHT);
                break;
            case LEFT_PRESSED:
                gameController.moveSnake(Direction.LEFT);
                break;
            case GAME_OVER:
                windowHolder.updateGameFrame();
                windowHolder.showEndOfGameWindow();
                break;
            case LEVEL_STATE_CHANGED:
                windowHolder.updateGameFrame();
                break;
            case CHANGE_OF_LEVEL:
                LevelID levelID = (LevelID) gameEvent.getAttribute("nextLevel");
                try {
                    synchronized (applicationContext) {
                        applicationContext.setLevel(applicationContext.getLevelDAO().getLevel(levelID));
                    }
                } catch (CannotAccessLevelException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
