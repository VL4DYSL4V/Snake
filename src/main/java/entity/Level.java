package entity;

import enums.Direction;
import enums.LevelID;
import enums.MapObject;
import exception.EndOfGameException;
import exception.TimeIsUpException;
import util.CoordinateUtils;

import java.time.LocalTime;
import java.util.*;

// TODO: 26.11.2020 I think, maybe instead of keeping the list of previous directions,
//  I just need to set the parts of body to coordinates, where was the previous part.
//  That would be faster
public final class Level {

    private long scores = 0L;
    private final List<List<FieldObject>> field = new LinkedList<>();
    private final List<Coordinates> spawnCoordinates;
    private final FieldDimension fieldDimension;
    private final Snake snake;
    private final long scoresThreshold;
    private final LevelID levelID;
    private final int spawnFrequency;
    private LocalTime playTime;

    public Level(FieldDimension fieldDimension, Snake snake, long scoresThreshold, LocalTime playTime, LevelID levelID,
                 List<Coordinates> spawnCoordinates, int spawnFrequency) {
        this.fieldDimension = fieldDimension;
        this.snake = snake;
        this.scoresThreshold = scoresThreshold;
        this.playTime = playTime;
        this.levelID = levelID;
        this.spawnCoordinates = spawnCoordinates;
        this.spawnFrequency = spawnFrequency;
        setupField();
        updateSnakePosition(null);
    }

