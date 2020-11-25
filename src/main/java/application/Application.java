package application;

import context.ApplicationContext;
import enums.Direction;
import exception.EndOfGameException;
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

public final class Application {

    private final JFrame mainWindow;
    private final GameFrame gameFrame;
    private final JFrame endOfGameFrame;

    private final ApplicationContext applicationContext = new ApplicationContext();

    private volatile boolean gameOver = false;

    private final ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);
    private ScheduledFuture<?> countDownFuture;
    private ScheduledFuture<?> foodSpawnFuture;

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
        }

        @Override
        public void moveSnake(Direction direction) {
            try{
                if(!gameOver) {
                    applicationContext.getLevel().moveSnake(direction);
                    gameFrame.updateScores();
                    gameFrame.repaint();
                }
            }catch (EndOfGameException e){
                gameOver = true;
                stopCountDown();
                stopFruitSpawn();
                SwingUtilities.invokeLater(() -> endOfGameFrame.setVisible(true));
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
            gameOver = false;
            if (prayer.getClass() == GameFrame.class) {
                SwingUtilities.invokeLater(() -> {
                    mainWindow.setVisible(true);
                    gameFrame.setVisible(false);
                    if(endOfGameFrame.isVisible()){
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
            } catch (EndOfGameException e) {
                gameOver = true;
                stopCountDown();
                stopFruitSpawn();
                SwingUtilities.invokeLater(() -> endOfGameFrame.setVisible(true));
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

}
