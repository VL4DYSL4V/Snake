package event;

import enums.LevelID;
import enums.event.GameEventType;

import java.util.HashMap;
import java.util.Map;

public final class GameEvent {

    private final GameEventType gameEventType;
    private final Map<String, Object> attributes = new HashMap<>();

    public GameEvent(GameEventType gameEventType) {
        this.gameEventType = gameEventType;
    }

    public static GameEvent changeOfLevelEvent(LevelID nextLevel){
        GameEvent gameEvent = new GameEvent(GameEventType.CHANGE_OF_LEVEL);
        gameEvent.addAttribute("nextLevel", nextLevel);
        return gameEvent;
    }

    public static GameEvent leftPressed(){
        return new GameEvent(GameEventType.LEFT_PRESSED);
    }

    public static GameEvent rightPressed(){
        return new GameEvent(GameEventType.RIGHT_PRESSED);
    }

    public static GameEvent upPressed(){
        return new GameEvent(GameEventType.UP_PRESSED);
    }

    public static GameEvent downPressed(){
        return new GameEvent(GameEventType.DOWN_PRESSED);
    }

    public static GameEvent gameOverEvent(){
        return new GameEvent(GameEventType.GAME_OVER);
    }

    public static GameEvent levelStateChangedEvent(){
        return new GameEvent(GameEventType.LEVEL_STATE_CHANGED);
    }

    public void addAttribute(String key, Object attribute){
        attributes.put(key, attribute);
    }

    public Object getAttribute(String key){
        return attributes.get(key);
    }

    public GameEventType getGameEventType() {
        return gameEventType;
    }
}
