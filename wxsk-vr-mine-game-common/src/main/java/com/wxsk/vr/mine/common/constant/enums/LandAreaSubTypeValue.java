package com.wxsk.vr.mine.common.constant.enums;

public enum LandAreaSubTypeValue {

    /**
     * 土地地块
     * */
    SOIL_LAND_AREA(1),

    /**
     * 火山
     * */
    VOLCANO_BLOCK(101),

    /**
     * 水洼
     * */
    SWAG_BLOCK(102),

    /**
     * 植物
     */
    PLANT_BLOCK(103),

    /**
     * 老鼠
     */
    RAT_BLOCK(104),

    /**
     * 宝箱
     * */
    MAGIC_BOX_BLOCK(105)
    ;

    private int value;

    LandAreaSubTypeValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LandAreaSubTypeValue{" +
                "value=" + value +
                '}';
    }
}
