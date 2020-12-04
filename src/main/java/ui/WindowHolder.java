package ui;

import context.ApplicationContext;
import handler.ChangeLevelHandler;
import handler.ExitHandler;
import controller.GameController;

import javax.swing.*;

public class WindowHolder {

    private final GameFrame gameFrame;
    private final EndOfGameFrame endOfGameFrame;
    private final MainWindow mainWindow;

    public WindowHolder(ApplicationContext applicationContext, ExitHandler exitHandler,
                        ChangeLevelHandler changeLevelHandler, GameController gameController) {
        gameFrame = new GameFrame(applicationContext, exitHandler, gameController);
        endOfGameFrame = new EndOfGameFrame(exitHandler);
        mainWindow = new MainWindow(applicationContext, exitHandler, gameController, changeLevelHandler);
    }

    public void showMainWindow() {
        SwingUtilities.invokeLater(() -> mainWindow.setVisible(true));
    }

    public void hideMainWindow() {
        SwingUtilities.invokeLater(() -> mainWindow.setVisible(false));
    }

    public void showGameWindow() {
        SwingUtilities.invokeLater(() -> gameFrame.setVisible(true));
    }

    public void hideGameWindow() {
        SwingUtilities.invokeLater(() -> gameFrame.setVisible(false));
    }

    public void showEndOfGameWindow() {
        SwingUtilities.invokeLater(() -> endOfGameFrame.setVisible(true));
    }

    public void hideEndOfGameWindow() {
        SwingUtilities.invokeLater(() -> endOfGameFrame.setVisible(false));
    }

    public void updateGameFrame() {
        SwingUtilities.invokeLater(() -> {
            gameFrame.updateScores();
            gameFrame.updateTime();
            gameFrame.repaint();
        });
    }
}
