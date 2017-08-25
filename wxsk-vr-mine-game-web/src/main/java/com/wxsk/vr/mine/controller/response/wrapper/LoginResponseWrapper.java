package com.wxsk.vr.mine.controller.response.wrapper;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.common.json.JSONResult;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.util.AppContext;
import com.wxsk.vr.mine.controller.response.vo.LandAreaVo;
import com.wxsk.vr.mine.helper.AppHelper;
import com.wxsk.vr.mine.model.PageLandArea;
import com.wxsk.vr.mine.model.UserGameData;
import com.wxsk.vr.mine.service.PageLandAreaService;
import com.wxsk.vr.mine.service.UserAccountService;
import com.wxsk.vr.mine.service.UserGameDataService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 登录结果包装
 */
@Service
public class LoginResponseWrapper {

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserGameDataService userGameDataService;
    @Autowired
    private PageLandAreaService pageLandAreaService;
    @Autowired
    private LandAreaVoWrapper landAreaVoWrapper;
    @Autowired
    private AccountVoWrapper accountVoWrapper;

    public JSONResult buildJsonResult(JSONResult jsonResult) throws BusinessException {
        Map<String, Object> data = jsonResult.getData();
        if (data == null) {
            data = new HashMap<>();
            jsonResult.setData(data);
        }
        data.put("systime", AppContext.getCurrentRequestTimePoint());
        if (jsonResult.getStatus() == 1) {
            //用户账户信息
            data.put("account", accountVoWrapper.buildAccountVo(userAccountService.queryUserAccount((User)data.get("user"))));
            //用户当前体力
            UserGameData userGameData = userGameDataService.queryUserGameData((User)data.get("user"));
            data.put("energy", userGameDataService.queryUserEnergy(userGameData));
            //第一层矿
            PageLandArea pageLandArea = pageLandAreaService.queryCurrentLandArea((User)data.get("user"));
            List<LandAreaVo> landAreaVoList = new ArrayList<>();
            pageLandArea.forEach(landArea -> landAreaVoList.add(landAreaVoWrapper.buildLandAreaVo(userGameData, landArea)));
            data.put("landAreas", landAreaVoList);
            //页码
            data.put("pageIndex", pageLandArea.getIndex());
        }
        else {
        	String errorCode = jsonResult.getErrorCode();
        	if(StringUtils.isEmpty(errorCode)){
                errorCode = jsonResult.getMessage();
        	}
            jsonResult.setErrorCode(errorCode);
        }
        return jsonResult;
    }

}
