package com.wxsk.vr.mine.controller.response.wrapper;

import com.wxsk.vr.mine.controller.response.vo.PageLandAreaVo;
import com.wxsk.vr.mine.controller.response.vo.constants.VoType;
import com.wxsk.vr.mine.model.PageLandArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PageLandAreaVoWrapper {

    @Autowired
    private LandAreaVoWrapper landAreaVoWrapper;

    public PageLandAreaVo buildPageLandAreaVo(PageLandArea pageLandArea, long now) {
        if (pageLandArea == null) {
            return null;
        }
        PageLandAreaVo pageLandAreaVo = new PageLandAreaVo();
        pageLandAreaVo.setStructureType(VoType.PAGE_LAND_AREA.getValue());
        pageLandAreaVo.setLandAreaVoList(landAreaVoWrapper.buildLandAreaVoList(pageLandArea.getLandAreaList(), now));
        pageLandAreaVo.setPageIndex(pageLandArea.getIndex());

        return pageLandAreaVo;
    }
}
