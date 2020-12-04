package enums;

public enum LevelID {
    ONE("Level 1"), TWO("Level 2");

    private final String id;

    LevelID(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
