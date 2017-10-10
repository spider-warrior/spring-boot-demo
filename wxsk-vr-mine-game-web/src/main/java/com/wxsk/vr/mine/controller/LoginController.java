package com.wxsk.vr.mine.controller;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.common.json.JSONResult;
import com.wxsk.vr.mine.common.util.AppContext;
import com.wxsk.vr.mine.controller.response.wrapper.LoginResponseWrapper;
import com.wxsk.vr.mine.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RequestMapping("login")
@RestController
public class LoginController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private LoginResponseWrapper loginResponseWrapper;


    /**
     * 用户密码登录
     *
     * @param username string 用户名
     * @param password string 密码
     * @param remember string 是否记住密码
     */
    @RequestMapping(value = "rest/password", method = RequestMethod.POST)
    public Object passwordLogin(@RequestBody Map<String, Object> param) throws BusinessException {
        String username = (String) param.get("username");
        String password = (String) param.get("password");
        Boolean remember = (Boolean) param.get("remember");
        JSONResult result = userAccountService.passwordLogin(username, password, remember);
        Date now = AppContext.getCurrentRequestTimePoint();
        return loginResponseWrapper.buildJsonResult(result, now.getTime());
    }

    @RequestMapping(value = "password")
    public Object passwordLoginUrlEncode(@RequestParam Map<String, Object> param) throws BusinessException {
        String username = (String) param.get("username");
        String password = (String) param.get("password");
        Boolean remember = (Boolean) param.get("remember");
        JSONResult result = userAccountService.passwordLogin(username, password, remember);
        Date now = AppContext.getCurrentRequestTimePoint();
        return loginResponseWrapper.buildJsonResult(result, now.getTime());
    }

    /**
     * 用户短信验证码登录
     *
     * @param username string 用户名
     * @param code     string 短信验证码
     * @param remember string 是否记住密码
     */
    @RequestMapping(value = "rest/code", method = RequestMethod.POST)
    public Object phoneMessageLogin(@RequestBody Map<String, Object> param) throws BusinessException {
        String username = (String) param.get("username");
        String code = (String) param.get("code");
        Boolean remember = (Boolean) param.get("remember");
        JSONResult result = userAccountService.phoneMessageLogin(username, code, remember);
        Date now = AppContext.getCurrentRequestTimePoint();
        return loginResponseWrapper.buildJsonResult(result, now.getTime());
    }

    @RequestMapping(value = "code")
    public Object phoneMessageLoginUrlEncode(@RequestParam Map<String, Object> param) throws BusinessException {
        String username = (String) param.get("username");
        String code = (String) param.get("code");
        Boolean remember = (Boolean) param.get("remember");
        JSONResult result = userAccountService.phoneMessageLogin(username, code, remember);
        Date now = AppContext.getCurrentRequestTimePoint();
        return loginResponseWrapper.buildJsonResult(result, now.getTime());
    }

    /**
     * 用户手机mac登录
     *
     * @param mac      string 手机mac
     * @param remember string 是否记住密码
     */
    @RequestMapping(value = "rest/mac", method = RequestMethod.POST)
    public Object macLogin(@RequestBody Map<String, Object> param) throws BusinessException {
        String mac = (String) param.get("mac");
        Boolean remember = (Boolean) param.get("remember");
        JSONResult result = userAccountService.macLogin(mac, remember);
        Date now = AppContext.getCurrentRequestTimePoint();
        return loginResponseWrapper.buildJsonResult(result, now.getTime());
    }

    @RequestMapping(value = "mac")
    public Object macLoginUrlEncode(@RequestParam Map<String, Object> param) throws BusinessException {
        String mac = (String) param.get("mac");
        Boolean remember = (Boolean) param.get("remember");
        JSONResult result = userAccountService.macLogin(mac, remember);
        Date now = AppContext.getCurrentRequestTimePoint();
        return loginResponseWrapper.buildJsonResult(result, now.getTime());
    }

    @RequestMapping(value = "reg_and_login")
    public Object regAndLogin(@RequestParam Map<String, Object> param) throws BusinessException {
        String username = (String) param.get("username");
        String password = (String) param.get("password");
        String surePassword = (String) param.get("surePassword");
        String code = (String) param.get("code");
        JSONResult result = userAccountService.regAndLogin(username, password, surePassword, code, 1);
        Date now = AppContext.getCurrentRequestTimePoint();
        return loginResponseWrapper.buildJsonResult(result, now.getTime());
    }


}
