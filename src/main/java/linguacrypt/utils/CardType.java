package linguacrypt.utils;

public enum CardType {
    BLUE(0), RED(1), BLACK(2), WHITE(3);

    private final int type;

    CardType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
