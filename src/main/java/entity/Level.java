package entity;

import enums.MapObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class Level {

    private long scores = 0L;
    private List<List<FieldObject>> field = new LinkedList<>();
    private final FieldDimension fieldDimension;

    public Level(FieldDimension fieldDimension) {
        this.fieldDimension = fieldDimension;
        setupField();
    }

    public Level(FieldDimension fieldDimension, List<List<FieldObject>> elements) {
        this(fieldDimension);
        for (List<FieldObject> fieldObjects : elements) {
            for(FieldObject object: fieldObjects){
                nonSynchronizedSetObject(object);
            }
        }
    }

    private void setupField() {
        for (int y = 0; y < fieldDimension.getHeight(); y++) {
            List<FieldObject> row = new LinkedList<>();
            for (int x = 0; x < fieldDimension.getWidth(); x++) {
                row.add(FieldObject.empty(new Coordinates(x, y)));
            }
            field.add(row);
        }
    }

    private void nonSynchronizedSetObject(FieldObject fieldObject) {
        Coordinates coordinates = fieldObject.getCoordinates();
        List<FieldObject> row = field.get(coordinates.getY());
        row.set(coordinates.getX(), fieldObject);
    }

    public void setObject(FieldObject fieldObject) {
        Coordinates coordinates = fieldObject.getCoordinates();
        synchronized (this) {
            List<FieldObject> row = field.get(coordinates.getY());
            row.set(coordinates.getX(), fieldObject);
        }
    }

//    public void setRow(MapObject mapObject, Coordinates start, int length) {
//        if (start.getX() + length > fieldDimension.getWidth()) {
//            throw new IllegalArgumentException("start.getX() + length >= fieldDimension.getWidth()");
//        }
//        synchronized (this) {
//            List<FieldObject> row = field.get(start.getY());
//            for (int i = 0; i < length; i++) {
//                row.set(start.getX() + i,
//                        new FieldObject(mapObject, new Coordinates(start.getX() + i, start.getY())));
//            }
//        }
//    }

//    public void setColumn(MapObject mapObject, Coordinates start, int length) {
//        if (start.getY() + length > fieldDimension.getHeight()) {
//            throw new IllegalArgumentException("start.getX() + length >= fieldDimension.getWidth()");
//        }
//        synchronized (this) {
//            for (int i = 0; i < length; i++) {
//                List<FieldObject> row = field.get(start.getY() + i);
//                row.set(start.getX(),
//                        new FieldObject(mapObject, new Coordinates(start.getX(), start.getY() + i)));
//            }
//        }
//    }
//
//    public List<List<FieldObject>> getField() {
//        List<List<FieldObject>> out = new LinkedList<>();
//        synchronized (this) {
//            for (List<FieldObject> fieldObjects : field) {
//                out.add(Collections.unmodifiableList(fieldObjects));
//            }
//        }
//        return Collections.unmodifiableList(out);
//    }

    public List<FieldObject> getFieldObjects() {
        List<FieldObject> out = new LinkedList<>();
        synchronized (this) {
            field.forEach(row -> row.stream()
                    .filter(obj -> obj.getMapObject() != MapObject.EMPTY).forEach(out::add));
        }
        return out;
    }

    public synchronized void incrementScores(long amount) {
        scores += amount;
    }

    public synchronized long getScores() {
        return scores;
    }

    public FieldDimension getFieldDimension() {
        return fieldDimension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Level level = (Level) o;
        return scores == level.scores &&
                Objects.equals(field, level.field) &&
                Objects.equals(fieldDimension, level.fieldDimension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scores, field, fieldDimension);
    }

    @Override
    public String toString() {
        return "Level{" +
                "scores=" + scores +
                ", field=" + field +
                ", fieldDimension=" + fieldDimension +
                '}';
    }
}
