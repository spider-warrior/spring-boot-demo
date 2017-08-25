package com.wxsk.vr.mine.common.constant.enums;

/**
 * 奖励枚举
 * 未知(0), 金币(1), 钻石(2), 无限币(3)
 */
public enum LandAwardType {

    /**
     * 未知(0)
     * */
    UNKNOWN((byte)0, "未知"),
    /**
     * 金币(1)
     * */
    GOLD_COIN((byte)1, "金币"),
    /**
     * 钻石(2)
     * */
    DIAMOND((byte)2, "钻石"),
    /**
     * 无限币(3)
     * */
    VR_COIN((byte)3, "无限币");

    public static LandAwardType getLandAwardType(byte value) {
        for (LandAwardType type: values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }

    LandAwardType(byte value, String name) {
        this.value = value;
        this.name = name;
    }

    public byte value;

    public String name;

    @Override
    public String toString() {
        return "LandAwardType{" +
                "value=" + value +
                ", name='" + name + '\'' +
                '}';
    }
}
