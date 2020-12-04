package main;

import application.Application;
import entity.Level;
import enums.LevelID;
import levelConstructor.LevelConstructor;
import levelConstructor.LevelConstructorImpl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public final class Main {

    public static void main(String[] args) throws JAXBException {
        Application application = new Application();
        application.start();
//        LevelConstructor levelConstructor = new LevelConstructorImpl();
//        Level one = levelConstructor.constructFirstLevel();
//        Level two = levelConstructor.constructSecondLevel();
//        JAXBContext jaxbContext = JAXBContext.newInstance(Level.class);
//        Marshaller marshaller = jaxbContext.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//        File f1 = new File("level\\" + LevelID.ONE.getId() + ".xml");
//
//        marshaller.marshal(one, f1);
//        File f2 = new File("level\\" + LevelID.TWO.getId() + ".xml");
//        marshaller.marshal(two, f2);
//        Level level1 = (Level) jaxbContext.createUnmarshaller().unmarshal(f1);
//        Level level2 = (Level) jaxbContext.createUnmarshaller().unmarshal(f2);
    }

}
