package com.wxsk.vr.mine.controller.response.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class DigRecordVo extends BaseVo {

    @JsonProperty("startTime")
    private Long startTime;

    @JsonProperty("endTime")
    private Long endTime;

    @JsonProperty("startTimeStr")
    private Date startTimeStr;

    @JsonProperty("endTimeStr")
    private Date endTimeStr;


    @JsonProperty("totalTime")
    private Long totalTime;

    @JsonProperty("totalTimeStr")
    private String totalTimeStr;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Date getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(Date startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public Date getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(Date endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalTimeStr() {
        return totalTimeStr;
    }

    public void setTotalTimeStr(String totalTimeStr) {
        this.totalTimeStr = totalTimeStr;
    }
}
