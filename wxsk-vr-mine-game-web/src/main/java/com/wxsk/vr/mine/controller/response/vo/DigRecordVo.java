package com.wxsk.vr.mine.controller.response.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class DigRecordVo {

    @JsonProperty("endTime")
    private Long endTime;

    @JsonProperty("totalTime")
    private Long totalTime;

    @JsonProperty("totalTimeStr")
    private String totalTimeStr;

    public Long getEndTime() {
        return endTime;
    }

    public DigRecordVo setEndTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public DigRecordVo setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
        return this;
    }

    public String getTotalTimeStr() {
        return totalTimeStr;
    }

    public DigRecordVo setTotalTimeStr(String totalTimeStr) {
        this.totalTimeStr = totalTimeStr;
        return this;
    }
}
