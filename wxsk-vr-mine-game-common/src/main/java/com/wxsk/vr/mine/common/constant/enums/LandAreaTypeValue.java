package com.wxsk.vr.mine.common.constant.enums;

import java.util.List;

/**
 * 地块类型
 * 地块(0), 障碍(1), 金币(2), 钻石(3), 无限币(4)
 */
public enum LandAreaTypeValue {

    /**
     * LAND_AREA
     */
    LAND_AREA((byte) 0),

    /**
     * 障碍
     */
    Block((byte) 1),

    /**
     * 金币
     */
    GOLD_COIN((byte) 2),

    /**
     * 钻石
     */
    DIAMOND((byte) 3),

    /**
     * 无限币
     */
    VR_COIN((byte) 4);

    public static LandAreaTypeValue getLandAreaTypeValue(byte value) {
        for (LandAreaTypeValue landAreaTypeValue : values()) {
            if (landAreaTypeValue.value == value) {
                return landAreaTypeValue;
            }
        }
        return null;
    }

    private byte value;

    private List<LandAreaTypeValue> subTypeList;

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    LandAreaTypeValue(byte value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LandAreaTypeValue{" +
                "value=" + value +
                '}';
    }
}
