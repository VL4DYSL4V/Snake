package application;

import command.*;
import context.ApplicationContext;
import controller.GameControllerImpl;
import eventHandler.GameEventHandler;
import eventHandler.UIEventHandler;
import ui.EndOfGameFrame;
import ui.GameFrame;
import ui.MainWindow;
import ui.WindowHolder;

public final class Application {

    private final WindowHolder windowHolder;

    public Application() {
        ApplicationContext applicationContext = new ApplicationContext();

        ChangeLevelCommand changeLevelCommand = new ChangeLevelCommandImpl(applicationContext);

        MainWindow mainWindow = new MainWindow(changeLevelCommand);
        EndOfGameFrame endOfGameFrame = new EndOfGameFrame();
        GameFrame gameFrame = new GameFrame(applicationContext);

        windowHolder = new WindowHolder(gameFrame, endOfGameFrame, mainWindow);

        GameControllerImpl gameController = new GameControllerImpl(applicationContext);

        UIEventHandler uiEventHandler = new UIEventHandler(windowHolder, gameController, applicationContext);
        mainWindow.subscribe(uiEventHandler);
        gameFrame.subscribe(uiEventHandler);
        endOfGameFrame.subscribe(uiEventHandler);

        GameEventHandler gameEventHandler = new GameEventHandler(windowHolder);
        gameController.subscribe(gameEventHandler);
    }

    public void start() {
        windowHolder.showMainWindow();
    }

}
