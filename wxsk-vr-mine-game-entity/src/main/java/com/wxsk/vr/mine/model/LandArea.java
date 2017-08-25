package com.wxsk.vr.mine.model;

import com.wxsk.vr.mine.common.util.DateUtil;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 地块
 */
@Document
public class LandArea extends BaseModel {

    /**
     * 地块类型
     * */
    private LandAreaType landAreaType;

    /**
     * 一页矿区中所排列的下标位置
     * */
    private int index;

    /**
     * 挖掘开始时间
     * */
    private long startTime;

    /**
     * 挖掘结束时间
     * */
    private long endTime;

    /**
     * 已消耗体力
     * */
    private int containEnergy;

    /**
     * 收益通知
     * */
    private boolean informed;

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

    public boolean isInformed() {
        return informed;
    }

    public void setInformed(boolean informed) {
        this.informed = informed;
    }
    
    /**
     * 计算地块开始时间
     * @param landArea
     * @return
     */
    public static long reckonStartTime(LandArea landArea){
    	if(landArea == null){
    		return 0L;
    	}
    	if(landArea.landAreaType == null){
    		return 0L;
    	}
    	return landArea.getEndTime() - landArea.landAreaType.getSpendTimeInSecond()*60*1000;
    }

    @Override
    public String toString() {
        return "LandArea{" +
                "\nlandAreaType=" + landAreaType +
                "\n, index=" + index +
                "\n, startTime=" + DateUtil.yyyyMMddHHmmssFormat(new Date(startTime)) +
                "\n, endTime=" + DateUtil.yyyyMMddHHmmssFormat(new Date(endTime)) +
                "\n, containEnergy=" + containEnergy +
                "\n, informed=" + informed +
                "\n}";
    }
}
