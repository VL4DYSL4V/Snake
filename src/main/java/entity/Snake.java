package entity;

import enums.Direction;
import util.coordinateUtil.CoordinateUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class Snake implements Serializable {

    @XmlElementWrapper(name = "snake_parts")
    private List<FieldObject> snakeParts;
    private Direction currentDirection;
    private Coordinates previousTailPosition;

    private static final long serialVersionUID = -873652552637819L;

    private Snake(){}

    public Snake(List<Coordinates> coordinates, Direction currentDirection) {
        this.currentDirection = currentDirection;
        if (coordinates.size() < 2) {
            throw new IllegalArgumentException("coordinates.size() must be >= 2");
        }
        this.snakeParts = new LinkedList<>();
        this.snakeParts.add(FieldObject.snakeHead(coordinates.get(0)));
        for (int i = 1; i < coordinates.size(); i++) {
            this.snakeParts.add(FieldObject.snakeBody(coordinates.get(i)));
        }
        this.previousTailPosition = coordinates.get(coordinates.size() - 1);
    }

    List<FieldObject> getSnakeParts() {
        return Collections.unmodifiableList(snakeParts);
    }

    List<FieldObject> move(Coordinates nextHeadCoordinates) {
        if (!CoordinateUtils.is_SWEN_Neighbour(snakeParts.get(0).getCoordinates(), nextHeadCoordinates)) {
            throw new IllegalArgumentException();
        }
        List<FieldObject> out = snakeParts;
        List<FieldObject> neuSnakeParts = new LinkedList<>();
        neuSnakeParts.add(FieldObject.snakeHead(nextHeadCoordinates));
        for (int i = 1; i < snakeParts.size(); i++) {
            neuSnakeParts.add(FieldObject.snakeBody(snakeParts.get(i - 1).getCoordinates()));
        }
        this.previousTailPosition = snakeParts.get(snakeParts.size() - 1).getCoordinates();
        this.snakeParts = neuSnakeParts;
        return out;
    }

    void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    Direction getCurrentDirection() {
        return currentDirection;
    }

    void appendTail() {
        snakeParts.add(FieldObject.snakeBody(previousTailPosition));
    }

    Collection<Coordinates> cutAsMuchTailAsPossible(int maxAmount) {
        Collection<Coordinates> out = new LinkedList<>();
        if (maxAmount < 0) {
            return out;
        }
        int snakeLength = snakeParts.size();
        int allowedAmount = Math.min(snakeLength - 2, maxAmount);
        for (int i = 0; i < allowedAmount; i++) {
            FieldObject bodyPart = snakeParts.get(snakeParts.size() - 1);
            out.add(bodyPart.getCoordinates());
            snakeParts.remove(snakeParts.size() - 1);
        }
        return out;
    }
}
