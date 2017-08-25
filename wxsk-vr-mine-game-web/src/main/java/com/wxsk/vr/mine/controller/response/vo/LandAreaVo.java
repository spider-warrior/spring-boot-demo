package com.wxsk.vr.mine.controller.response.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class LandAreaVo {

    @JsonProperty(value = "name")
    private String name;

    /**
     * 地块大类
     * */
    @JsonProperty(value = "type")
    private Byte type;

    /**
     * 地块子类
     * */
    @JsonProperty("subType")
    private Integer subType;

    /**
     * index 矿区中的所在下标
     * */
    @JsonProperty("index")
    private Integer index;

    /**
     * 结束时间
     * */
    @JsonProperty("endTime")
    private Date endTime;

    /**
     * 石块状态
     * 1: 100 - 61 ---> 40%
     * 2: 60 - 31  ---> 70%
     * 3: 30 - 0   ---> 100%
     * */
    @JsonProperty("status")
    private Byte status;

    /**
     * 总消耗体力
     * */
    @JsonProperty("consumeEnergy")
    private Integer consumeEnergy;

    /**
     * 总消耗体力
     * */
    @JsonProperty("containedEnergy")
    private Integer containedEnergy;

    /**
     * 消耗时间
     * */
    @JsonProperty("consumeTime")
    private String consumeTime;

    /**
     * 消耗时间
     * */
    @JsonProperty("consumeTimeInSecond")
    private Integer consumeTimeInSecond;

    /**
     * 奖励名称
     * */
    @JsonProperty("awardName")
    private String awardName;

    /**
     * 奖励类型
     * */
    @JsonProperty("awardType")
    private Byte awardType;

    /**
     * 奖励数量
     * */
    @JsonProperty("awardAmount")
    private Double awardAmount;

    /**
     * 是否正在挖掘中
     * */
    @JsonProperty("ing")
    private boolean ing;

    /**
     * 通知状态
     * */
    @JsonProperty("informed")
    private boolean informed;


    public String getName() {
        return name;
    }

    public LandAreaVo setName(String name) {
        this.name = name;
        return this;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getConsumeEnergy() {
        return consumeEnergy;
    }

    public void setConsumeEnergy(Integer consumeEnergy) {
        this.consumeEnergy = consumeEnergy;
    }

    public Integer getContainedEnergy() {
        return containedEnergy;
    }

    public LandAreaVo setContainedEnergy(Integer containedEnergy) {
        this.containedEnergy = containedEnergy;
        return this;
    }

    public String getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(String consumeTime) {
        this.consumeTime = consumeTime;
    }

    public Integer getConsumeTimeInSecond() {
        return consumeTimeInSecond;
    }

    public void setConsumeTimeInSecond(Integer consumeTimeInSecond) {
        this.consumeTimeInSecond = consumeTimeInSecond;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public Byte getAwardType() {
        return awardType;
    }

    public void setAwardType(Byte awardType) {
        this.awardType = awardType;
    }

    public Double getAwardAmount() {
        return awardAmount;
    }

    public void setAwardAmount(Double awardAmount) {
        this.awardAmount = awardAmount;
    }

    public boolean isIng() {
        return ing;
    }

    public void setIng(boolean ing) {
        this.ing = ing;
    }

    public boolean isInformed() {
        return informed;
    }

    public void setInformed(boolean informed) {
        this.informed = informed;
    }
}
