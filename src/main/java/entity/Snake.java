package entity;

import enums.Direction;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class Snake {

    private List<FieldObject> snakeParts;
    private Direction currentDirection;
    private Coordinates previousTailPosition;
    private final List<Direction> previousDirections = new LinkedList<>();

    public Snake(List<Coordinates> coordinates, Direction currentDirection) {
        this.currentDirection = currentDirection;
        if (coordinates.size() < 2) {
            throw new IllegalArgumentException("coordinates.size() must be > 3");
        }
        this.snakeParts = new LinkedList<>();
        this.snakeParts.add(FieldObject.snakeHead(coordinates.get(0)));
        this.previousDirections.add(currentDirection);
        for (int i = 1; i < coordinates.size(); i++) {
            this.snakeParts.add(FieldObject.snakeBody(coordinates.get(i)));
            this.previousDirections.add(currentDirection);
        }
        this.previousTailPosition = coordinates.get(coordinates.size() - 1);
    }

    List<FieldObject> getSnakeParts() {
        return snakeParts;
    }

    void updateSnakeCoordinates(List<Coordinates> coordinates) {
        this.previousTailPosition = snakeParts.get(snakeParts.size() - 1).getCoordinates();
        List<FieldObject> neuSnakeParts = new LinkedList<>();
        neuSnakeParts.add(FieldObject.snakeHead(coordinates.get(0)));
        for (int i = 1; i < coordinates.size(); i++) {
            neuSnakeParts.add(FieldObject.snakeBody(coordinates.get(i)));
        }
        this.snakeParts = neuSnakeParts;
    }

    void updateDirections(Direction currentDirection) {
        previousDirections.remove(previousDirections.size() - 1);
        previousDirections.add(0, currentDirection);
        this.currentDirection = currentDirection;
    }

    Direction getCurrentDirection() {
        return currentDirection;
    }

    Direction getPreviousDirection(){return previousDirections.get(1);}

    Direction getPreviousDirection(int index) {
        return previousDirections.get(index);
    }

    void appendTail(){
        snakeParts.add(FieldObject.snakeBody(previousTailPosition));
        previousDirections.add(previousDirections.get(previousDirections.size() - 1));
    }

    Collection<Coordinates> cutOffTail(int amount){
        Collection<Coordinates> out = new LinkedList<>();
        for(int i = 0; i < amount; i++){
            FieldObject bodyPart = snakeParts.get(snakeParts.size() - 1);
            out.add(bodyPart.getCoordinates());
            snakeParts.remove(snakeParts.size() - 1);
            previousDirections.remove(previousDirections.size() - 1);
        }
        return out;
    }
}
