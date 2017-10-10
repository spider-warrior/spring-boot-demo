package com.wxsk.vr.mine.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 奖励类型
 */
@Document
public class AwardType extends BaseModel implements Cloneable {

    /**
     * 奖励类型值
     */
    private byte value;

    /**
     * 奖励名称
     */
    private String name;

    /**
     * 奖励数量
     */
    private int amount;

    /**
     * 级别
     */
    private byte level;


    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "AwardType{" +
            "value=" + value +
            ", name='" + name + '\'' +
            ", amount=" + amount +
            ", level=" + level +
            '}';
    }
}
