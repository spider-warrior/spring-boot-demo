package com.wxsk.vr.mine.controller.response.wrapper;

import com.wxsk.vr.mine.controller.response.vo.LandAreaVo;
import com.wxsk.vr.mine.controller.response.vo.PageLandAreaVo;
import com.wxsk.vr.mine.model.LandArea;
import com.wxsk.vr.mine.model.PageLandArea;
import com.wxsk.vr.mine.model.UserGameData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PageLandAreaVoWrapper {

    @Autowired
    private LandAreaVoWrapper landAreaVoWrapper;

    public PageLandAreaVo buildPageLandAreaVo(UserGameData userGameData, PageLandArea pageLandArea) {
        if (pageLandArea == null) {
            return null;
        }
        PageLandAreaVo pageLandAreaVo = new PageLandAreaVo();
        List<LandAreaVo> landAreaVoList = new ArrayList<>();
        pageLandAreaVo.setLandAreaVoList(landAreaVoList);
        pageLandAreaVo.setPageIndex(pageLandArea.getIndex());
        for (LandArea landArea: pageLandArea) {
            LandAreaVo landAreaVo = landAreaVoWrapper.buildLandAreaVo(userGameData, landArea);
            if (landAreaVo != null) {
                landAreaVoList.add(landAreaVo);
            }
        }
        return pageLandAreaVo;
    }
}
