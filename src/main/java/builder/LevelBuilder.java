package builder;

import entity.Coordinates;
import entity.FieldDimension;
import entity.FieldObject;
import entity.Snake;
import enums.LevelID;

import java.time.LocalTime;
import java.util.List;

public interface LevelBuilder {

    LevelBuilder setFieldDimension(FieldDimension fieldDimension);

    LevelBuilder setApples(List<FieldObject> apples);

    LevelBuilder setPears(List<FieldObject> pears);

    LevelBuilder setMushrooms(List<FieldObject> mushrooms);

    LevelBuilder setScoreBonus(List<FieldObject> scoreBonuses);

    LevelBuilder setStones(List<FieldObject> stones);

    LevelBuilder setWalls(List<FieldObject> walls);

    LevelBuilder setScoresThreshold(long scoresThreshold);

    LevelBuilder setPlayTime(LocalTime playTime);

    LevelBuilder setLevelID(LevelID levelID);

    LevelBuilder setSpawnCoordinates(List<Coordinates> spawnCoordinates);

    LevelBuilder setSpawnFrequency(int spawnFrequency);

    LevelBuilder setSnake(Snake snake);
}
