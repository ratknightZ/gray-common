package com.arms.common.enums;

public enum Punctuation {

    /** 逗号 */
    DOT(","),

    /** 分号 */
    SEMICOLON(";"),

    /** 百分号 */
    PERCENT("%"),

    /** 空格 */
    SPACE(" "),

    /** 冒号 */
    COLON(":"),

    /** 双引号 */
    DOUBLE_QUOTATION("\"");

    private String value;

    private Punctuation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
