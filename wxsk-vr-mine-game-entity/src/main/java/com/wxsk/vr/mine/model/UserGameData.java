package com.wxsk.vr.mine.model;

import com.wxsk.vr.mine.common.constant.enums.GameBuff;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 用户游戏数据
 */
@Document
public class UserGameData extends BaseModel {

    /**
     * userId
     */
    @Indexed(sparse = true)
    private Long userId;

    /**
     * 体力
     */
    private int energy;

    /**
     * 上次消耗体力时间
     */
    private long lastConsumeEnergyTime;

    /**
     * 预计体力长满极限时间
     */
    private long predictEnergyFullTime;

    /**
     * 经验值
     */
    private int empiric;

    /**
     * userId
     */
    @Indexed(sparse = true)
    private String jiguangId;

    /**
     * 正在进行的挖掘记录
     */
    private DigRecord digRecord;

    /**
     * 当前挖掘的地块
     * */
    private LandArea currentLandArea;

    /**
     * 当前地块信息
     * */
    private PageLandArea currentPageLandArea;

    /**
     * buf类型列表
     * */
    private List<GameBuff> buffs;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public long getLastConsumeEnergyTime() {
        return lastConsumeEnergyTime;
    }

    public void setLastConsumeEnergyTime(long lastConsumeEnergyTime) {
        this.lastConsumeEnergyTime = lastConsumeEnergyTime;
    }

    public long getPredictEnergyFullTime() {
        return predictEnergyFullTime;
    }

    public void setPredictEnergyFullTime(long predictEnergyFullTime) {
        this.predictEnergyFullTime = predictEnergyFullTime;
    }

    public String getJiguangId() {
        return jiguangId;
    }

    public void setJiguangId(String jiguangId) {
        this.jiguangId = jiguangId;
    }

    public DigRecord getDigRecord() {
        return digRecord;
    }

    public void setDigRecord(DigRecord digRecord) {
        this.digRecord = digRecord;
    }

    /**
     * 获取经验值
     *
     * @return
     */
    public int getEmpiric() {
        return empiric;
    }

    /**
     * 设置经验值
     *
     * @param empiric 验值
     */
    public void setEmpiric(int empiric) {
        this.empiric = empiric;
    }

    public LandArea getCurrentLandArea() {
        return currentLandArea;
    }

    public void setCurrentLandArea(LandArea currentLandArea) {
        this.currentLandArea = currentLandArea;
    }

    public PageLandArea getCurrentPageLandArea() {
        return currentPageLandArea;
    }

    public void setCurrentPageLandArea(PageLandArea currentPageLandArea) {
        this.currentPageLandArea = currentPageLandArea;
    }

    public List<GameBuff> getBuffs() {
        return buffs;
    }

    public void setBuffs(List<GameBuff> buffs) {
        this.buffs = buffs;
    }
}
