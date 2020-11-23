package enums;

public enum LevelName {
    ONE("Level 1");

    private final String name;

    LevelName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
