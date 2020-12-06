package enums;

import java.util.Objects;

public enum LevelID {
    ONE("Level 1"), TWO("Level 2");

    private final String id;

    LevelID(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static LevelID of(String levelID){
        for(LevelID id: LevelID.values()){
            if(Objects.equals(id.getId(), levelID)){
                return id;
            }
        }
        throw new IllegalArgumentException("Unknown level id: " + levelID);
    }
}
