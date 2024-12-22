package com.esenyurt.enums;

public enum Hours {


    Nine(9,"9:00:00"),
    Ten(10,"10:00:00"),
    Eleven(11,"11:00:00"),
    Twelve(12,"12:00:00"),
    Thirteen(13,"13:00:00"),
    Fourteen(14,"14:00:00"),
    Fifteen(15,"15:00:00"),
    Sixteen(16,"16:00:00"),
    Seventeen(17,"17:00:00"),
    Eighteen(18,"18:00:00");
    public int value;
    public String label;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    Hours(int value, String label) {
        this.value = value;
        this.label = label;
    }
}
