package util.converter;

import entity.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;

public final class StringToLevelConverter {

    private StringToLevelConverter() {
    }

    public static Level of(String level) {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Level.class, Coordinates.class,
                    FieldObject.class, FieldDimension.class, Snake.class);
            File f = new File("level\\" + level + ".xml");
            return (Level) jaxbContext.createUnmarshaller().unmarshal(f);
//            LevelConstructor levelConstructor = new LevelConstructorImpl();
//            if (Objects.equals(level, LevelID.ONE.getName())) {
//                return levelConstructor.constructFirstLevel();
//            } else if (Objects.equals(level, LevelID.TWO.getName())) {
//                return levelConstructor.constructSecondLevel();
//            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Unknown level: " + level);
    }
}
