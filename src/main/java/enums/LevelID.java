package enums;

public enum LevelID {
    ONE("Level 1"), TWO("Level 2");

    private final String name;

    LevelID(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
