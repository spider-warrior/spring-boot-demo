package com.wxsk.vr.mine.common.constant.enums;

/**
 * 地块类型
 * */
public enum LandType {

    /**
     * 障碍
     * */
    Block((byte)1),

    /**
     * Nothing
     * */
    Nothing((byte)2),

    /**
     * 金币
     * */
    GOLD_COIN((byte)3),

    /**
     * 钻石
     * */
    DIAMOND((byte)4),

    /**
     * 无限币
     * */
    VR_COIN((byte)5);

    public static LandType getLandType(byte value) {
        for (LandType type: values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }

    private byte value;

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    LandType(byte value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LandAreaType{" +
                "value=" + value +
                '}';
    }
}
