package controller;

import enums.Direction;

public interface GameController {

    void start();

    void moveSnake(Direction direction);
}
