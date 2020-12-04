package entity;

import enums.MapObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@XmlRootElement
public final class FieldObject implements Serializable {

    @XmlElement
    private MapObject mapObject;
    @XmlElement
    private Coordinates coordinates;

    private static final long serialVersionUID = 934875976768632L;

    private FieldObject(){}

    public FieldObject(MapObject name, Coordinates coordinates) {
        this.mapObject = name;
        this.coordinates = coordinates;
    }

    public static FieldObject apple(Coordinates coordinates) {
        return new FieldObject(MapObject.APPLE, coordinates);
    }

    public static FieldObject pear(Coordinates coordinates) {
        return new FieldObject(MapObject.PEAR, coordinates);
    }

    public static FieldObject wall(Coordinates coordinates) {
        return new FieldObject(MapObject.WALL, coordinates);
    }

    public static FieldObject stone(Coordinates coordinates) {
        return new FieldObject(MapObject.STONE, coordinates);
    }

    public static FieldObject mushroom(Coordinates coordinates) {
        return new FieldObject(MapObject.MUSHROOM, coordinates);
    }

    public static FieldObject scoreBonus(Coordinates coordinates) {
        return new FieldObject(MapObject.SCORE_BONUS, coordinates);
    }

    public static FieldObject snakeHead(Coordinates coordinates) {
        return new FieldObject(MapObject.SNAKE_HEAD, coordinates);
    }

    public static FieldObject snakeBody(Coordinates coordinates) {
        return new FieldObject(MapObject.SNAKE_BODY, coordinates);
    }

    public static FieldObject empty(Coordinates coordinates) {
        return new FieldObject(MapObject.EMPTY, coordinates);
    }

    public MapObject getMapObject() {
        return mapObject;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldObject that = (FieldObject) o;
        return mapObject == that.mapObject &&
                Objects.equals(coordinates, that.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mapObject, coordinates);
    }

    @Override
    public String toString() {
        return "FieldObject{" +
                "name=" + mapObject +
                ", coordinates=" + coordinates +
                '}';
    }
}
