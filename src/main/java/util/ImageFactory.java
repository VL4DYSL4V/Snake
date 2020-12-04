package util;

import enums.MapObject;

import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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
    private static final Path snakeHead = Paths.get("C:\\Users\\владислав\\IdeaProjects\\Labs\\Snake\\picture\\snakeHead.png");
    private static final Path snakeBody = Paths.get("C:\\Users\\владислав\\IdeaProjects\\Labs\\Snake\\picture\\snakeBody.png");

    private static final Map<MapObject, Image> fieldObjectImageCache = new HashMap<>();
    private static final Map<String, Image> uiElementCache = new HashMap<>();

    static {
        initializeFieldObjectImageCache();
        initializeUIElementCache();
    }

    private ImageFactory() {
    }

    private static void initializeFieldObjectImageCache(){
        fieldObjectImageCache.put(MapObject.APPLE, TOOLKIT.createImage(apple.toString()));
        fieldObjectImageCache.put(MapObject.PEAR, TOOLKIT.createImage(pear.toString()));
        fieldObjectImageCache.put(MapObject.WALL, TOOLKIT.createImage(wall.toString()));
        fieldObjectImageCache.put(MapObject.STONE, TOOLKIT.createImage(stone.toString()));
        fieldObjectImageCache.put(MapObject.MUSHROOM, TOOLKIT.createImage(mushroom.toString()));
        fieldObjectImageCache.put(MapObject.SCORE_BONUS, TOOLKIT.createImage(scoreBonus.toString()));
        fieldObjectImageCache.put(MapObject.SNAKE_HEAD, TOOLKIT.createImage(snakeHead.toString()));
        fieldObjectImageCache.put(MapObject.SNAKE_BODY, TOOLKIT.createImage(snakeBody.toString()));
    }

    private static void initializeUIElementCache(){
        uiElementCache.put("background", TOOLKIT.createImage(grassBcg.toString()));
        uiElementCache.put("logo", TOOLKIT.createImage(logo.toString()));
    }

    public static Image getFieldObjectImage(MapObject mapObject){
        return fieldObjectImageCache.get(mapObject);
    }

    public static Image getGrassImage(){
        return uiElementCache.get("background");
    }

    public static Image getLogo(){
        return uiElementCache.get("logo");
    }

}
