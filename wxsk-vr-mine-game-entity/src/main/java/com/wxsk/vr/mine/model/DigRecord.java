package com.wxsk.vr.mine.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 挖掘记录
 */
@Document
public class DigRecord extends BaseModel {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 结束时间
     */
    private long endTime;

    /**
     * 地块开始下标
     */
    private int landAreaStartIndex;

    /**
     * 地块结束下标
     */
    private int landAreaEndIndex;
    /**
     * 是否通知过
     */
    private boolean informed;

    /**
     * 涉及的地块页码
     */
    private List<Integer> pageIndex;
    /**
     * 未消费体力
     */
    private int remainEnergy;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public int getLandAreaStartIndex() {
        return landAreaStartIndex;
    }

    public void setLandAreaStartIndex(int landAreaStartIndex) {
        this.landAreaStartIndex = landAreaStartIndex;
    }

    public int getLandAreaEndIndex() {
        return landAreaEndIndex;
    }

    public void setLandAreaEndIndex(int landAreaEndIndex) {
        this.landAreaEndIndex = landAreaEndIndex;
    }

    public boolean isInformed() {
        return informed;
    }

    public void setInformed(boolean informed) {
        this.informed = informed;
    }

    public List<Integer> getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(List<Integer> pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getRemainEnergy() {
        return remainEnergy;
    }

    public void setRemainEnergy(int remainEnergy) {
        this.remainEnergy = remainEnergy;
    }

    public void addPageIndex(int index) {
        if (pageIndex == null) {
            pageIndex = new ArrayList<>();
        }
        if (!pageIndex.contains(index)) {
            pageIndex.add(index);
        }
    }

    @Override
    public String toString() {
        return "DigRecord{" +
                "userId=" + userId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", informed=" + informed +
                ", pageIndex=" + pageIndex +
                ", remainEnergy=" + remainEnergy +
                '}';
    }
}
