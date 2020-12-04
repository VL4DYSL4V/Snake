package dao;

import entity.Level;
import enums.LevelID;
import exception.CannotAccessLevelException;

public interface LevelDAO {

    Level getLevel(LevelID levelID) throws CannotAccessLevelException;

}