package com.wxsk.vr.mine.controller.response.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class AccountVo {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("infiniteCoin")
    private Double vrCoin;

    @JsonProperty("goldCoin")
    private Double goldCoin;

    @JsonProperty("diamond")
    private Double diamond;

    @JsonProperty("ctime")
    private Date ctime;

    @JsonProperty("mtime")
    private Date mtime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getVrCoin() {
        return vrCoin;
    }

    public void setVrCoin(Double vrCoin) {
        this.vrCoin = vrCoin;
    }

    public Double getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(Double goldCoin) {
        this.goldCoin = goldCoin;
    }

    public Double getDiamond() {
        return diamond;
    }

    public void setDiamond(Double diamond) {
        this.diamond = diamond;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getMtime() {
        return mtime;
    }

    public void setMtime(Date mtime) {
        this.mtime = mtime;
    }
}
