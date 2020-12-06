package eventHandler;

import context.ApplicationContext;
import controller.GameController;
import enums.Direction;
import enums.event.UIEvent;
import observer.UIEventSubscriber;
import ui.WindowHolder;

public final class UIEventHandler implements UIEventSubscriber {

    private final WindowHolder windowHolder;
    private final GameController gameController;
    private final ApplicationContext applicationContext;

    public UIEventHandler(WindowHolder windowHolder, GameController gameController, ApplicationContext applicationContext) {
        this.windowHolder = windowHolder;
        this.gameController = gameController;
        this.applicationContext = applicationContext;
    }

    @Override
    public void react(UIEvent uiEvent) {
        switch (uiEvent){
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
            case RETURNING_TO_MENU:
                gameController.stop();
                applicationContext.restoreLastLevel();
                windowHolder.showMainWindow();
                windowHolder.hideGameWindow();
                windowHolder.hideEndOfGameWindow();
                break;
            case START:
                gameController.start();
                windowHolder.hideMainWindow();
                windowHolder.showGameWindow();
                windowHolder.updateGameFrame();
                break;
            case EXIT:
                handleExit();
                break;
        }
    }

    private void handleExit(){
        System.exit(0);
    }
}
