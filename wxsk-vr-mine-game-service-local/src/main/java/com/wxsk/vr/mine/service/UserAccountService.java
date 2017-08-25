package com.wxsk.vr.mine.service;

import com.wxsk.common.json.JSONResult;
import com.wxsk.passport.model.User;
import com.wxsk.vr.account.constant.Enums.CoinTypeEnum;
import com.wxsk.vr.account.model.Account;
import com.wxsk.vr.mine.model.AwardType;

/**
 * 用户账户信息
 */
public interface UserAccountService {

    /**
     * 查询账户信息
     * @param user 用户数据 查询时如果账户不存在则根据user对象创建一个新的Account
     * */
    Account queryUserAccount(User user);

    /**
     * 密码登录
     * @param username 用户名
     * @param password 密码
     * @param remember 是否记住密码
     * */
    JSONResult passwordLogin(String username, String password, Boolean remember);

    /**
     * 短信登录
     * @param username 用户名
     * @param code 短信验证码
     * @param remember 是否记住密码
     * */
    JSONResult phoneMessageLogin(String username, String code, Boolean remember);

    /**
     * mac登录
     * @param mac 手机mac
     * @param remember 是否记住密码
     * */
    JSONResult macLogin(String mac, Boolean remember);

    /**
     * 消耗货币
     * */
    JSONResult deductionCoin(User user, long cnt, CoinTypeEnum type,String remark);

    /**
     * 账户收益
     * @param user 用户数据
     * @param awardType 奖励类型
     * */
    void increaseAccount(User user, AwardType awardType);

    /**
     * 账户收益
     * @param user 用户数据
     * @param awardType 奖励类型
     * */
    void increaseAccount(User user, byte awardType, long amount);
    
    /**
	 * 注册并登录
	 * @param username [*] 用户名
	 * @param password [*] 密码
	 * @param surePassword [*] 确认密码
	 * @param code [*] 验证码
	 * @param seconds 记住密码：1记住，0不记住
	 * @return
	 */
	public JSONResult regAndLogin(String arg0, String arg1, String arg2, String arg3, Integer arg4);

}
