package com.wxsk.vr.mine.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 地块
 */
@Document
public class LandArea extends BaseModel {

    /**
     * 地块类型
     */
    private LandAreaType landAreaType;

    /**
     * 一页矿区中所排列的下标位置
     */
    private int index;

    /**
     * 挖掘开始时间
     */
    private long startTime;

    /**
     * 挖掘结束时间
     */
    private long endTime;

    /**
     * 已消耗体力
     */
    private int containEnergy;

    /**
     * 实际收益数量
     * */
    private int actualProfitAmount;

    public LandAreaType getLandAreaType() {
        return landAreaType;
    }

    public void setLandAreaType(LandAreaType landAreaType) {
        this.landAreaType = landAreaType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getContainEnergy() {
        return containEnergy;
    }

    public void setContainEnergy(int containEnergy) {
        this.containEnergy = containEnergy;
    }

    public int getActualProfitAmount() {
        return actualProfitAmount;
    }

    public void setActualProfitAmount(int actualProfitAmount) {
        this.actualProfitAmount = actualProfitAmount;
    }

    @Override
    public String toString() {
        return "LandArea{" +
                "landAreaType=" + landAreaType +
                ", index=" + index +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", containEnergy=" + containEnergy +
                ", actualProfitAmount=" + actualProfitAmount +
                '}';
    }
}
