package util.converter;

import levelConstructor.LevelConstructor;
import levelConstructor.LevelConstructorImpl;
import entity.Level;
import enums.LevelID;

import java.util.Objects;

public final class StringToLevelConverter {

    private StringToLevelConverter(){}

    public static Level of(String level){
        LevelConstructor levelConstructor = new LevelConstructorImpl();
        if (Objects.equals(level, LevelID.ONE.getName())){
            return levelConstructor.constructFirstLevel();
        }else if (Objects.equals(level, LevelID.TWO.getName())){
            return levelConstructor.constructSecondLevel();
        }
        throw new IllegalArgumentException("Unknown level: " + level);
    }
}
