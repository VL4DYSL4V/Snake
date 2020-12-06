package observer.gameEvent;

import event.GameEvent;

public interface GameEventSubscriber {

    void react(GameEvent gameEvent);

}
