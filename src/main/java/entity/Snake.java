package entity;

import enums.Direction;

import java.util.LinkedList;
import java.util.List;

public final class Snake {

    private List<FieldObject> snakeParts;
    private Direction currentDirection;
    private final List<Direction> previousDirections = new LinkedList<>();

    public Snake(List<Coordinates> coordinates, Direction currentDirection){
        this.currentDirection = currentDirection;
        if(coordinates.size() < 2){
            throw new IllegalArgumentException("coordinates.size() must be > 3");
        }
        this.snakeParts = new LinkedList<>();
        this.snakeParts.add(FieldObject.snakeHead(coordinates.get(0)));
        this.previousDirections.add(currentDirection);
        for(int i = 1; i < coordinates.size(); i++){
            this.snakeParts.add(FieldObject.snakeBody(coordinates.get(i)));
            this.previousDirections.add(currentDirection);
        }

    }

    public List<FieldObject> getSnakeParts() {
        return snakeParts;
    }

    public void updateSnakeCoordinates(List<Coordinates> coordinates) {
        List<FieldObject> neuSnakeParts = new LinkedList<>();
        neuSnakeParts.add(FieldObject.snakeHead(coordinates.get(0)));
        for(int i = 1; i < coordinates.size(); i++){
            neuSnakeParts.add(FieldObject.snakeBody(coordinates.get(i)));
        }
        this.snakeParts = neuSnakeParts;
    }

    public void updateDirections(Direction currentDirection) {
        previousDirections.remove(previousDirections.size() - 1);
        previousDirections.add(0, currentDirection);
        this.currentDirection = currentDirection;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public Direction getPreviousDirection(int index) {
        return previousDirections.get(index);
    }
}
