package ui;


import enums.UIColor;

import javax.swing.*;

final class AppButton extends JButton {

    AppButton(String text) {
        setText(text);
        setBackground(UIColor.BACKGROUND.getColor());
        setForeground(UIColor.FOREGROUND.getColor());
        setFocusable(false);
    }

}
