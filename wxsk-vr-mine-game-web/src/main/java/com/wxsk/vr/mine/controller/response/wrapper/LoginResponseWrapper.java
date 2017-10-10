package com.wxsk.vr.mine.controller.response.wrapper;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.common.json.JSONResult;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.controller.util.ResponseUtil;
import com.wxsk.vr.mine.model.PageLandArea;
import com.wxsk.vr.mine.model.UserGameData;
import com.wxsk.vr.mine.service.PageLandAreaService;
import com.wxsk.vr.mine.service.UserAccountService;
import com.wxsk.vr.mine.service.UserGameDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
    @Autowired
    private UserGameDataVoWrapper userGameDataVoWrapper;

    public JSONResult buildJsonResult(JSONResult jsonResult, long now) throws BusinessException {
        Map<String, Object> data = ResponseUtil.getResultMap();
        Map<String, Object> jsonResultData = jsonResult.getData();
        if (jsonResultData != null) {
            data.putAll(jsonResultData);
        }
        jsonResult.setData(data);
        if (jsonResult.getStatus() == 1) {
            User user = (User)data.get("user");
            data.putAll(buildFullLoginResponse(user, now));
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

    public Map<String, Object> buildFullLoginResponse(User user, long now) throws BusinessException {
        Map<String, Object> data = ResponseUtil.getResultMap();
        //同步
        pageLandAreaService.synchronizedPageLandArea(user, now);
        //用户账户信息
        data.put("account", accountVoWrapper.buildAccountVo(userAccountService.queryUserAccount(user)));
        //用户当前体力
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        data.put("energy", userGameDataService.queryUserCurrentEnergy(userGameData, now));
        //第一层矿
        PageLandArea pageLandArea = userGameData.getCurrentPageLandArea();
        data.put("landAreas", landAreaVoWrapper.buildLandAreaVoList(pageLandArea.getLandAreaList(), now));
        //页码
        data.put("pageIndex", pageLandArea.getIndex());
        //用户游戏数据
        data.put("userGameData", userGameDataVoWrapper.buildUserGameDataVo(userGameData));
        //user
        data.put("user", user);
        return data;
    }

}
