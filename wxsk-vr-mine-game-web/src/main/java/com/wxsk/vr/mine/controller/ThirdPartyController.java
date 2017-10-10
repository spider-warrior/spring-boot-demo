package com.wxsk.vr.mine.controller;

import com.wxsk.cas.client.annotation.AccessRequired;
import com.wxsk.common.exception.BusinessException;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.util.AppContext;
import com.wxsk.vr.mine.common.util.JSONResult;
import com.wxsk.vr.mine.model.UserGameData;
import com.wxsk.vr.mine.service.UserGameDataService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("third_party")
@RestController
public class ThirdPartyController {

    private static final Logger logger = LogManager.getLogger(ThirdPartyController.class);
    @Autowired
    private UserGameDataService userGameDataService;

    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "jiguang/binding", method = RequestMethod.POST)
    public Object binding(@RequestParam String jiguangId) throws BusinessException {
        User user = AppContext.getCurrentUser();
        UserGameData userGameData = userGameDataService.queryUserGameData(user) ;
        userGameData.setJiguangId(jiguangId);
        userGameDataService.updateUserGameData(userGameData);
        logger.info("用户："+user.getUsername()+"绑定极光Id");
        return JSONResult.success();
    }
}
