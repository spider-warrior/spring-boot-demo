package com.wxsk.vr.mine.controller.response.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class PageLandAreaVo extends BaseVo {

    private int pageIndex;

    @JsonProperty("landAreas")
    private List<LandAreaVo> landAreaVoList;

    @JsonProperty("pageIndex")
    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<LandAreaVo> getLandAreaVoList() {
        return landAreaVoList;
    }

    public void setLandAreaVoList(List<LandAreaVo> landAreaVoList) {
        this.landAreaVoList = landAreaVoList;
    }
}
