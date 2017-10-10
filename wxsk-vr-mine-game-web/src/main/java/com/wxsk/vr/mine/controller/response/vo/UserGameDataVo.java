package com.wxsk.vr.mine.controller.response.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserGameDataVo extends BaseVo {

    /**
     * buff列表
     * */
    @JsonProperty("gameBuffs")
    private List<GameBuffVo> gameBuffVoList;

    /**
     * 挖掘记录
     */
    @JsonProperty("digRecord")
    private DigRecordVo digRecordVo;

    public List<GameBuffVo> getGameBuffVoList() {
        return gameBuffVoList;
    }

    public void setGameBuffVoList(List<GameBuffVo> gameBuffVoList) {
        this.gameBuffVoList = gameBuffVoList;
    }

    public DigRecordVo getDigRecordVo() {
        return digRecordVo;
    }

    public void setDigRecordVo(DigRecordVo digRecordVo) {
        this.digRecordVo = digRecordVo;
    }
}
