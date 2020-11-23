package enums;

import java.awt.*;

public enum UIColor {

    BACKGROUND(new Color(70,70,70)),
    FOREGROUND(new Color(230,230,190));

    private final Color color;

    UIColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
