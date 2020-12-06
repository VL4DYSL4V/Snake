package eventHandler;

import enums.event.GameEvent;
import observer.GameEventSubscriber;
import ui.WindowHolder;

public class GameEventHandler implements GameEventSubscriber {

    private final WindowHolder windowHolder;

    public GameEventHandler(WindowHolder windowHolder) {
        this.windowHolder = windowHolder;
    }

    @Override
    public void react(GameEvent gameEvent) {
        switch (gameEvent){
            case GAME_OVER:
                windowHolder.updateGameFrame();
                windowHolder.showEndOfGameWindow();
                break;
            case LEVEL_STATE_CHANGED:
                windowHolder.updateGameFrame();
                break;
        }
    }
}
