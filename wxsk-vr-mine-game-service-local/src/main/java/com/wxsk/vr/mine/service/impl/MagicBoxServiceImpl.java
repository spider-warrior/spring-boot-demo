package com.wxsk.vr.mine.service.impl;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.common.json.JSONResult;
import com.wxsk.mine.account.constant.Enums.CoinTypeEnum;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.constant.enums.ConsumeType;
import com.wxsk.vr.mine.common.constant.enums.LandAreaAwardTypeValue;
import com.wxsk.vr.mine.dao.BaseDao;
import com.wxsk.vr.mine.dao.MagicBoxDao;
import com.wxsk.vr.mine.model.AwardType;
import com.wxsk.vr.mine.model.MagicBox;
import com.wxsk.vr.mine.service.MagicBoxService;
import com.wxsk.vr.mine.service.UserAccountService;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wxsk.vr.mine.common.constant.enums.ServiceErrorCode.*;

/**
 * 挖掘服务实现
 */
@Service
public class MagicBoxServiceImpl extends BaseServiceImpl<MagicBox> implements MagicBoxService {

	private static final Logger logger = LogManager.getLogger(MagicBoxServiceImpl.class);

    @Autowired
    private MagicBoxDao magicBoxDao;

	@Autowired
	private UserAccountService userAccountService;

	@Override
	public Collection<AwardType> consumeMagicBox(User user, ConsumeType type, byte amount, long now) throws BusinessException {
		MagicBoxDao.MagicBoxParam param = new MagicBoxDao.MagicBoxParam();
		param.setUserId(user.getId());
		param.setUsed(false);
		param.setLimit(amount);
		List<MagicBox> magicBoxList = queryByParam(param);
		if (magicBoxList.size() < amount) {
			throw new BusinessException(GAME_USER_MAGIC_BOX_NOT_ENOUGH.msg, null, GAME_USER_MAGIC_BOX_NOT_ENOUGH.code);
		}
		long requiredAsset = 1000 * amount;
		CoinTypeEnum coinTypeEnum;
		if (ConsumeType.GOLD_COIN == type) {
			coinTypeEnum = CoinTypeEnum.GOLDCOIN;
		}
		else if (ConsumeType.DIAMOND == type) {
			coinTypeEnum = CoinTypeEnum.DIAMOND;
		}
		else {
			throw new BusinessException(GAME_CLIENT_INVILAD_REQUEST.msg, null, GAME_CLIENT_INVILAD_REQUEST.code);
		}
		JSONResult jsonResult = userAccountService.deductionCoin(user, requiredAsset, coinTypeEnum, "开宝箱");
		if (jsonResult.getStatus() != 1) {
			if ("ACCOUNT_0002".equals(jsonResult.getErrorCode())) {
				if (coinTypeEnum == CoinTypeEnum.GOLDCOIN) {
					throw new BusinessException(GAME_GOLD_COIN_NOT_ENOUGH.msg, null, GAME_GOLD_COIN_NOT_ENOUGH.code);
				}
				else if (coinTypeEnum == CoinTypeEnum.DIAMOND) {
					throw new BusinessException(GAME_DIAMONDS_NOT_ENOUGH.msg, null, GAME_DIAMONDS_NOT_ENOUGH.code);
				}
				else {
					throw new BusinessException(GAME_CLIENT_INVILAD_REQUEST.msg, null, GAME_CLIENT_INVILAD_REQUEST.code);
				}
			}
			else {
				throw new BusinessException(GAME_SERVER_INTERNAL_EXCEPTION.msg, null, GAME_SERVER_INTERNAL_EXCEPTION.code);
			}
		}
		Map<LandAreaAwardTypeValue, AwardType> awardTypeMap = new HashMap<>();
		for (MagicBox magicBox: magicBoxList) {
			Pair<LandAreaAwardTypeValue,Integer> pair = MagicBox.randomAward(type);
			if (pair != null) {
				AwardType awardType = awardTypeMap.get(pair.getKey());
				if (awardType == null) {
					awardType = new AwardType();
					awardType.setValue(pair.getKey().value);
					awardType.setAmount(0);
					awardTypeMap.put(pair.getKey(), awardType);
				}
				awardType.setAmount(awardType.getAmount() + pair.getValue());
			}
			//更新宝箱
			magicBox.setUseTime(now);
			magicBox.setAwardType(pair.getKey().value);
			magicBox.setAwardCnt(pair.getValue());
			magicBox.setPayType(type.value);
			updateMagicBox(magicBox);
		}
		//累计收益入库
		for (AwardType award: awardTypeMap.values()) {
			LandAreaAwardTypeValue landAreaAwardTypeValue = LandAreaAwardTypeValue.getLandAreaAwardTypeValue(award.getValue());
			if (landAreaAwardTypeValue != null && award.getAmount() != 0) {
				userAccountService.increaseAccount(user, LandAreaAwardTypeValue.getLandAreaAwardTypeValue(award.getValue()), award.getAmount());
			}
		}
		return awardTypeMap.values();
	}

	@Override
	public void removeUserMagicBox(User user) {
		magicBoxDao.removeByUserId(user.getId());
	}

	@Override
	public long countUserUnusedMagicBox(User user) {
		MagicBoxDao.MagicBoxParam param = new MagicBoxDao.MagicBoxParam();
		param.setUserId(user.getId());
		param.setUsed(false);
		return magicBoxDao.countMagicBoxByParam(param);
	}

	@Override
	public void updateMagicBox(MagicBox magicBox) {
		magicBoxDao.update(magicBox);
	}

	@Override
	public BaseDao<MagicBox> getBaseDao() {
		return magicBoxDao;
	}
}
