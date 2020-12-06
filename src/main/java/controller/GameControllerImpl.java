package controller;

import context.ApplicationContext;
import entity.Coordinates;
import entity.FieldObject;
import entity.Level;
import enums.Direction;
import enums.MapObject;
import event.GameEvent;
import exception.EndOfGameException;
import exception.TimeIsUpException;
import observer.gameEvent.GameEventPublisher;
import observer.gameEvent.GameEventSubscriber;
import util.coordinateUtil.CoordinateUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class GameControllerImpl implements GameController, GameEventPublisher {

    private final ApplicationContext applicationContext;

    private ScheduledFuture<?> crawlingFuture;
    private ScheduledFuture<?> countDownFuture;
    private ScheduledFuture<?> foodSpawnFuture;

    private volatile boolean turningPerformed = false;
    private volatile boolean gameOver = false;

    private final List<GameEventSubscriber> gameEventSubscribers = new LinkedList<>();
    private final Map<String, GameEvent> eventMap = new HashMap<>();

    public GameControllerImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        setupEventMap();
    }

    private void setupEventMap(){
        eventMap.put("gameOver", GameEvent.gameOverEvent());
        eventMap.put("levelStateChanged", GameEvent.levelStateChangedEvent());
    }

    private void spawnFood() {
        FieldObject fieldObject = randomFood();
        if (fieldObject != null) {
            applicationContext.getLevel().setObject(fieldObject);
        }
    }

    @Nullable
    private FieldObject randomFood() {
        Coordinates coordinates = applicationContext.getLevel().randomAvailableSpawnCoordinates();
        if (coordinates == null) {
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

    private void interactWithNextCell(Direction whereItMoves) throws EndOfGameException, TimeIsUpException {
        Level level = applicationContext.getLevel();
        FieldObject next = level.getNextToHeadFieldObject(whereItMoves);
        switch (next.getMapObject()) {
            case EMPTY:
                return;
            case WALL:
                throw new EndOfGameException("You hit the wall at " + next.getCoordinates());
            case SNAKE_BODY:
                throw new EndOfGameException("You have bitten yourself at + " + next.getCoordinates());
            case PEAR:
            case APPLE:
                level.incrementScores(5);
                level.incrementPlayTime(1);
                level.setObject(FieldObject.empty(next.getCoordinates()));
                level.appendSnakeTail();
                break;
            case MUSHROOM:
                level.incrementScores(15);
                level.decrementPlayTime(5);
                level.setObject(FieldObject.empty(next.getCoordinates()));
                level.cutAsMuchTailAsPossible(1)
                        .forEach(c -> applicationContext.getLevel().setObject(FieldObject.empty(c)));
                break;
            case SCORE_BONUS:
                level.incrementScores(15);
                level.setObject(FieldObject.empty(next.getCoordinates()));
                break;
            case STONE:
                level.decrementPlayTime(5);
                break;
        }

    }

    private boolean canHitNextObject(Direction whereItMoves) {
        FieldObject next = applicationContext.getLevel().getNextToHeadFieldObject(whereItMoves);
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
        applicationContext.getLevel().moveSnake(direction);
        if (exception != null) {
            EndOfGameException exc = new EndOfGameException();
            exc.initCause(exception);
            throw exc;
        }
    }

    private void turnSnake(Direction direction) throws EndOfGameException {
        if (applicationContext.getLevel().snakeDirectionIsOppositeToCurrent(direction)
                || direction == applicationContext.getLevel().getSnakeCurrentDirection()) {
            return;
        }
        if (!CoordinateUtils.canWalkInThatDirection(applicationContext.getLevel().snakeHeadCoordinates(),
                applicationContext.getLevel().getFieldDimension(), direction)) {
            throw new EndOfGameException("You fell of the " + direction + " edge");
        }
        move(direction);
    }

    private void moveInCurrentDirection() throws EndOfGameException {
        Direction currentDirection = applicationContext.getLevel().getSnakeCurrentDirection();
        if (!CoordinateUtils.canWalkInThatDirection(applicationContext.getLevel().snakeHeadCoordinates(),
                applicationContext.getLevel().getFieldDimension(), currentDirection)) {
            throw new EndOfGameException("You fell of the " + currentDirection + " edge");
        }
        move(currentDirection);
    }

    @Override
    public void moveSnake(Direction direction) {
        synchronized (applicationContext) {
            try {
                if (!(gameOver
                        || applicationContext.getLevel().snakeDirectionIsOppositeToCurrent(direction)
                        || applicationContext.getLevel().getSnakeCurrentDirection() == direction)) {
                    turnSnake(direction);
                    notifySubscribers(eventMap.get("levelStateChanged"));
                    turningPerformed = true;
                }
            } catch (EndOfGameException e) {
                stop();
                notifySubscribers(eventMap.get("gameOver"));
            }
        }
    }

    private void startAutomaticCrawling() {
        Runnable crawling = () -> {
            synchronized (applicationContext) {
                try {
                    if (!turningPerformed) {
                        moveInCurrentDirection();
                    } else {
                        turningPerformed = false;
                    }
                } catch (EndOfGameException e) {
                    stop();
                    notifySubscribers(eventMap.get("gameOver"));
                }
                notifySubscribers(eventMap.get("levelStateChanged"));
            }
        };

        crawlingFuture = applicationContext.getScheduledExecutorService()
                .scheduleAtFixedRate(crawling, 2, 250, TimeUnit.MILLISECONDS);
    }

    private void stopAutomaticCrawling() {
        if (crawlingFuture != null) {
            crawlingFuture.cancel(false);
        }
    }

    private void startFruitSpawn() {
        Runnable fruitSpawn = () -> {
            synchronized (applicationContext) {
                spawnFood();
                notifySubscribers(eventMap.get("levelStateChanged"));
            }
        };
        foodSpawnFuture = applicationContext.getScheduledExecutorService()
                .scheduleAtFixedRate(fruitSpawn, 2,
                        applicationContext.getLevel().getSpawnFrequency(), TimeUnit.SECONDS);
    }

    private void stopFruitSpawn() {
        if (foodSpawnFuture != null) {
            foodSpawnFuture.cancel(false);
        }
    }

    public void startCountDown() {
        Runnable countdown = () -> {
            synchronized (applicationContext) {
                try {
                    applicationContext.getLevel().decrementPlayTime(1);
                } catch (TimeIsUpException e) {
                    stop();
                    notifySubscribers(eventMap.get("gameOver"));
                }
                notifySubscribers(eventMap.get("levelStateChanged"));
            }
        };
        countDownFuture = applicationContext.getScheduledExecutorService()
                .scheduleAtFixedRate(countdown, 2, 1, TimeUnit.SECONDS);
    }

    private void stopCountDown() {
        if (countDownFuture != null) {
            countDownFuture.cancel(false);
        }
    }

    @Override
    public void start() {
        gameOver = false;
        startCountDown();
        startFruitSpawn();
        startAutomaticCrawling();
    }

    @Override
    public void stop() {
        gameOver = true;
        stopCountDown();
        stopFruitSpawn();
        stopAutomaticCrawling();
    }

    @Override
    public void subscribe(GameEventSubscriber gameEventSubscriber) {
        gameEventSubscribers.add(gameEventSubscriber);
    }

    @Override
    public void unsubscribe(GameEventSubscriber gameEventSubscriber) {
        gameEventSubscribers.remove(gameEventSubscriber);
    }

    @Override
    public void notifySubscribers(GameEvent gameEvent) {
        gameEventSubscribers.forEach(gameEventSubscriber -> gameEventSubscriber.react(gameEvent));
    }

}
