package com.wxsk.vr.mine.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 地块类型
 */
@Document
public class LandAreaType extends BaseModel implements Cloneable {

    /**
     * 大类
     */
    private byte type;

    /**
     * 小类
     */
    private int subType;

    /**
     * 地块名称
     */
    private String name;

    /**
     * 挖掘时间
     */
    private int spendTimeInSecond;

    /**
     * 消耗体力
     */
    private int consumeEnergy;

    /**
     * 经验
     * */
    private int exp;

    /**
     * 奖励类型
     */
    private AwardType awardType;


    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpendTimeInSecond() {
        return spendTimeInSecond;
    }

    public void setSpendTimeInSecond(int spendTimeInSecond) {
        this.spendTimeInSecond = spendTimeInSecond;
    }

    public int getConsumeEnergy() {
        return consumeEnergy;
    }

    public void setConsumeEnergy(int consumeEnergy) {
        this.consumeEnergy = consumeEnergy;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public AwardType getAwardType() {
        return awardType;
    }

    public void setAwardType(AwardType awardType) {
        this.awardType = awardType;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "LandAreaType{" +
            "type=" + type +
            ", subType=" + subType +
            ", name='" + name + '\'' +
            ", spendTimeInSecond=" + spendTimeInSecond +
            ", consumeEnergy=" + consumeEnergy +
            ", awardType=" + awardType +
            '}';
    }
}
