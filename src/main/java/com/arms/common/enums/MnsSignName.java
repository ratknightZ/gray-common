package com.arms.common.enums;

public enum MnsSignName {
    Taotie("Taotie金融");

    private String value;

    private MnsSignName(String value) {
        this.setValue(value);
    }

    public static MnsSignName getMnsSignName(String value) {
        for (MnsSignName mnsSignName : MnsSignName.values()) {
            if (mnsSignName.getValue() == value) {
                return mnsSignName;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
