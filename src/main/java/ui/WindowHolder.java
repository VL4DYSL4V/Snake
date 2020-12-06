package ui;

import javax.swing.*;

public class WindowHolder {

    private final GameFrame gameFrame;
    private final EndOfGameFrame endOfGameFrame;
    private final MainWindow mainWindow;

    public WindowHolder(GameFrame gameFrame, EndOfGameFrame endOfGameFrame, MainWindow mainWindow) {
        this.gameFrame = gameFrame;
        this.endOfGameFrame = endOfGameFrame;
        this.mainWindow = mainWindow;
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
