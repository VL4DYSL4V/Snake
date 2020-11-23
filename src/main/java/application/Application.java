package application;

import context.ApplicationContext;
import handler.ExitHandler;
import handler.MoveForwardHandler;
import ui.GameFrame;
import ui.MainWindow;

import javax.swing.*;

public final class Application {

    private final JFrame mainWindow;
    private final JFrame gameFrame;

    private final ApplicationContext applicationContext = new ApplicationContext();

    public Application(){
        mainWindow = new MainWindow(applicationContext, new ExitController(), new MoveForwardController());
        gameFrame = new GameFrame(new ExitController(), applicationContext);
    }

    public void start(){
        SwingUtilities.invokeLater(() -> mainWindow.setVisible(true));
    }

    private final class MoveForwardController implements MoveForwardHandler {
        @Override
        public void play() {
            mainWindow.setVisible(false);
            gameFrame.setVisible(true);
        }
    }

    private final class ExitController implements ExitHandler{
        @Override
        public void exit(JFrame prayer) {
            if(prayer.getClass() == MainWindow.class) {
                System.exit(0);
            }else if(prayer.getClass() == GameFrame.class){
                gameFrame.setVisible(false);
                mainWindow.setVisible(true);
            }
        }
    }


}
