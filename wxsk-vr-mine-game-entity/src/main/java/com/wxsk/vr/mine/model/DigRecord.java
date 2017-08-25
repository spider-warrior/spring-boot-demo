package com.wxsk.vr.mine.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

/**
 * 挖掘记录
 */
@Document
public class DigRecord extends BaseModel {

    /**
     * 结束时间
     * */
    private long endTime;
    /**
     * 挖掘总耗时
     * */
    private long totalDigTime;

    /**
     * 是否通知过
     * */
    private boolean informed;

    /**
     * 涉及的地块页码
     * */
    private List<Integer> pageIndex;

    /**
     * 用户ID
     * */
    private Long userId;


    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getTotalDigTime() {
        return totalDigTime;
    }

    public void setTotalDigTime(long totalDigTime) {
        this.totalDigTime = totalDigTime;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
