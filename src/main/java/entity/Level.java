package entity;

import enums.Direction;
import enums.LevelID;
import enums.MapObject;
import exception.EndOfGameException;
import exception.TimeIsUpException;
import util.coordinateUtil.CoordinateUtils;
import util.xmlUtil.DateLocalTimeAdapter;

import javax.annotation.Nullable;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class Level implements Serializable {

    private long scores = 0L;
    @XmlElementWrapper(name = "rows")
    @XmlElement(name = "row")
    private final List<RowHolder> field = new LinkedList<>();
    @XmlElementWrapper(name = "spawn_coordinates")
    private List<Coordinates> spawnCoordinates;
    private FieldDimension fieldDimension;
    private Snake snake;
    private long scoresThreshold;
    @XmlAttribute
    private LevelID levelID;
    private int spawnFrequency;
    @XmlJavaTypeAdapter(DateLocalTimeAdapter.class)
    private LocalTime playTime;

    private static final long serialVersionUID = 29834868761341L;

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    private static final class RowHolder implements Serializable{

        @XmlAttribute
        private int id = 0;

        @XmlElement(name = "field_object")
        private List<FieldObject> row = new LinkedList<>();

        private static final long serialVersionUID = -28379098789910L;

        private RowHolder() {
        }

        private RowHolder(int id, List<FieldObject> row) {
            this.id = id;
            this.row = row;
        }

        public List<FieldObject> getRow() {
            return row;
        }

        public int getId() {
            return id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RowHolder rowHolder = (RowHolder) o;
            return id == rowHolder.id &&
                    Objects.equals(row, rowHolder.row);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, row);
        }

        @Override
        public String toString() {
            return "RowHolder{" +
                    "id=" + id +
                    ", row=" + row +
                    '}';
        }
    }

    private Level(){

    }

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
        snake.getSnakeParts().forEach(this::setObject);
    }

    public Level(FieldDimension fieldDimension, List<List<FieldObject>> elements, Snake snake,
                 long scoresThreshold, LocalTime playTime, LevelID levelID, List<Coordinates> spawnCoordinates, int spawnFrequency) {
        this(fieldDimension, snake, scoresThreshold, playTime, levelID, spawnCoordinates, spawnFrequency);
        for (List<FieldObject> fieldObjects : elements) {
            for (FieldObject object : fieldObjects) {
                setObject(object);
            }
        }
    }

    private void setupField() {
        for (int y = 0; y < fieldDimension.getHeight(); y++) {
            List<FieldObject> row = new LinkedList<>();
            for (int x = 0; x < fieldDimension.getWidth(); x++) {
                row.add(FieldObject.empty(new Coordinates(x, y)));
            }
            RowHolder rowHolder = new RowHolder(y, row);
            field.add(rowHolder);
        }
    }

    private void setObject(FieldObject fieldObject) {
        Coordinates coordinates = fieldObject.getCoordinates();
        List<FieldObject> row = field.get(coordinates.getY()).getRow();
        row.set(coordinates.getX(), fieldObject);
    }

    private boolean snakeDirectionIsOppositeToCurrent(Direction direction) {
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

    @Nullable
    private Coordinates randomAvailableSpawnCoordinates(){
        Random random = new Random();
        Coordinates coordinates = null;
        for (int i = 0; i < 10; i++) {
            Coordinates c = spawnCoordinates.get(random.nextInt(spawnCoordinates.size()));
            if (fieldObjectAt(c).getMapObject() == MapObject.EMPTY) {
                coordinates = c;
            }
        }
        if (coordinates == null) {
            coordinates = spawnCoordinates.stream()
                    .filter(c -> fieldObjectAt(c).getMapObject() == MapObject.EMPTY)
                    .findAny().orElse(null);
        }
        return coordinates;
    }

    @Nullable
    private FieldObject randomFood() {
        Coordinates coordinates = randomAvailableSpawnCoordinates();
        if(coordinates == null){
            return null;
        }
        int chance = new Random().nextInt(4);
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
        return field.get(coordinates.getY()).getRow().get(coordinates.getX());
    }

    private FieldObject getNextFieldObject(Direction direction) {
        Coordinates headCoordinates = snake.getSnakeParts().get(0).getCoordinates();
        switch (direction) {
            case LEFT:
                return fieldObjectAt(CoordinateUtils.walkLeft(headCoordinates));
            case RIGHT:
                return fieldObjectAt(CoordinateUtils.walkRight(headCoordinates));
            case UP:
                return fieldObjectAt(CoordinateUtils.walkUp(headCoordinates));
            case DOWN:
                return fieldObjectAt(CoordinateUtils.walkDown(headCoordinates));
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
                throw new EndOfGameException("You have bitten yourself at + " + next.getCoordinates());
            case PEAR:
            case APPLE:
                scores += 5;
                incrementPlayTime(1);
                setObject(FieldObject.empty(next.getCoordinates()));
                snake.appendTail();
                break;
            case MUSHROOM:
                scores += 10;
                setObject(FieldObject.empty(next.getCoordinates()));
                snake.cutAsMuchTailAsPossible(1).forEach(c -> setObject(FieldObject.empty(c)));
                decrementPlayTime(5);
                break;
            case SCORE_BONUS:
                scores += 15;
                setObject(FieldObject.empty(next.getCoordinates()));
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

        Coordinates next = CoordinateUtils.next(snake.getSnakeParts().get(0).getCoordinates(), direction, fieldDimension);
        List<FieldObject> oldSnakeParts = snake.move(next);
        snake.setCurrentDirection(direction);
        oldSnakeParts.forEach(sp -> setObject(FieldObject.empty(sp.getCoordinates())));
        snake.getSnakeParts().forEach(this::setObject);

        if (exception != null) {
            EndOfGameException exc = new EndOfGameException();
            exc.initCause(exception);
            throw exc;
        }
    }

    /**********************
     *      INTERFACE      *
     * ********************/

    public void spawnFood() {
        FieldObject fieldObject = randomFood();
        if (fieldObject != null) {
            setObject(fieldObject);
        }
    }

    public void incrementPlayTime(long seconds) {
        playTime = playTime.plusSeconds(seconds);
    }

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

    public void turnSnake(Direction direction) throws EndOfGameException {
        if (snakeDirectionIsOppositeToCurrent(direction) || direction == snake.getCurrentDirection()) {
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

    /**********************
     *      GETTERS       *
     * ********************/

    public LocalTime getPlayTime() {
        return playTime;
    }

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

    public List<FieldObject> getFieldObjects() {
        List<FieldObject> out = new LinkedList<>();
        field.forEach(rowHolder -> rowHolder.getRow().stream()
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
