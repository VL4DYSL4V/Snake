package entity;

import com.sun.istack.internal.Nullable;
import enums.MapObject;
import util.uiUtil.ImageFactory;

import java.awt.*;
import java.util.Objects;

public final class FieldObject {

    private final MapObject mapObject;
    private final Coordinates coordinates;
    private final Image image;

    public FieldObject(MapObject name, Coordinates coordinates, Image image) {
        this.mapObject = name;
        this.coordinates = coordinates;
        this.image = image;
    }

    public FieldObject(MapObject name, Coordinates coordinates) {
        this.mapObject = name;
        this.coordinates = coordinates;
        this.image = getImageByName(name);
    }

    private Image getImageByName(MapObject name) {
        switch (name) {
            case PEAR:
                return ImageFactory.getPear();
            case WALL:
                return ImageFactory.getWall();
            case APPLE:
                return ImageFactory.getApple();
            case STONE:
                return ImageFactory.getStone();
            case MUSHROOM:
                return ImageFactory.getMushroom();
            case SCORE_BONUS:
                return ImageFactory.getScoreBonus();
            case SNAKE:
            case EMPTY:
                return null;
        }
        return null;
    }

    public static FieldObject apple(Coordinates coordinates) {
        return new FieldObject(MapObject.APPLE, coordinates, ImageFactory.getApple());
    }

    public static FieldObject pear(Coordinates coordinates) {
        return new FieldObject(MapObject.PEAR, coordinates, ImageFactory.getPear());
    }

    public static FieldObject wall(Coordinates coordinates) {
        return new FieldObject(MapObject.WALL, coordinates, ImageFactory.getWall());
    }

    public static FieldObject stone(Coordinates coordinates) {
        return new FieldObject(MapObject.STONE, coordinates, ImageFactory.getStone());
    }

    public static FieldObject mushroom(Coordinates coordinates) {
        return new FieldObject(MapObject.MUSHROOM, coordinates, ImageFactory.getMushroom());
    }

    public static FieldObject scoreBonus(Coordinates coordinates) {
        return new FieldObject(MapObject.SCORE_BONUS, coordinates, ImageFactory.getScoreBonus());
    }

    public static FieldObject empty(Coordinates coordinates) {
        return new FieldObject(MapObject.EMPTY, coordinates, null);
    }

    public MapObject getMapObject() {
        return mapObject;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Nullable
    public Image getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldObject that = (FieldObject) o;
        return mapObject == that.mapObject &&
                Objects.equals(coordinates, that.coordinates) &&
                Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mapObject, coordinates, image);
    }

    @Override
    public String toString() {
        return "FieldObject{" +
                "name=" + mapObject +
                ", coordinates=" + coordinates +
                ", image=" + image +
                '}';
    }
}
