package controller;

import enums.Direction;

public interface GameController {

    void start();

    void stop();

    void moveSnake(Direction direction);
}
