package ui;

import enums.UIColor;
import enums.event.UIEvent;
import observer.uiEvent.UIEventPublisher;
import observer.uiEvent.UIEventSubscriber;
import util.uiUtil.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public final class EndOfGameFrame extends JFrame implements UIEventPublisher {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 170;
    private static final Font FONT = new Font(Font.SERIF, Font.BOLD, WIDTH / 6);

    private final JPanel rootPanel = new JPanel();
    private final JLabel messageHolder = new JLabel();
    private final JButton toMenuButton = new AppButton("To menu");

    private final java.util.List<UIEventSubscriber> UIEventSubscribers = new LinkedList<>();

    public EndOfGameFrame() {
        frameTuning();
        configRootPanel();
        configMessageHolder();
        configToMenuButton();
        constructWindow();
    }

    private void constructWindow(){
        add(rootPanel);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        rootPanel.add(messageHolder, constraints);
        constraints.gridy = 1;
        rootPanel.add(toMenuButton, constraints);
    }

    private void configToMenuButton(){
        toMenuButton.addActionListener(e -> notifySubscribers(UIEvent.RETURNING_TO_MENU));
        toMenuButton.setFont(FONT);
    }

    private void configMessageHolder(){
        messageHolder.setFont(FONT);
        messageHolder.setForeground(UIColor.FOREGROUND.getColor());
        messageHolder.setBackground(UIColor.BACKGROUND.getColor());
        messageHolder.setOpaque(true);
        messageHolder.setText("Game over!!!");
    }

    private void configRootPanel() {
        rootPanel.setSize(WIDTH, HEIGHT);
        rootPanel.setBackground(UIColor.BACKGROUND.getColor());
        rootPanel.setForeground(UIColor.FOREGROUND.getColor());
        rootPanel.setLayout(new GridBagLayout());
    }

    private void frameTuning() {
        setResizable(false);
        setLocation((UIUtils.getScreenWidth() - WIDTH) / 2, (UIUtils.getScreenHeight() - HEIGHT) / 2);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setFocusable(true);
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
        UIEventSubscribers.forEach(subscriber -> subscriber.react(uiEvent));
    }

}
