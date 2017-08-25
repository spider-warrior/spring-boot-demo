package com.wxsk.vr.mine.controller;

import com.wxsk.cas.client.annotation.AccessRequired;
import com.wxsk.passport.model.User;
import com.wxsk.vr.account.model.Account;
import com.wxsk.vr.mine.common.util.AppContext;
import com.wxsk.vr.mine.common.util.JSONResult;
import com.wxsk.vr.mine.controller.response.vo.AccountVo;
import com.wxsk.vr.mine.controller.response.wrapper.AccountVoWrapper;
import com.wxsk.vr.mine.helper.AppHelper;
import com.wxsk.vr.mine.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户数据控制器
 */
@RequestMapping("user")
@RestController
public class UserController {

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private AccountVoWrapper accountVoWrapper;

    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping("account/detail")
    public Object userDetail() {
        User user = AppContext.getCurrentUser();
        Account account = userAccountService.queryUserAccount(user);
        AccountVo accountVo = accountVoWrapper.buildAccountVo(account);
        Map<String, Object> data = new HashMap<>();
        data.put("account", accountVo);
        data.put("systime", AppContext.getCurrentRequestTimePoint());
        return JSONResult.success().setData(data);
    }
}
