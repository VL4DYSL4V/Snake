package application;

import context.ApplicationContext;
import handler.ChangeLevelHandlerImpl;
import enums.Direction;
import exception.EndOfGameException;
import exception.TimeIsUpException;
import handler.ExitHandler;
import controller.GameController;
import ui.EndOfGameFrame;
import ui.GameFrame;
import ui.MainWindow;
import ui.WindowHolder;

import javax.swing.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

// TODO: 26.11.2020 Think of better thread interaction than just monopolizing ApplicationContext by one thread
public final class Application {

    private final WindowHolder windowHolder;
    private final ApplicationContext applicationContext = new ApplicationContext();

    private volatile boolean gameOver = false;
    private volatile boolean turningPerformed = false;

    // TODO: 26.11.2020 THIS MUST BE SOMEHOW MOVED IN SEPARATE CLASSES
    private ScheduledFuture<?> countDownFuture;
    private ScheduledFuture<?> foodSpawnFuture;
    private ScheduledFuture<?> crawlingFuture;

    public Application() {
        ExitHandler exitHandler = new ExitController();
        GameControllerImpl gameControllerImpl = new GameControllerImpl();
        ChangeLevelHandlerImpl changeLevelHandler = new ChangeLevelHandlerImpl(applicationContext);
        windowHolder = new WindowHolder(applicationContext, exitHandler, changeLevelHandler, gameControllerImpl);
    }

    public void start() {
        windowHolder.showMainWindow();
    }

    private final class GameControllerImpl implements GameController {

        @Override
        public void start() {
            gameOver = false;
            windowHolder.hideMainWindow();
            windowHolder.showGameWindow();
            windowHolder.updateGameFrame();
            startCountDown();
            startFruitSpawn();
            startAutomaticCrawling();
        }

        @Override
        public void moveSnake(Direction direction) {
            synchronized (applicationContext) {
                try {
                    if (!gameOver) {
                        applicationContext.getLevel().turnSnake(direction);
                        windowHolder.updateGameFrame();
                        turningPerformed = true;
                    }
                } catch (EndOfGameException e) {
                    endOfGameExceptionHandling();
                }
            }
        }

    }

    private final class ExitController implements ExitHandler {
        @Override
        public void exit(JFrame prayer) {
            if (prayer.getClass() == MainWindow.class) {
                System.exit(0);
            }
            gameOver = false;
            stopCountDown();
            stopFruitSpawn();
            stopAutomaticCrawling();
            if (prayer.getClass() == GameFrame.class || prayer.getClass() == EndOfGameFrame.class) {
                windowHolder.showMainWindow();
                windowHolder.hideGameWindow();
                windowHolder.hideEndOfGameWindow();
                applicationContext.restoreLastLevel();
            }
        }
    }

    private void startAutomaticCrawling() {
        Runnable crawling = () -> {
            synchronized (applicationContext) {
                try {
                    if (!turningPerformed) {
                        applicationContext.getLevel().moveInCurrentDirection();
                    } else {
                        turningPerformed = false;
                    }
                } catch (EndOfGameException e) {
                    endOfGameExceptionHandling();
                }
                windowHolder.updateGameFrame();
            }
        };
        crawlingFuture = applicationContext.getScheduledExecutorService()
                .scheduleAtFixedRate(crawling, 2,
                250, TimeUnit.MILLISECONDS);
    }

    private void startFruitSpawn() {
        Runnable fruitSpawn = () -> {
            applicationContext.getLevel().spawnFood();
            windowHolder.updateGameFrame();
        };
        foodSpawnFuture = applicationContext.getScheduledExecutorService()
                .scheduleAtFixedRate(fruitSpawn, 2,
                applicationContext.getLevel().getSpawnFrequency(), TimeUnit.SECONDS);
    }

    private void startCountDown() {
        Runnable countdown = () -> {
            try {
                applicationContext.getLevel().decrementPlayTime(1);
            } catch (TimeIsUpException e) {
                endOfGameExceptionHandling();
            }
            windowHolder.updateGameFrame();
        };
        countDownFuture = applicationContext.getScheduledExecutorService()
                .scheduleAtFixedRate(countdown, 2, 1, TimeUnit.SECONDS);
    }

    private void stopFruitSpawn() {
        if (foodSpawnFuture != null) {
            foodSpawnFuture.cancel(false);
        }
    }

    private void stopCountDown() {
        if (countDownFuture != null) {
            countDownFuture.cancel(false);
        }
    }

    private void stopAutomaticCrawling() {
        if (crawlingFuture != null) {
            crawlingFuture.cancel(false);
        }
    }

    private void endOfGameExceptionHandling() {
        gameOver = true;
        windowHolder.updateGameFrame();
        stopCountDown();
        stopFruitSpawn();
        stopAutomaticCrawling();
        windowHolder.showEndOfGameWindow();
    }
}
