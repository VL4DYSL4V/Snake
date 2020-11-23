package builder;

import entity.FieldDimension;
import entity.FieldObject;

import java.util.List;

public interface LevelBuilder {

    LevelBuilder setFieldDimension(FieldDimension fieldDimension);

    LevelBuilder setApples(List<FieldObject> apples);

    LevelBuilder setPears(List<FieldObject> pears);

    LevelBuilder setMushrooms(List<FieldObject> mushrooms);

    LevelBuilder setScoreBonus(List<FieldObject> scoreBonuses);

    LevelBuilder setStones(List<FieldObject> stones);

    LevelBuilder setWalls(List<FieldObject> walls);

    FieldDimension getFieldDimension();
}
