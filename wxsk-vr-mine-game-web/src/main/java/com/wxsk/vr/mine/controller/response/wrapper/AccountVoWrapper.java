package com.wxsk.vr.mine.controller.response.wrapper;


import com.wxsk.mine.account.model.Account;
import com.wxsk.vr.mine.controller.response.vo.AccountVo;
import com.wxsk.vr.mine.controller.response.vo.constants.VoType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class AccountVoWrapper {

    public AccountVo buildAccountVo(Account account) {
        if (account == null) {
            return null;
        }
        AccountVo accountVo = new AccountVo();
        accountVo.setStructureType(VoType.ACCOUNT.getValue());
        accountVo.setId(account.getId());
        accountVo.setUserId(account.getUserId());
        accountVo.setUsername(account.getUsername());
        accountVo.setVrCoin(account.getInfiniteCoin() == null ? 0.0 : new BigDecimal(account.getInfiniteCoin()).divide(new BigDecimal(10), 1, RoundingMode.DOWN).doubleValue());
        accountVo.setGoldCoin(account.getGoldCoin() == null ? 0.0 : new BigDecimal(account.getGoldCoin()).divide(new BigDecimal(10), 1, RoundingMode.DOWN).doubleValue());
        accountVo.setDiamond(account.getDiamond() == null ? 0.0 : new BigDecimal(account.getDiamond()).divide(new BigDecimal(10), 1, RoundingMode.DOWN).doubleValue());
        return accountVo;
    }
}
