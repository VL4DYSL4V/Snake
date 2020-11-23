package util.converter;

import builder.LevelBuilderImpl;
import director.Director;
import director.DirectorImpl;
import entity.FieldDimension;
import entity.Level;
import enums.LevelName;

import java.util.Objects;

public final class StringToLevelConverter {

    private StringToLevelConverter(){}

    public static Level of(String level){
        if (Objects.equals(level, LevelName.ONE.getName())){
            LevelBuilderImpl builder = new LevelBuilderImpl(new FieldDimension(20, 30));
            Director director = new DirectorImpl();
            director.constructFirstLevel(builder);
            return builder.build();
        }
        throw new IllegalArgumentException("Unknown level: " + level);
    }
}