    public Level(FieldDimension fieldDimension, List<List<FieldObject>> elements, Snake snake,
                 long scoresThreshold, LocalTime playTime, LevelID levelID, List<Coordinates> spawnCoordinates, int spawnFrequency) {
        this(fieldDimension, snake, scoresThreshold, playTime, levelID, spawnCoordinates, spawnFrequency);
        for (List<FieldObject> fieldObjects : elements) {
            for (FieldObject object : fieldObjects) {
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

    private void updateSnakePosition(List<Coordinates> oldPosition) {
        if (oldPosition != null) {
            oldPosition.forEach(c -> nonSynchronizedSetObject(FieldObject.empty(c)));
        }
        snake.getSnakeParts().forEach(this::nonSynchronizedSetObject);
    }

    private boolean directionIsOppositeToCurrent(Direction direction) {
        switch (direction) {
            case UP:
                return snake.getCurrentDirection() == Direction.DOWN;
            case DOWN:
                return snake.getCurrentDirection() == Direction.UP;
            case RIGHT:
                return snake.getCurrentDirection() == Direction.LEFT;
            case LEFT:
                return snake.getCurrentDirection() == Direction.RIGHT;
        }
        throw new IllegalArgumentException("Unknown direction: " + direction);
    }

    private boolean directionIsEqualToCurrent(Direction direction) {
        return direction == snake.getCurrentDirection();
    }

    private FieldObject randomFood() {
        Random random = new Random();
        Coordinates coordinates = null;
        for (int i = 0; i < 10; i++) {
            Coordinates c = spawnCoordinates.get(random.nextInt(spawnCoordinates.size()));
            if (field.get(c.getY()).get(c.getX()).getMapObject() == MapObject.EMPTY) {
                coordinates = c;
            }
        }
        if (coordinates == null) {
            coordinates = spawnCoordinates.stream()
                    .filter(c -> field.get(c.getY()).get(c.getX()).getMapObject() == MapObject.EMPTY)
                    .findAny().orElse(spawnCoordinates.get(0));
            return FieldObject.empty(coordinates);
        }
        int chance = random.nextInt(3);
        switch (chance) {
            case 0:
                return FieldObject.apple(coordinates);
            case 1:
                return FieldObject.pear(coordinates);
            case 2:
                return FieldObject.mushroom(coordinates);
        }
        return FieldObject.scoreBonus(coordinates);
    }

    private FieldObject fieldObjectAt(Coordinates coordinates) {
        return field.get(coordinates.getY()).get(coordinates.getX());
    }

    private FieldObject getNextFieldObject(Direction direction) {
        Coordinates headCoordinates = snake.getSnakeParts().get(0).getCoordinates();
        int x = headCoordinates.getX();
        int y = headCoordinates.getY();
        switch (direction) {
            case LEFT:
                return fieldObjectAt(new Coordinates(x - 1, y));
            case RIGHT:
                return fieldObjectAt(new Coordinates(x + 1, y));
            case UP:
                return fieldObjectAt(new Coordinates(x, y - 1));
            case DOWN:
                return fieldObjectAt(new Coordinates(x, y + 1));
        }
        throw new IllegalArgumentException("Unknown direction: " + direction);
    }

    private void interactWithNextCell(Direction whereItMoves) throws EndOfGameException, TimeIsUpException {
        FieldObject next = getNextFieldObject(whereItMoves);
        switch (next.getMapObject()) {
            case EMPTY:
                return;
            case WALL:
                throw new EndOfGameException("You hit the wall at " + next.getCoordinates());
            case SNAKE_BODY:
                throw new EndOfGameException("You bitten yourself at + " + next.getCoordinates());
            case PEAR:
            case APPLE:
                scores += 5;
                incrementPlayTime(1);
                nonSynchronizedSetObject(FieldObject.empty(next.getCoordinates()));
                snake.appendTail();
                break;
            case MUSHROOM:
                scores += 10;
                nonSynchronizedSetObject(FieldObject.empty(next.getCoordinates()));
                if (snake.getSnakeParts().size() > 2) {
                    snake.cutOffTail(1).forEach(c -> nonSynchronizedSetObject(FieldObject.empty(c)));
                }
                decrementPlayTime(5);
                break;
            case SCORE_BONUS:
                scores += 15;
                nonSynchronizedSetObject(FieldObject.empty(next.getCoordinates()));
                break;
            case STONE:
                decrementPlayTime(5);
                break;
        }

    }

    private boolean canHitNextObject(Direction whereItMoves) {
        FieldObject next = getNextFieldObject(whereItMoves);
        return next.getMapObject() != MapObject.STONE;
    }

    private void move(Direction direction) throws EndOfGameException {
        TimeIsUpException exception = null;
        try {
            interactWithNextCell(direction);
        } catch (TimeIsUpException e) {
            exception = e;
        }
        if (!canHitNextObject(direction)) {
            return;
        }
        snake.updateDirections(direction);
        List<Coordinates> neuSnakeCoordinates = new LinkedList<>();
        List<Coordinates> oldSnakeCoordinates = new LinkedList<>();
        snake.getSnakeParts().forEach(sp -> oldSnakeCoordinates.add(sp.getCoordinates()));
        for (int i = 0; i < snake.getSnakeParts().size(); i++) {
            FieldObject e = snake.getSnakeParts().get(i);
            Direction previousDirection = snake.getPreviousDirection(i);
            Coordinates out = CoordinateUtils.next(e.getCoordinates(), previousDirection, fieldDimension);
            neuSnakeCoordinates.add(out);
        }
        snake.updateSnakeCoordinates(neuSnakeCoordinates);
        updateSnakePosition(oldSnakeCoordinates);
        if (exception != null) {
            EndOfGameException exc = new EndOfGameException();
            exc.initCause(exception);
            throw exc;
        }
    }

    /**********************
     *      INTERFACE      *
     * ********************/

    //
    public void spawnFood() {
        FieldObject fieldObject = randomFood();
        if (fieldObject.getMapObject() != MapObject.EMPTY) {
            List<FieldObject> row = field.get(fieldObject.getCoordinates().getY());
            row.set(fieldObject.getCoordinates().getX(), fieldObject);
        }
    }

    //
    public void incrementPlayTime(long seconds) {
        playTime = playTime.plusSeconds(seconds);
    }

    //
    public void decrementPlayTime(long seconds) throws TimeIsUpException {
        if (playTime.getHour() == 0 && playTime.getMinute() == 0 && playTime.getSecond() == 0) {
            throw new TimeIsUpException("Game over!");
        }
        if (playTime.getHour() * 60 * 60 + playTime.getMinute() * 60 + playTime.getSecond() <= seconds) {
            playTime = LocalTime.of(0, 0, 0);
            throw new TimeIsUpException("Game over!");
        }
        playTime = playTime.minusSeconds(seconds);

    }

    //
    public void turnSnake(Direction direction) throws EndOfGameException {
        if (directionIsOppositeToCurrent(direction) || directionIsEqualToCurrent(direction)) {
            return;
        }
        if (!CoordinateUtils.canWalkInThatDirection(
                snake.getSnakeParts().get(0).getCoordinates(), fieldDimension, direction)) {
            throw new EndOfGameException("You fell of the " + direction + " edge");
        }
        move(direction);
    }

    public void moveInCurrentDirection() throws EndOfGameException {
        if (!CoordinateUtils.canWalkInThatDirection(
                snake.getSnakeParts().get(0).getCoordinates(), fieldDimension, snake.getCurrentDirection())) {
            throw new EndOfGameException("You fell of the " + snake.getCurrentDirection() + " edge");
        }
        move(snake.getCurrentDirection());
    }

    public boolean turningPerformed(){
        return snake.getCurrentDirection() != snake.getPreviousDirection();
    }

    /**********************
     *      GETTERS       *
     * ********************/

    //
    public LocalTime getPlayTime() {
        return playTime;
    }

    //
    public long getScores() {
        return scores;
    }

    public long getScoresThreshold() {
        return scoresThreshold;
    }

    public FieldDimension getFieldDimension() {
        return fieldDimension;
    }

    public LevelID getLevelID() {
        return levelID;
    }

    public int getSpawnFrequency() {
        return spawnFrequency;
    }

    //
    public List<FieldObject> getFieldObjects() {
        List<FieldObject> out = new LinkedList<>();
        field.forEach(row -> row.stream()
                .filter(obj -> obj.getMapObject() != MapObject.EMPTY).forEach(out::add));
        return out;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Level level = (Level) o;
        return scores == level.scores &&
                scoresThreshold == level.scoresThreshold &&
                spawnFrequency == level.spawnFrequency &&
                Objects.equals(field, level.field) &&
                Objects.equals(spawnCoordinates, level.spawnCoordinates) &&
                Objects.equals(fieldDimension, level.fieldDimension) &&
                Objects.equals(snake, level.snake) &&
                levelID == level.levelID &&
                Objects.equals(playTime, level.playTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scores, field, spawnCoordinates, fieldDimension, snake, scoresThreshold, levelID, spawnFrequency, playTime);
    }

    @Override
    public String toString() {
        return "Level{" +
                "scores=" + scores +
                ", field=" + field +
                ", spawnCoordinates=" + spawnCoordinates +
                ", fieldDimension=" + fieldDimension +
                ", snake=" + snake +
                ", scoresThreshold=" + scoresThreshold +
                ", levelID=" + levelID +
                ", spawnFrequency=" + spawnFrequency +
                ", playTime=" + playTime +
                '}';
    }
}
