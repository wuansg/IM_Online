package xyz.silverspoon.utils;

public enum UUIDType {
    IM_USER(0), IM_MESSAGE(1), IM_RELATION(2), IM_REQUEST(3), IM_NOTIFICATION(4);

    private int type;

    private UUIDType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
