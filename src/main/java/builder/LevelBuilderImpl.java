package builder;

import entity.*;
import enums.LevelID;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Deprecated
public final class LevelBuilderImpl implements LevelBuilder {

    private FieldDimension fieldDimension = new FieldDimension(0, 0);
    private long scoresThreshold = 0L;
    private Snake snake;
    private LocalTime playTime = LocalTime.of(0, 0, 0, 0);
    private LevelID levelID;
    private int spawnFrequency = 0;
    private List<Coordinates> spawnCoordinates = new LinkedList<>();
    private List<FieldObject> apples = new LinkedList<>();
    private List<FieldObject> pears = new LinkedList<>();
    private List<FieldObject> mushrooms = new LinkedList<>();
    private List<FieldObject> scoreBonuses = new LinkedList<>();
    private List<FieldObject> stones = new LinkedList<>();
    private List<FieldObject> walls = new LinkedList<>();

    public LevelBuilderImpl(LevelID levelID, Snake snake) {
        this.levelID = levelID;
        this.snake = snake;
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
    public LevelBuilder setScoresThreshold(long scoresThreshold) {
        this.scoresThreshold = scoresThreshold;
        return this;
    }

    @Override
    public LevelBuilder setPlayTime(LocalTime playTime) {
        this.playTime = playTime;
        return this;
    }

    @Override
    public LevelBuilder setLevelID(LevelID levelID) {
        this.levelID = levelID;
        return this;
    }

    @Override
    public LevelBuilder setSpawnCoordinates(List<Coordinates> spawnCoordinates) {
        this.spawnCoordinates = spawnCoordinates;
        return this;
    }

    @Override
    public LevelBuilder setSpawnFrequency(int spawnFrequency) {
        this.spawnFrequency = spawnFrequency;
        return this;
    }

    @Override
    public LevelBuilder setSnake(Snake snake) {
        this.snake = snake;
        return this;
    }


    public Level build() {
        List<List<FieldObject>> objects = new LinkedList<>();
        objects.add(apples);
        objects.add(pears);
        objects.add(mushrooms);
        objects.add(scoreBonuses);
        objects.add(stones);
        objects.add(walls);
        return new Level(fieldDimension, objects, snake, scoresThreshold,
                playTime, levelID, spawnCoordinates, spawnFrequency);
    }
}
