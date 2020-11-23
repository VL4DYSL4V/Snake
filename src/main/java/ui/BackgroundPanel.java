package ui;

import javax.swing.*;
import java.awt.*;

final class BackgroundPanel extends JPanel {

    private final Image backGroundImage;

    public BackgroundPanel(Image backGroundImage) {
        this.backGroundImage = backGroundImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backGroundImage, 0,0, getParent().getWidth(), getParent().getHeight(),this);
    }
}
