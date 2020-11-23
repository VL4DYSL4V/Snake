package util.uiUtil;

import java.awt.*;

public final class UIUtils {

    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();

    private UIUtils(){}

    public static int getScreenWidth(){
        return toolkit.getScreenSize().width;
    }

    public static int getScreenHeight(){
        return toolkit.getScreenSize().height;
    }
}
