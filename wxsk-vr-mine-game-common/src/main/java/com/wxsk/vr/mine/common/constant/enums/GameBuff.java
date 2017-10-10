package com.wxsk.vr.mine.common.constant.enums;

/**
 * buff
 * */
public enum GameBuff {

    /**
     * 火山buff
     * */
    VOLCANO_BUFF(0.5, LandAreaSubTypeValue.VOLCANO_BLOCK.getValue(), AffectType.TIME),

    /**
     * 水潭buff
     * */
    SWAG_BUFF(2, LandAreaSubTypeValue.SWAG_BLOCK.getValue(), AffectType.TIME),

    /**
     * 藤蔓buff
     * */
    PLANT_BUFF(2, LandAreaSubTypeValue.PLANT_BLOCK.getValue(), AffectType.ENERGY),

    /**
     * 老鼠buff
     * */
    RAT_BUFF(0.5, LandAreaSubTypeValue.RAT_BLOCK.getValue(), AffectType.HARVEST)
    ;

    public static GameBuff getGameBuff(int type) {
        for (GameBuff buff: values()) {
            if (buff.type == type) {
                return buff;
            }
        }
        return null;
    }

    private double coefficient;

    /**
     * type
     * */
    protected int type;

    /**
     * 影响方面
     * */
    protected AffectType affectType;

    GameBuff(double coefficient, int type, AffectType affectType) {
        this.coefficient = coefficient;
        this.type = type;
        this.affectType = affectType;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public AffectType getAffectType() {
        return affectType;
    }

    public void setAffectType(AffectType affectType) {
        this.affectType = affectType;
    }
}
