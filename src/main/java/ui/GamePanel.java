package ui;

import context.ApplicationContext;
import entity.FieldDimension;
import entity.FieldObject;

import javax.swing.*;
import java.awt.*;
import java.util.List;

final class GamePanel extends JPanel {

    private final ApplicationContext applicationContext;

    public GamePanel(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        List<FieldObject> objects = applicationContext.getLevel().getFieldObjects();
        FieldDimension dimension = applicationContext.getLevel().getFieldDimension();
        int componentWidth = getWidth() / dimension.getWidth() + 1;
        int componentHeight = getHeight() / dimension.getHeight();
        for (FieldObject object : objects) {
            g.drawImage(object.getImage(), object.getCoordinates().getX() * componentWidth,
                    object.getCoordinates().getY() * componentHeight, componentWidth, componentHeight, this);
        }
    }
}
