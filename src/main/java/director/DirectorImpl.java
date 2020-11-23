package director;

import builder.LevelBuilder;
import entity.Coordinates;
import entity.FieldDimension;
import entity.FieldObject;

import java.util.LinkedList;
import java.util.List;

public final class DirectorImpl implements Director {

    @Override
    public void constructFirstLevel(LevelBuilder builder) {
        List<FieldObject> walls = new LinkedList<>();
        FieldDimension dimension = builder.getFieldDimension();
        for(int x = 0; x < dimension.getWidth(); x++){
            walls.add(FieldObject.wall(new Coordinates(x, 0)));
            walls.add(FieldObject.wall(new Coordinates(x, dimension.getMaxY())));
        }
        for(int y = 1; y < dimension.getMaxY(); y++){
            walls.add(FieldObject.wall(new Coordinates(0, y)));
            walls.add(FieldObject.wall(new Coordinates(dimension.getMaxX(), y)));
        }
        builder.setWalls(walls);
    }
}
