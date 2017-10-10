package com.wxsk.vr.mine.controller;

import com.wxsk.cas.client.annotation.AccessRequired;
import com.wxsk.common.exception.BusinessException;
import com.wxsk.common.redis.StringRedisClusterUtil;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.constant.enums.ServiceErrorCode;
import com.wxsk.vr.mine.common.util.AppContext;
import com.wxsk.vr.mine.common.util.CatchKeyFactory;
import com.wxsk.vr.mine.common.util.JSONResult;
import com.wxsk.vr.mine.controller.util.ResponseUtil;
import com.wxsk.vr.mine.model.UserGameData;
import com.wxsk.vr.mine.properties.GameProperties;
import com.wxsk.vr.mine.service.UserGameDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * 购买体力
 */
@RequestMapping(value = "buy_energy")
@RestController
public class BuyEnergyController {

    @Autowired
    private UserGameDataService userGameDataService;
    @Autowired
	private StringRedisClusterUtil stringRedisClusterUtil;
    @Autowired
    private GameProperties gameProperties;

    /** 一天时间  */
    private static final int DAY_TIME = 24*60*60;

    /**
     * 接口名称: 查询今天能够购买的体力
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_user_can_buy_energy", method = RequestMethod.GET)
    public Object queryUserCanBuyEnergy() throws BusinessException {
        User user = AppContext.getCurrentUser();
        Date now = AppContext.getCurrentRequestTimePoint();
        String dayBuyEnergyKey = CatchKeyFactory.getKeyOfUserBuyEnergy(user.getId());
    	//获取今天已经购买的体力
    	String dayBuyEnergy = stringRedisClusterUtil.get(dayBuyEnergyKey);
    	//获取游戏用户数据
    	UserGameData userGameData = userGameDataService.queryUserGameData(user);
    	//获取用户体力
		int energy = userGameDataService.queryUserCurrentEnergy(userGameData, now.getTime());
		//获取最大体力值
    	int maxEnergy = gameProperties.getEnergyConfig().getMax();
    	//用户每天可购买体力最大值
    	int userDayBuyMaxEnergy = gameProperties.getEnergyConfig().getMaxReceiveEnergyInEachTimeRange();
    	//今天可购买体力
    	int canBuyEnergy = maxEnergy - energy ;

    	if(StringUtils.isNotEmpty(dayBuyEnergy)){
    		int limitCanBuyEnergy = userDayBuyMaxEnergy - Integer.valueOf(dayBuyEnergy) ;
    		if(canBuyEnergy > limitCanBuyEnergy){
    			canBuyEnergy = limitCanBuyEnergy ;
    		}
    	}
		Map<String, Object> data = ResponseUtil.getResultMap();
        data.put("canBuyEnergy", canBuyEnergy);//可购买体力
        return JSONResult.success().setData(data);
    }

    /**
     * 接口名称: 钻石购买体力
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "diamond", method = RequestMethod.POST)
    public Object diamond(@RequestBody Map<String, Object> param) throws BusinessException {
    	//获取钻石数量
    	int diamondCount = (int)param.get("diamondCount");
        User user = AppContext.getCurrentUser();
        Date now = AppContext.getCurrentRequestTimePoint();
	    //钻石购买体力1:1
        int energyCount = diamondCount ;
        String dayBuyEnergyKey = CatchKeyFactory.getKeyOfUserBuyEnergy(user.getId());
    	//获取今天已经购买的体力
    	String dayBuyEnergy = stringRedisClusterUtil.get(dayBuyEnergyKey);
    	//获取游戏用户数据
    	UserGameData userGameData = userGameDataService.queryUserGameData(user);
    	//获取用户体力
		int energy = userGameDataService.queryUserCurrentEnergy(userGameData, now.getTime());
		//获取最大体力值
    	int maxEnergy = gameProperties.getEnergyConfig().getMax();
    	//用户每天可购买体力最大值
    	int userDayBuyMaxEnergy = gameProperties.getEnergyConfig().getMaxReceiveEnergyInEachTimeRange();
    	//今天可购买体力
    	int canBuyEnergy = maxEnergy - energy ;
		Map<String, Object> data = ResponseUtil.getResultMap();
    	if(energyCount > canBuyEnergy){
    		data.put("canBuyEnergy", canBuyEnergy);//今天可购买体力
			return JSONResult.faild(ServiceErrorCode.GAME_DAY_BUY_ENERGY_OVERRUN).setData(data) ;
    	}
    	if(StringUtils.isNotEmpty(dayBuyEnergy)){
    		int limitCanBuyEnergy = userDayBuyMaxEnergy - Integer.valueOf(dayBuyEnergy) ;
    		if(canBuyEnergy > limitCanBuyEnergy){
    			data.put("canBuyEnergy", limitCanBuyEnergy);//今天可购买体力
    			return JSONResult.faild(ServiceErrorCode.GAME_DAY_BUY_ENERGY_OVERRUN).setData(data) ;
    		}
    	}
        int restEnergy = userGameDataService.addEnergy(user, energyCount, now.getTime());
        //往缓存里面存入今天购买的体力
        if(StringUtils.isEmpty(dayBuyEnergy)){
    		stringRedisClusterUtil.add(dayBuyEnergyKey,  "" + energyCount, DAY_TIME);
    	}else{
    		stringRedisClusterUtil.set(dayBuyEnergyKey,  "" + (energyCount + Integer.parseInt(dayBuyEnergy)));
    	}
        data.put("restEnergy", restEnergy);//剩余体力
        return JSONResult.success().setData(data);
    }

}
