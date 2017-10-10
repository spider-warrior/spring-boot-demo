package com.wxsk.vr.mine.common.constant.enums;

/**
 * 奖励枚举
 * 未知(0), 金币(1), 钻石(2), 无限币(3), 经验(4), 体力(5)
 */
public enum LandAreaAwardTypeValue {

    /**
     * 未知(0)
     */
    UNKNOWN((byte) 0, "未知"),
    /**
     * 金币(1)
     */
    GOLD_COIN((byte) 1, "金币"),
    /**
     * 钻石(2)
     */
    DIAMOND((byte) 2, "钻石"),
    /**
     * 无限币(3)
     */
    VR_COIN((byte) 3, "无限币"),

    /**
     * 经验(4)
     * */
    EXP((byte)4, "经验"),

    /**
     * 经验(5)
     * */
    ENERGY((byte)5, "体力"),

    /**
     * 宝箱
     * */
    MAGIC_BOX((byte)6, "宝箱");

    public static LandAreaAwardTypeValue getLandAreaAwardTypeValue(byte value) {
        for (LandAreaAwardTypeValue type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return UNKNOWN;
    }

    LandAreaAwardTypeValue(byte value, String name) {
        this.value = value;
        this.name = name;
    }

    public final byte value;

    public final String name;

}
