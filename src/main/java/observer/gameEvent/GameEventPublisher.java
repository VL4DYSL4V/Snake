package observer.gameEvent;

import event.GameEvent;

public interface GameEventPublisher {

    void subscribe(GameEventSubscriber gameEventSubscriber);

    void unsubscribe(GameEventSubscriber gameEventSubscriber);

    void notifySubscribers(GameEvent gameEvent);
}
