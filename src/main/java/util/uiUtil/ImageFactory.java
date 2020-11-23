package util.uiUtil;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ImageFactory {

    private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();
    private static final Path grassBcg = Paths.get("C:\\Users\\владислав\\IdeaProjects\\Labs\\Snake\\picture\\grass.jpg");
    private static final Path logo = Paths.get("C:\\Users\\владислав\\IdeaProjects\\Labs\\Snake\\picture\\logo.png");
    private static final Path apple = Paths.get("C:\\Users\\владислав\\IdeaProjects\\Labs\\Snake\\picture\\apple.png");
    private static final Path pear = Paths.get("C:\\Users\\владислав\\IdeaProjects\\Labs\\Snake\\picture\\pear.png");
    private static final Path stone = Paths.get("C:\\Users\\владислав\\IdeaProjects\\Labs\\Snake\\picture\\stone.png");
    private static final Path wall = Paths.get("C:\\Users\\владислав\\IdeaProjects\\Labs\\Snake\\picture\\wall.png");
    private static final Path mushroom = Paths.get("C:\\Users\\владислав\\IdeaProjects\\Labs\\Snake\\picture\\mushroom.png");
    private static final Path scoreBonus = Paths.get("C:\\Users\\владислав\\IdeaProjects\\Labs\\Snake\\picture\\scoreBonus.png");

    private ImageFactory() {
    }

    public static Image getGrassImage() {
        return TOOLKIT.createImage(grassBcg.toString());
    }

    public static Image getLogo() {
        return TOOLKIT.createImage(logo.toString());
    }

    public static Image getApple() {
        return TOOLKIT.createImage(apple.toString());
    }

    public static Image getPear() {
        return TOOLKIT.createImage(pear.toString());
    }

    public static Image getWall() {
        return TOOLKIT.createImage(wall.toString());
    }

    public static Image getStone() {
        return TOOLKIT.createImage(stone.toString());
    }

    public static Image getMushroom() {
        return TOOLKIT.createImage(mushroom.toString());
    }

    public static Image getScoreBonus() {
        return TOOLKIT.createImage(scoreBonus.toString());
    }

}
