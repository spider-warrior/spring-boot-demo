package com.wxsk.vr.mine.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wxsk.cas.client.annotation.AccessRequired;
import com.wxsk.common.exception.BusinessException;
import com.wxsk.mine.account.constant.Enums.CoinTypeEnum;
import com.wxsk.mine.account.model.Account;
import com.wxsk.mine.account.service.remote.IAccountServiceRemote;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.util.AppContext;
import com.wxsk.vr.mine.common.util.JSONResult;
import com.wxsk.vr.mine.service.*;
import com.wxsk.vr.mine.service.impl.JpushServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("test")
@RestController
public class TestController {

    private static final Logger logger = LogManager.getLogger(TestController.class);

    @Autowired
    private UserGameDataService userGameDataService;
    @Autowired
    private PageLandAreaService pageLandAreaService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private DigRecordService digRecordService;
    @Autowired
    private MagicBoxService magicBoxService;
    @Reference(version = "1.0", timeout = 5000)
    private IAccountServiceRemote accountServiceRemote;
    @Autowired
    private JpushServiceImpl jpushServiceImpl;


    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(method = RequestMethod.GET)
    public Object test() {
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
        return com.wxsk.common.json.JSONResult.success();
    }

    /**
     * 清理用户数据
     */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "clear_user_data", method = RequestMethod.GET)
    public Object clearUserData() throws BusinessException {
        User user = AppContext.getCurrentUser();
        userGameDataService.remove(userGameDataService.queryUserGameData(user));
        pageLandAreaService.removeUserPageLandArea(user);
        digRecordService.removeUserDigRecord(user);
        magicBoxService.removeUserMagicBox(user);
        Account account = userAccountService.queryUserAccount(user);
        for (CoinTypeEnum typeEnum : CoinTypeEnum.values()) {
            long amount;
            switch (typeEnum) {
                case DIAMOND: {
                    amount = account.getDiamond();
                    break;
                }
                case GOLDCOIN: {
                    amount = account.getGoldCoin();
                    break;
                }
                case INFINITECOIN: {
                    amount = account.getInfiniteCoin();
                    break;
                }
                default: {
                    amount = 0;
                }
            }
            userAccountService.deductionCoin(user, amount, typeEnum, "clear user data for test");
        }
        logger.info("user data cleared, userId: {}", user.getId());
        return JSONResult.success();
    }

    @RequestMapping(value = "test2", method = RequestMethod.GET)
    public Object test2() throws BusinessException {
        jpushServiceImpl.sendDigFinished();
        return JSONResult.success();
    }
}
