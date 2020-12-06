package observer;

import enums.event.UIEvent;

public interface UIEventPublisher {

    void subscribe(UIEventSubscriber UIEventSubscriber);

    void unsubscribe(UIEventSubscriber UIEventSubscriber);

    void notifySubscribers(UIEvent uiEvent);

}
