package com.wxsk.vr.mine.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wxsk.cas.service.remote.IMineLoginServiceRemote;
import com.wxsk.common.exception.BusinessException;
import com.wxsk.common.json.JSONResult;
import com.wxsk.mine.account.constant.Enums.CoinTypeEnum;
import com.wxsk.mine.account.model.Account;
import com.wxsk.mine.account.service.remote.IAccountServiceRemote;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.constant.enums.LandAreaAwardTypeValue;
import com.wxsk.vr.mine.model.AwardType;
import com.wxsk.vr.mine.model.UserGameData;
import com.wxsk.vr.mine.service.UserAccountService;
import com.wxsk.vr.mine.service.UserGameDataService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户账户实现
 */
@Service
public class UserAccountServiceImpl implements UserAccountService {

    private static final Logger logger = LogManager.getLogger(UserAccountServiceImpl.class);

    @Autowired
    private UserGameDataService userGameDataService;
    @Reference(version = "1.0", timeout = 5000)
    private IAccountServiceRemote accountServiceRemote;
    @Reference(version = "1.0", timeout = 5000)
    private IMineLoginServiceRemote loginServiceRemote;
    @Autowired
    private UserAccountService userAccountService;

    @Override
    public Account queryUserAccount(User user) {
        return accountServiceRemote.insertAndGetAccount(user);
    }

    @Override
    public JSONResult passwordLogin(String username, String password, Boolean remember) {
        return loginServiceRemote.passwordLogin(username, password, remember != null && remember ? 1 : 0);
    }

    @Override
    public JSONResult phoneMessageLogin(String username, String code, Boolean remember) {
        return loginServiceRemote.messageLogin(username, code, remember != null && remember ? 1 : 0);
    }

    @Override
    public JSONResult macLogin(String mac, Boolean remember) {
        return loginServiceRemote.macLogin(mac, remember != null && remember ? 1 : 0);
    }

    @Override
    public JSONResult deductionCoin(User user, long cnt, CoinTypeEnum type, String remark) {
        return accountServiceRemote.deductionCoin(user, cnt, type, remark);
    }

    @Override
    public JSONResult regAndLogin(String arg0, String arg1, String arg2, String arg3, Integer arg4) {
        return loginServiceRemote.registerLogin(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public void increaseAccount(User user, AwardType awardType) throws BusinessException {
        increaseAccount(user, LandAreaAwardTypeValue.getLandAreaAwardTypeValue(awardType.getValue()), awardType.getAmount());
    }

    @Override
    public void increaseAccount(User user, LandAreaAwardTypeValue awardType, long amount) throws BusinessException {
        CoinTypeEnum typeEnum = null;
        switch (awardType) {
            case GOLD_COIN: {
                typeEnum = CoinTypeEnum.GOLDCOIN;
                break;
            }
            case DIAMOND: {
                typeEnum = CoinTypeEnum.DIAMOND;
                break;
            }
            case VR_COIN: {
                typeEnum = CoinTypeEnum.INFINITECOIN;
                break;
            }
            default: {

            }
        }
        //账户收益
        if (typeEnum != null) {
            userAccountService.deductionCoin(user, -amount, typeEnum, "game收益");
            logger.info("emums: {}.收益, 类型: {}, 数量: {}", typeEnum, awardType, amount);
        }
        //其他收益
        else {
            UserGameData userGameData = userGameDataService.queryUserGameData(user);
            //体力收益
            if (awardType == LandAreaAwardTypeValue.ENERGY) {
                long now = System.currentTimeMillis();
                //兑换体力
                userGameDataService.exchangeTimeToEnergy(userGameData, now);
                //追加体力
                userGameData.setEnergy(userGameData.getEnergy() + (int)amount);
                //重新计算满体力时间
                userGameDataService.exchangeTimeToEnergy(userGameData, now);
                //更新UserGameData
            }
            //经验收益
            else if (awardType == LandAreaAwardTypeValue.EXP) {
                userGameData.setEmpiric(userGameData.getEmpiric() + (int)amount);
            }
            userGameDataService.updateUserGameData(userGameData);
        }
    }
}
