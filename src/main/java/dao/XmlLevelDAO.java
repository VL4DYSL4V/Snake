package dao;

import entity.*;
import enums.LevelID;
import exception.CannotAccessLevelException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;

public class XmlLevelDAO implements LevelDAO{

    @Override
    public Level getLevel(LevelID levelID) throws CannotAccessLevelException {
        File f = new File("level\\" + levelID.getId() + ".xml");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Level.class);
            return (Level) jaxbContext.createUnmarshaller().unmarshal(f);
        } catch (JAXBException e) {
            CannotAccessLevelException exception = new CannotAccessLevelException();
            exception.initCause(e);
            throw exception;
        }
    }

}
