package ui;

import context.ApplicationContext;
import enums.LevelID;
import enums.UIColor;
import handler.ChangeLevelHandler;
import handler.ExitHandler;
import controller.GameController;
import util.uiUtil.ImageFactory;
import util.uiUtil.UIUtils;

import javax.swing.*;
import java.awt.*;

public final class MainWindow extends JFrame {

    private static final int WIDTH = 480;
    private static final int HEIGHT = 320;
    private static final Font FONT = new Font(Font.SERIF, Font.BOLD, HEIGHT / 11);

    private final JPanel rootPanel = new BackgroundPanel(ImageFactory.getGrassImage());
    private final JPanel leftPanel = new JPanel();
    private final JPanel rightPanel = new JPanel();

    private final JButton exitButton = new AppButton("Exit");
    private final JButton playButton = new AppButton("Play");
    private final JButton settingsButton = new AppButton("Settings");

    private final JComboBox<String> levelComboBox;

    private final JLabel logoHolder = new JLabel();

    private final ExitHandler exitHandler;
    private final GameController gameController;
//    private final ApplicationContext applicationContext;
    private final ChangeLevelHandler changeLevelHandler;

    public MainWindow(ApplicationContext applicationContext, ExitHandler exitHandler,
                      GameController gameController, ChangeLevelHandler changeLevelHandler) {
//        this.applicationContext = applicationContext;
        this.levelComboBox = new JComboBox<>();
        this.exitHandler = exitHandler;
        this.gameController = gameController;
        this.changeLevelHandler = changeLevelHandler;
        frameTuning();
        configRootPanel();
        configLeftPanel();
        configRightPanel();
        configExitButton();
        configPlayButton();
        configSettingsButton();
        configLevelComboBox();
        configLogoHolder();
        constructWindow();
    }

    private void constructWindow() {
        add(rootPanel);
        constructLeftPanel();
        constructRightPanel();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        rootPanel.add(logoHolder, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        rootPanel.add(leftPanel, constraints);
        constraints.gridx = 2;
        rootPanel.add(rightPanel, constraints);
    }

    private void constructRightPanel() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        rightPanel.add(settingsButton, constraints);
        constraints.gridy = 1;
        rightPanel.add(exitButton, constraints);
    }

    private void constructLeftPanel() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.LINE_START;
        leftPanel.add(playButton, constraints);
        constraints.gridy = 1;
        leftPanel.add(levelComboBox, constraints);
    }

    @SuppressWarnings("unchecked")
    private void configLevelComboBox() {
        for (LevelID levelID : LevelID.values()) {
            levelComboBox.addItem(levelID.getId());
        }
        levelComboBox.setBackground(UIColor.BACKGROUND.getColor());
        levelComboBox.setForeground(UIColor.FOREGROUND.getColor());
        levelComboBox.setFont(FONT);
        levelComboBox.addActionListener(e -> {
            Object source = e.getSource();
            if (source.equals(levelComboBox)) {
                JComboBox<String> box = (JComboBox<String>) source;
                String item = (String) box.getSelectedItem();
                changeLevelHandler.change(item);
            }
        });
    }

    private void configLogoHolder() {
        logoHolder.setIcon(new ImageIcon(ImageFactory.getLogo()
                .getScaledInstance((int) (WIDTH / 2.5), HEIGHT / 3, Image.SCALE_SMOOTH)));

    }

    private void configSettingsButton() {
        settingsButton.setFont(FONT);
    }

    private void configPlayButton() {
        playButton.addActionListener(e -> gameController.start());
        playButton.setFont(FONT);
    }

    private void configExitButton() {
        exitButton.addActionListener(e -> exitHandler.exit(this));
        exitButton.setFont(FONT);
    }

    private void configRightPanel() {
        rightPanel.setBackground(UIColor.BACKGROUND.getColor());
        rightPanel.setForeground(UIColor.FOREGROUND.getColor());
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    private void configLeftPanel() {
        leftPanel.setBackground(UIColor.BACKGROUND.getColor());
        leftPanel.setForeground(UIColor.FOREGROUND.getColor());
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    private void configRootPanel() {
        rootPanel.setSize(WIDTH, HEIGHT);
        rootPanel.setBackground(UIColor.BACKGROUND.getColor());
        rootPanel.setForeground(UIColor.FOREGROUND.getColor());
        rootPanel.setLayout(new GridBagLayout());
    }

    private void frameTuning() {
        setTitle("*** Snake ***");
        setResizable(false);
        setLocation((UIUtils.getScreenWidth() - WIDTH) / 2, (UIUtils.getScreenHeight() - HEIGHT) / 2);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setFocusable(true);
    }
}
