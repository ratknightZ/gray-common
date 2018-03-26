package com.arms.common.enums;

public enum PlatformType {
    //PC端
    PC(1),
    //手机端
    MOBILE(2),
    //无线端
    APP(3);

    private int value;

    private PlatformType(int value) {
        this.setValue(value);
    }

    public static PlatformType getServerType(int value) {
        for (PlatformType serverType : PlatformType.values()) {
            if (serverType.getValue() == value) {
                return serverType;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
