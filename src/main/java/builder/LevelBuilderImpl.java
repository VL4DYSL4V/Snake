package builder;

import entity.FieldDimension;
import entity.FieldObject;
import entity.Level;

import java.util.LinkedList;
import java.util.List;

public final class LevelBuilderImpl implements LevelBuilder {

    private FieldDimension fieldDimension;

    private List<FieldObject> apples = new LinkedList<>();
    private List<FieldObject> pears = new LinkedList<>();
    private List<FieldObject> mushrooms = new LinkedList<>();
    private List<FieldObject> scoreBonuses = new LinkedList<>();
    private List<FieldObject> stones = new LinkedList<>();
    private List<FieldObject> walls = new LinkedList<>();

    public LevelBuilderImpl(FieldDimension fieldDimension) {
        this.fieldDimension = fieldDimension;
    }

    @Override
    public LevelBuilder setFieldDimension(FieldDimension fieldDimension) {
        this.fieldDimension = fieldDimension;
        return this;
    }

    @Override
    public LevelBuilder setApples(List<FieldObject> apples) {
        this.apples = apples;
        return this;
    }

    @Override
    public LevelBuilder setPears(List<FieldObject> pears) {
        this.pears = pears;
        return this;
    }

    @Override
    public LevelBuilder setMushrooms(List<FieldObject> mushrooms) {
        this.mushrooms = mushrooms;
        return this;
    }

    @Override
    public LevelBuilder setScoreBonus(List<FieldObject> scoreBonuses) {
        this.scoreBonuses = scoreBonuses;
        return this;
    }

    @Override
    public LevelBuilder setStones(List<FieldObject> stones) {
        this.stones = stones;
        return this;
    }

    @Override
    public LevelBuilder setWalls(List<FieldObject> walls) {
        this.walls = walls;
        return this;
    }

    @Override
    public FieldDimension getFieldDimension() {
        return fieldDimension;
    }

    public Level build(){
        List<List<FieldObject>> objects = new LinkedList<>();
        objects.add(apples);
        objects.add(pears);
        objects.add(mushrooms);
        objects.add(scoreBonuses);
        objects.add(stones);
        objects.add(walls);
        return new Level(fieldDimension, objects);
    }
}
