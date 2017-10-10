package com.wxsk.vr.mine.common.constant.enums;

public enum AffectType {

    /**
     * 体力消耗
     * */
    ENERGY((byte)1),

    /**
     * 时间消耗
     * */
    TIME((byte)2),

    /**
     * 收益
     * */
    HARVEST((byte)3)
    ;


    public static AffectType getAffectType(byte value) {
        for (AffectType type: values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }

    /**
     * 影响类型
     * */
    private byte value;

    AffectType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AffectType{" +
            "value=" + value +
            '}';
    }
}
