package levelConstructor;

import builder.LevelBuilderImpl;
import entity.*;
import enums.Direction;
import enums.LevelID;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

public final class LevelConstructorImpl implements LevelConstructor {

    @Override
    public Level constructFirstLevel() {
        List<Coordinates> snakeCoordinates = new LinkedList<>();
        snakeCoordinates.add(new Coordinates(5, 15));
        snakeCoordinates.add(new Coordinates(5, 16));
        snakeCoordinates.add(new Coordinates(5, 17));
        snakeCoordinates.add(new Coordinates(5, 18));
        snakeCoordinates.add(new Coordinates(5, 19));
        Snake snake = new Snake(snakeCoordinates, Direction.UP);
        LevelBuilderImpl builder = new LevelBuilderImpl(LevelID.ONE, snake);
        FieldDimension dimension = new FieldDimension(20, 30);

        List<FieldObject> walls = createBorderBox(0, dimension.getMaxX(), 0, dimension.getMaxY());
        walls.addAll(createBorderBox(6, 12, 9, 18));

        List<FieldObject> stones = new LinkedList<>();
        stones.add(FieldObject.stone(new Coordinates(9, 4)));
        stones.add(FieldObject.stone(new Coordinates(4, 3)));
        stones.add(FieldObject.stone(new Coordinates(3, 26)));
        stones.add(FieldObject.stone(new Coordinates(15, 26)));

        List<Coordinates> spawnCoordinates = new LinkedList<>();

        spawnCoordinates.add(new Coordinates(2,3));
        spawnCoordinates.add(new Coordinates(2,2));
        spawnCoordinates.add(new Coordinates(3,4));
        spawnCoordinates.add(new Coordinates(4,3));
        spawnCoordinates.add(new Coordinates(5,4));
        spawnCoordinates.add(new Coordinates(13,3));
        spawnCoordinates.add(new Coordinates(14,5));
        spawnCoordinates.add(new Coordinates(15,7));
        spawnCoordinates.add(new Coordinates(16,19));
        spawnCoordinates.add(new Coordinates(13,23));
        spawnCoordinates.add(new Coordinates(13,25));
        spawnCoordinates.add(new Coordinates(10,24));
        spawnCoordinates.add(new Coordinates(15,25));
        spawnCoordinates.add(new Coordinates(7,21));
        spawnCoordinates.add(new Coordinates(6,7));
        spawnCoordinates.add(new Coordinates(2,13));
        spawnCoordinates.add(new Coordinates(4,11));

        List<FieldObject> apples = new LinkedList<>();
        apples.add(FieldObject.apple(new Coordinates(5, 3)));
        apples.add(FieldObject.apple(new Coordinates(16, 25)));
        apples.add(FieldObject.apple(new Coordinates(3, 14)));
        apples.add(FieldObject.apple(new Coordinates(16, 4)));
        apples.add(FieldObject.apple(new Coordinates(14, 14)));

        List<FieldObject> pears = new LinkedList<>();
        pears.add(FieldObject.pear(new Coordinates(7, 4)));
        pears.add(FieldObject.pear(new Coordinates(18, 9)));
        pears.add(FieldObject.pear(new Coordinates(3, 18)));
        pears.add(FieldObject.pear(new Coordinates(5, 23)));

        List<FieldObject> scoreBonus = new LinkedList<>();
        scoreBonus.add(FieldObject.scoreBonus(new Coordinates(6, 25)));
        scoreBonus.add(FieldObject.scoreBonus(new Coordinates(18, 16)));

        List<FieldObject> mushrooms = new LinkedList<>();
        mushrooms.add(FieldObject.mushroom(new Coordinates(16, 22)));
        mushrooms.add(FieldObject.mushroom(new Coordinates(14, 8)));
        mushrooms.add(FieldObject.mushroom(new Coordinates(5, 10)));

        builder.setFieldDimension(dimension)
                .setWalls(walls)
                .setStones(stones)
                .setApples(apples)
                .setPears(pears)
                .setScoreBonus(scoreBonus)
                .setMushrooms(mushrooms)
                .setSpawnCoordinates(spawnCoordinates)
                .setSpawnFrequency(8)
                .setPlayTime(LocalTime.of(0, 1, 0))
                .setScoresThreshold(100);
        return builder.build();
    }

