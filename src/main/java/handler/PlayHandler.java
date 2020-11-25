package handler;

import enums.Direction;

public interface PlayHandler {

    void start();

    void moveSnake(Direction direction);
}
