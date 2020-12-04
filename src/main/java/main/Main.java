package main;

import application.Application;

import javax.xml.bind.JAXBException;

public final class Main {

    public static void main(String[] args) throws JAXBException {
        Application application = new Application();
        application.start();
//        Level level = StringToLevelConverter.of(LevelID.TWO.getName());
//        JAXBContext jaxbContext = JAXBContext.newInstance(Level.class, Coordinates.class,
//                FieldObject.class, FieldDimension.class, Snake.class);
//        Marshaller marshaller = jaxbContext.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//        File f =  new File("level\\" + LevelID.TWO.getName() + ".xml");
//
//        marshaller.marshal(level, f);
//
//        level = (Level) jaxbContext.createUnmarshaller().unmarshal(f);
    }

}