    @Override
    public Level constructSecondLevel() {
        List<Coordinates> snakeCoordinates = new LinkedList<>();
        snakeCoordinates.add(new Coordinates(10, 15));
        snakeCoordinates.add(new Coordinates(11, 15));
        snakeCoordinates.add(new Coordinates(12, 15));
        snakeCoordinates.add(new Coordinates(13, 15));
        snakeCoordinates.add(new Coordinates(14, 15));
        Snake snake = new Snake(snakeCoordinates, Direction.LEFT);

        LevelBuilderImpl builder = new LevelBuilderImpl(LevelID.TWO, snake);
        FieldDimension dimension = new FieldDimension(30, 30);

        List<FieldObject> walls = new LinkedList<>(createBorderRow(0, 10, 0));
        walls.addAll(createBorderRow(19, 29, 0));
        walls.addAll(createBorderRow(10, 19, 7));
        walls.addAll(createBorderRow(10, 19, 21));
        walls.addAll(createBorderRow(0, 10, 29));
        walls.addAll(createBorderRow(19, 29, 29));
        walls.addAll(createBorderColumn(1, 28, 0));
        walls.addAll(createBorderColumn(1, 6, 10));
        walls.addAll(createBorderColumn(1, 6, 19));
        walls.addAll(createBorderColumn(1, 28, 29));
        walls.addAll(createBorderColumn(22, 28, 10));
        walls.addAll(createBorderColumn(22, 28, 19));

        List<Coordinates> spawnCoordinates = new LinkedList<>();

        spawnCoordinates.add(new Coordinates(2,2));
        spawnCoordinates.add(new Coordinates(2,5));
        spawnCoordinates.add(new Coordinates(3,14));
        spawnCoordinates.add(new Coordinates(2,23));
        spawnCoordinates.add(new Coordinates(3,26));
        spawnCoordinates.add(new Coordinates(4,7));
        spawnCoordinates.add(new Coordinates(9,2));
        spawnCoordinates.add(new Coordinates(11,11));
        spawnCoordinates.add(new Coordinates(13,18));
        spawnCoordinates.add(new Coordinates(13,14));
        spawnCoordinates.add(new Coordinates(12,17));
        spawnCoordinates.add(new Coordinates(7,24));
        spawnCoordinates.add(new Coordinates(6,26));
        spawnCoordinates.add(new Coordinates(3,27));
        spawnCoordinates.add(new Coordinates(19,13));
        spawnCoordinates.add(new Coordinates(23,3));
        spawnCoordinates.add(new Coordinates(24,4));
        spawnCoordinates.add(new Coordinates(21,10));
        spawnCoordinates.add(new Coordinates(26,13));
        spawnCoordinates.add(new Coordinates(27,19));
        spawnCoordinates.add(new Coordinates(21,25));
        spawnCoordinates.add(new Coordinates(28,23));
        spawnCoordinates.add(new Coordinates(28,29));

        List<FieldObject> stones = new LinkedList<>();
        stones.add(FieldObject.stone(new Coordinates(3, 4)));
        stones.add(FieldObject.stone(new Coordinates(8, 11)));
        stones.add(FieldObject.stone(new Coordinates(19, 19)));
        stones.add(FieldObject.stone(new Coordinates(17, 11)));
        stones.add(FieldObject.stone(new Coordinates(24, 7)));
        stones.add(FieldObject.stone(new Coordinates(27, 26)));
        stones.add(FieldObject.stone(new Coordinates(12, 13)));
        stones.add(FieldObject.stone(new Coordinates(5, 26)));

        List<FieldObject> apples = new LinkedList<>();
        apples.add(FieldObject.apple(new Coordinates(5, 3)));
        apples.add(FieldObject.apple(new Coordinates(26, 25)));
        apples.add(FieldObject.apple(new Coordinates(3, 14)));
        apples.add(FieldObject.apple(new Coordinates(6, 23)));
        apples.add(FieldObject.apple(new Coordinates(14, 14)));
        apples.add(FieldObject.apple(new Coordinates(21, 4)));
        apples.add(FieldObject.apple(new Coordinates(24, 13)));

        List<FieldObject> pears = new LinkedList<>();
        pears.add(FieldObject.pear(new Coordinates(7, 4)));
        pears.add(FieldObject.pear(new Coordinates(18, 9)));
        pears.add(FieldObject.pear(new Coordinates(3, 18)));
        pears.add(FieldObject.pear(new Coordinates(25, 23)));
        pears.add(FieldObject.pear(new Coordinates(10, 19)));
        pears.add(FieldObject.pear(new Coordinates(27, 4)));

        List<FieldObject> scoreBonus = new LinkedList<>();
        scoreBonus.add(FieldObject.scoreBonus(new Coordinates(6, 25)));
        scoreBonus.add(FieldObject.scoreBonus(new Coordinates(18, 16)));
        scoreBonus.add(FieldObject.scoreBonus(new Coordinates(26, 25)));
        scoreBonus.add(FieldObject.scoreBonus(new Coordinates(5, 9)));
        scoreBonus.add(FieldObject.scoreBonus(new Coordinates(24, 15)));

        List<FieldObject> mushrooms = new LinkedList<>();
        mushrooms.add(FieldObject.mushroom(new Coordinates(21, 25)));
        mushrooms.add(FieldObject.mushroom(new Coordinates(14, 8)));
        mushrooms.add(FieldObject.mushroom(new Coordinates(8, 13)));

        builder.setFieldDimension(dimension)
                .setWalls(walls)
                .setStones(stones)
                .setApples(apples)
                .setPears(pears)
                .setScoreBonus(scoreBonus)
                .setMushrooms(mushrooms)
                .setScoresThreshold(300)
                .setSpawnCoordinates(spawnCoordinates)
                .setSpawnFrequency(7)
                .setPlayTime(LocalTime.of(0, 2, 0));
        return builder.build();
    }

    private List<FieldObject> createBorderBox(int startX, int lastX, int startY, int lastY) {
        List<FieldObject> walls = new LinkedList<>();
        for (int x = startX; x <= lastX; x++) {
            walls.add(FieldObject.wall(new Coordinates(x, startY)));
            walls.add(FieldObject.wall(new Coordinates(x, lastY)));
        }
        for (int y = startY + 1; y <= lastY - 1; y++) {
            walls.add(FieldObject.wall(new Coordinates(startX, y)));
            walls.add(FieldObject.wall(new Coordinates(lastX, y)));
        }
        return walls;
    }

    private List<FieldObject> createBorderRow(int startX, int lastX, int y) {
        List<FieldObject> walls = new LinkedList<>();
        for (int x = startX; x <= lastX; x++) {
            walls.add(FieldObject.wall(new Coordinates(x, y)));
        }
        return walls;
    }

    private List<FieldObject> createBorderColumn(int startY, int lastY, int x) {
        List<FieldObject> walls = new LinkedList<>();
        for (int y = startY; y <= lastY; y++) {
            walls.add(FieldObject.wall(new Coordinates(x, y)));
        }
        return walls;
    }
}
