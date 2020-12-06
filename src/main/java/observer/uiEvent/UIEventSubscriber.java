package observer.uiEvent;

import enums.event.UIEvent;

public interface UIEventSubscriber {

    void react(UIEvent uiEvent);

}
