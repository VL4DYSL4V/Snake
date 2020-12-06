package ui;

import context.ApplicationContext;
import enums.UIColor;
import enums.event.UIEvent;
import event.GameEvent;
import observer.gameEvent.GameEventPublisher;
import observer.gameEvent.GameEventSubscriber;
import observer.uiEvent.UIEventPublisher;
import observer.uiEvent.UIEventSubscriber;
import util.uiUtil.ImageFactory;
import util.uiUtil.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public final class GameFrame extends JFrame implements UIEventPublisher, GameEventPublisher {

    private static final int WIDTH = 900;
    private static final int HEIGHT = 900;
    private static final Font FONT = new Font(Font.SERIF, Font.BOLD, HEIGHT / 25);

    private final JPanel rootPanel = new BackgroundPanel(ImageFactory.getGrassImage());
    private final JPanel controlPanel = new JPanel();
    private final JPanel gamePanel;

    private final JButton backButton = new AppButton("Back");

    private final JLabel scoreLabel = new JLabel();
    private final JLabel timeLabel = new JLabel();

    private final ApplicationContext applicationContext;

    private final java.util.List<UIEventSubscriber> UIEventSubscribers = new LinkedList<>();
    private final java.util.List<GameEventSubscriber> gameEventSubscribers = new LinkedList<>();
    private final Map<String, GameEvent> gameEventMap = new HashMap<>();

    public GameFrame(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.gamePanel = new GamePanel(applicationContext);
        frameTuning();
        configRootPanel();
        configControlPanel();
        configGamePanel();
        configBackButton();
        configScoreLabel();
        configTimeLabel();
        constructWindow();
        setupGameEventMap();
    }

    private void setupGameEventMap(){
        gameEventMap.put("leftPressed", GameEvent.leftPressed());
        gameEventMap.put("rightPressed", GameEvent.rightPressed());
        gameEventMap.put("upPressed", GameEvent.upPressed());
        gameEventMap.put("downPressed", GameEvent.downPressed());
    }

    @Override
    public void subscribe(UIEventSubscriber UIEventSubscriber) {
        UIEventSubscribers.add(UIEventSubscriber);
    }

    @Override
    public void unsubscribe(UIEventSubscriber UIEventSubscriber) {
        UIEventSubscribers.remove(UIEventSubscriber);
    }

    @Override
    public void notifySubscribers(UIEvent uiEvent) {
        UIEventSubscribers.forEach(subscriber ->subscriber.react(uiEvent));
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

    private final class MoveSnakeInvoker extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    notifySubscribers(gameEventMap.get("upPressed"));
                    break;
                case KeyEvent.VK_DOWN:
                    notifySubscribers(gameEventMap.get("downPressed"));
                    break;
                case KeyEvent.VK_LEFT:
                    notifySubscribers(gameEventMap.get("leftPressed"));
                    break;
                case KeyEvent.VK_RIGHT:
                    notifySubscribers(gameEventMap.get("rightPressed"));
                    break;
            }
        }
    }

    private void constructWindow() {
        add(rootPanel);
        rootPanel.add(controlPanel, BorderLayout.NORTH);
        rootPanel.add(gamePanel, BorderLayout.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5, 5, 5, 5);
        controlPanel.add(backButton, constraints);
        constraints.insets = new Insets(5, (int) (WIDTH / 4.5), 5, WIDTH / 6);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 1;
        controlPanel.add(timeLabel, constraints);
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 2;
        controlPanel.add(scoreLabel, constraints);
    }

    private void configTimeLabel() {
        timeLabel.setFont(new Font(Font.SERIF, Font.BOLD, HEIGHT / 18));
        timeLabel.setForeground(new Color(235, 0, 0));
        updateTime();
    }

    private void configScoreLabel() {
        scoreLabel.setFont(FONT);
        scoreLabel.setForeground(UIColor.FOREGROUND.getColor());
        scoreLabel.setBackground(UIColor.BACKGROUND.getColor());
        scoreLabel.setOpaque(true);
        updateScores();
    }

    private void configBackButton() {
        backButton.setFont(FONT);
        backButton.addActionListener(e -> notifySubscribers(UIEvent.RETURNING_TO_MENU));
    }

    private void configGamePanel() {
        gamePanel.setOpaque(false);
        gamePanel.setSize(WIDTH, HEIGHT - 50);
    }

    private void configControlPanel() {
        controlPanel.setBackground(new Color(110, 110, 110));
        controlPanel.setForeground(UIColor.FOREGROUND.getColor());
        controlPanel.setLayout(new GridBagLayout());
        controlPanel.setOpaque(false);
        controlPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    private void configRootPanel() {
        rootPanel.setSize(WIDTH, HEIGHT);
        rootPanel.setBackground(UIColor.BACKGROUND.getColor());
        rootPanel.setForeground(UIColor.FOREGROUND.getColor());
        rootPanel.setLayout(new BorderLayout());
    }

    private void frameTuning() {
        setTitle("*** Snake ***");
        setResizable(false);
        setLocation((UIUtils.getScreenWidth() - WIDTH) / 2, (UIUtils.getScreenHeight() - HEIGHT) / 2);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setFocusable(true);
        addKeyListener(new MoveSnakeInvoker());
    }

    public void updateScores() {
        scoreLabel.setText(" Scores: " + applicationContext.getLevel().getScores() +
                "/" + applicationContext.getLevel().getScoresThreshold() + " ");
    }

    public void updateTime() {
        LocalTime time = applicationContext.getLevel().getPlayTime();
        int minute = time.getMinute();
        int second = time.getSecond();
        StringBuilder out = new StringBuilder();
        if (minute > 10) {
            out.append(minute);
        } else {
            out.append("0").append(minute);
        }
        out.append(":");
        if (second >= 10) {
            out.append(second);
        } else {
            out.append("0").append(second);
        }
        timeLabel.setText(out.toString());
    }

}
