package observer;

import enums.event.GameEvent;

public interface GameEventSubscriber {

    void react(GameEvent gameEvent);

}
