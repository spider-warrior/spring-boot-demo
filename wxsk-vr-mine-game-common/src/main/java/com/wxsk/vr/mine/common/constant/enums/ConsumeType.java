package com.wxsk.vr.mine.common.constant.enums;


public enum ConsumeType {
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
    VR_COIN((byte) 3, "无限币")
    ;

    public static ConsumeType getConsumeType(byte value) {
        for (ConsumeType type: values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }

    public final byte value;

    public final String name;

    ConsumeType(byte value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ConsumeType{" +
                "value=" + value +
                ", name='" + name + '\'' +
                '}';
    }
}
