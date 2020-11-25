package util;

import entity.Coordinates;
import entity.FieldDimension;
import enums.Direction;

import java.util.Collection;
import java.util.Random;

public final class CoordinateUtils {

    private CoordinateUtils() {
    }

    public static Coordinates simpleCoordinatesCalculation(int maxDesiredX, int maxDesiredY) {
        Random random = new Random();
        return new Coordinates(random.nextInt(maxDesiredX + 1),
                random.nextInt(maxDesiredY + 1));
    }

    public static Coordinates advancedCoordinatesCalculation(Collection<Coordinates> redundantCoords, FieldDimension fieldDimension) {
        Coordinates coordinates;
        int chance = new Random().nextInt(4);
        switch (chance) {
            case 1:
                coordinates = generateLeftBottomCorner(fieldDimension);
                while (redundantCoords.contains(coordinates)) {
                    coordinates = walkRightAndUp(coordinates, fieldDimension.getMinX(), fieldDimension);
                    if (coordinates.getX() == fieldDimension.getMaxX() && coordinates.getY() == fieldDimension.getMinY()) {
                        return coordinates;
                    }
                }
                break;
            case 2:
                coordinates = generateRightUpperCorner(fieldDimension);
                while (redundantCoords.contains(coordinates)) {
                    coordinates = walkLeftAndDown(coordinates, fieldDimension.getMaxX(), fieldDimension);
                    if (coordinates.getX() == fieldDimension.getMinX() && coordinates.getY() == fieldDimension.getMaxY()) {
                        return coordinates;
                    }
                }
                break;
            case 3:
                coordinates = generateRightBottomCorner(fieldDimension);
                while (redundantCoords.contains(coordinates)) {
                    coordinates = walkLeftAndUp(coordinates, fieldDimension.getMaxX(), fieldDimension);
                    if (coordinates.getX() == fieldDimension.getMinX() && coordinates.getY() == fieldDimension.getMinY()) {
                        return coordinates;
                    }
                }
                break;
            default:
                coordinates = generateLeftUpperCorner(fieldDimension);
                while (redundantCoords.contains(coordinates)) {
                    coordinates = walkRightAndDown(coordinates, fieldDimension.getMinX(), fieldDimension);
                    if (coordinates.getX() == fieldDimension.getMaxX() && coordinates.getY() == fieldDimension.getMaxY()) {
                        return coordinates;
                    }
                }
                break;
        }
        return coordinates;
    }

    public static Coordinates walkRightAndDown(Coordinates from, int where2StartNewRowX, FieldDimension fieldDimension) {
        if (from.getX() < fieldDimension.getMaxX()) {
            return walkRight(from);
        } else if (from.getY() < fieldDimension.getMaxY()) {
            return new Coordinates(where2StartNewRowX, from.getY() + 1);
        } else {
            throw new IllegalStateException(from.toString());
        }
    }

    public static Coordinates walkLeftAndDown(Coordinates from, int where2StartNewRowX, FieldDimension fieldDimension) {
        if (from.getX() > fieldDimension.getMinX()) {
            return walkLeft(from);
        } else if (from.getY() < fieldDimension.getMaxY()) {
            return new Coordinates(where2StartNewRowX, from.getY() + 1);
        } else {
            throw new IllegalStateException(from.toString());
        }
    }

    public static Coordinates walkLeftAndUp(Coordinates from, int where2StartNewRowX, FieldDimension fieldDimension) {
        if (from.getX() > fieldDimension.getMinX()) {
            return walkLeft(from);
        } else if (from.getY() > fieldDimension.getMinY()) {
            return new Coordinates(where2StartNewRowX, from.getY() - 1);
        } else {
            throw new IllegalStateException(from.toString());
        }
    }

    public static Coordinates walkRightAndUp(Coordinates from, int where2StartNewRowX, FieldDimension fieldDimension) {
        if (from.getX() < fieldDimension.getMaxX()) {
            return walkRight(from);
        } else if (from.getY() > fieldDimension.getMinY()) {
            return new Coordinates(where2StartNewRowX, from.getY() - 1);
        } else {
            throw new IllegalStateException(from.toString());
        }
    }

    public static Coordinates next(Coordinates from, Direction direction, FieldDimension fieldDimension) {
        switch (direction) {
            case UP:
                if (isOnUpperEdge(from, fieldDimension)) {
                    throw new IllegalStateException("Coordinates are on upper edge");
                }
                return walkUp(from);
            case DOWN:
                if (isOnBottomEdge(from, fieldDimension)) {
                    throw new IllegalStateException("Coordinates are on bottom edge");
                }
                return walkDown(from);
            case LEFT:
                if (isOnLeftEdge(from, fieldDimension)) {
                    throw new IllegalStateException("Coordinates are on left edge");
                }
                return walkLeft(from);
            case RIGHT:
                if (isOnRightEdge(from, fieldDimension)) {
                    throw new IllegalStateException("Coordinates are on right edge");
                }
                return walkRight(from);
        }
        throw new IllegalArgumentException("Unknown direction " + direction);
    }

    private static Coordinates walkRight(Coordinates from) {
        return new Coordinates(from.getX() + 1, from.getY());
    }

    private static Coordinates walkLeft(Coordinates from) {
        return new Coordinates(from.getX() - 1, from.getY());
    }

    private static Coordinates walkUp(Coordinates from) {
        return new Coordinates(from.getX(), from.getY() - 1);
    }

    private static Coordinates walkDown(Coordinates from) {
        return new Coordinates(from.getX(), from.getY() + 1);
    }

    public static boolean canWalkInThatDirection(Coordinates coordinates, FieldDimension fieldDimension, Direction direction){
        return ! ((isOnUpperEdge(coordinates, fieldDimension) || direction == Direction.UP)
                && (isOnBottomEdge(coordinates, fieldDimension) || direction == Direction.DOWN)
                && (isOnLeftEdge(coordinates, fieldDimension) || direction == Direction.LEFT)
                && (isOnRightEdge(coordinates, fieldDimension) || direction == Direction.RIGHT));
    }

    public static boolean isOnLeftEdge(Coordinates coordinates, FieldDimension fieldDimension) {
        return coordinates.getX() == fieldDimension.getMinX();
    }

    public static boolean isOnRightEdge(Coordinates coordinates, FieldDimension fieldDimension) {
        return coordinates.getX() == fieldDimension.getMaxX();
    }

    public static boolean isOnUpperEdge(Coordinates coordinates, FieldDimension fieldDimension) {
        return coordinates.getY() == fieldDimension.getMinY();
    }

    public static boolean isOnBottomEdge(Coordinates coordinates, FieldDimension fieldDimension) {
        return coordinates.getY() == fieldDimension.getMaxY();
    }

    public static Coordinates generateLeftUpperCorner(FieldDimension fieldDimension) {
        return new Coordinates(fieldDimension.getMinX(), fieldDimension.getMinY());
    }

    public static Coordinates generateRightBottomCorner(FieldDimension fieldDimension) {
        return new Coordinates(fieldDimension.getMaxX(), fieldDimension.getMaxY());
    }

    public static Coordinates generateRightUpperCorner(FieldDimension fieldDimension) {
        return new Coordinates(fieldDimension.getMaxX(), fieldDimension.getMinY());
    }

    public static Coordinates generateLeftBottomCorner(FieldDimension fieldDimension) {
        return new Coordinates(fieldDimension.getMinX(), fieldDimension.getMaxY());
    }

}
