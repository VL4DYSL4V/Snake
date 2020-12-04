package application;

import context.ApplicationContext;
import enums.Direction;
import exception.EndOfGameException;
import exception.TimeIsUpException;
import handler.ExitHandler;
import handler.PlayHandler;
import ui.EndOfGameFrame;
import ui.GameFrame;
import ui.MainWindow;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

// TODO: 26.11.2020 Think of better thread interaction than just monopolizing ApplicationContext by one thread
public final class Application {

    // TODO: 26.11.2020  Create separate WindowHolder class and handlers
    private final JFrame mainWindow;
    private final GameFrame gameFrame;
    private final JFrame endOfGameFrame;

    private final ApplicationContext applicationContext = new ApplicationContext();

    private volatile boolean gameOver = false;

    // TODO: 26.11.2020 THIS MUST BE IN APPLICATION_CONTEXT
    private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(3);
    private ScheduledFuture<?> countDownFuture;
    private ScheduledFuture<?> foodSpawnFuture;
    private ScheduledFuture<?> crawlingFuture;

    public Application() {
        ExitHandler exitHandler = new ExitController();
        PlayController playController = new PlayController();
        mainWindow = new MainWindow(applicationContext, exitHandler, playController);
        gameFrame = new GameFrame(applicationContext, exitHandler, playController);
        endOfGameFrame = new EndOfGameFrame(exitHandler);
    }

    public void start() {
        SwingUtilities.invokeLater(() -> mainWindow.setVisible(true));
    }

    private final class PlayController implements PlayHandler {

        @Override
        public void start() {
                SwingUtilities.invokeLater(() -> {
                    mainWindow.setVisible(false);
                    gameFrame.setVisible(true);
                });
                gameOver = false;
                gameFrame.updateScores();
                gameFrame.updateTime();
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
                        gameFrame.updateScores();
                        gameFrame.repaint();
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
            stopCountDown();
            stopFruitSpawn();
            stopAutomaticCrawling();
            gameOver = false;
            if (prayer.getClass() == GameFrame.class) {
                SwingUtilities.invokeLater(() -> {
                    mainWindow.setVisible(true);
                    gameFrame.setVisible(false);
                    if (endOfGameFrame.isVisible()) {
                        endOfGameFrame.setVisible(false);
                    }
                });
                applicationContext.restoreLastLevel();
            } else if (prayer.getClass() == EndOfGameFrame.class) {
                SwingUtilities.invokeLater(() -> {
                    mainWindow.setVisible(true);
                    endOfGameFrame.setVisible(false);
                    gameFrame.setVisible(false);
                });
                applicationContext.restoreLastLevel();
            }
        }
    }

    private void startAutomaticCrawling() {
        class Crawler implements Runnable {

            boolean skippedOnce = false;

            @Override
            public void run() {
                synchronized (applicationContext) {
                    try {
//                        if (applicationContext.getLevel().turningPerformed() && !skippedOnce) {
//                            skippedOnce = true;
//                        } else {
//                            skippedOnce = false;
                            applicationContext.getLevel().moveInCurrentDirection();
//                        }
                    } catch (EndOfGameException e) {
                        endOfGameExceptionHandling();
                    }
                    gameFrame.updateScores();
                    gameFrame.repaint();
                }
            }
        }

        crawlingFuture = ses.scheduleAtFixedRate(new Crawler(), 2,
                250, TimeUnit.MILLISECONDS);
    }

    private void startFruitSpawn() {
        Runnable fruitSpawn = () -> {
            applicationContext.getLevel().spawnFood();
            gameFrame.repaint();
        };
        foodSpawnFuture = ses.scheduleAtFixedRate(fruitSpawn, 2,
                applicationContext.getLevel().getSpawnFrequency(), TimeUnit.SECONDS);
    }

    private void stopFruitSpawn() {
        if (foodSpawnFuture != null) {
            foodSpawnFuture.cancel(false);
        }
    }

    private void startCountDown() {
        Runnable countdown = () -> {
            try {
                applicationContext.getLevel().decrementPlayTime(1);
            } catch (TimeIsUpException e) {
                endOfGameExceptionHandling();
            }
            gameFrame.updateTime();
        };
        countDownFuture = ses.scheduleAtFixedRate(countdown, 2, 1, TimeUnit.SECONDS);
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
        gameFrame.updateScores();
        gameFrame.updateTime();
        gameFrame.repaint();
        stopCountDown();
        stopFruitSpawn();
        stopAutomaticCrawling();
        SwingUtilities.invokeLater(() -> endOfGameFrame.setVisible(true));
    }
}
