package com.wxsk.vr.mine.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wxsk.cas.client.annotation.AccessRequired;
import com.wxsk.common.redis.StringRedisClusterUtil;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.constant.enums.ServiceErrorCode;
import com.wxsk.vr.mine.common.util.AppContext;
import com.wxsk.vr.mine.common.util.CatchKeyFactory;
import com.wxsk.vr.mine.common.util.JSONResult;
import com.wxsk.vr.mine.config.GameConfig;
import com.wxsk.vr.mine.model.UserGameData;
import com.wxsk.vr.mine.service.UserGameDataService;

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
    private GameConfig gameConfig;
    
    /** 一天时间  */
    private static final int DAY_TIME = 24*60*60;
    
    /**
     * 接口名称: 查询今天能够购买的体力
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_user_can_buy_energy", method = RequestMethod.GET)
    public Object queryUserCanBuyEnergy() {
        User user = AppContext.getCurrentUser();
        String dayBuyEnergyKey = CatchKeyFactory.getKeyOfUserBuyEnergy(user.getId());
    	//获取今天已经购买的体力
    	String dayBuyEnergy = stringRedisClusterUtil.get(dayBuyEnergyKey);
    	//获取游戏用户数据
    	UserGameData userGameData = userGameDataService.queryUserGameData(user);
    	//获取用户体力
		int energy = userGameDataService.queryUserEnergy(userGameData);
		//获取最大体力值
    	int maxEnergy = gameConfig.getEnergyConfig().getMax();
    	//用户每天可购买体力最大值
    	int userDayBuyMaxEnergy = gameConfig.getEnergyConfig().getUserDayBuyMaxEnergy();
    	//今天可购买体力
    	int canBuyEnergy = maxEnergy - energy ;
    	
    	if(StringUtils.isNotEmpty(dayBuyEnergy)){
    		int limitCanBuyEnergy = userDayBuyMaxEnergy - Integer.valueOf(dayBuyEnergy) ;
    		if(canBuyEnergy > limitCanBuyEnergy){
    			canBuyEnergy = limitCanBuyEnergy ;
    		}
    	}
        Map<String, Object> data = new HashMap<>();
        data.put("systime", new Date());
        data.put("canBuyEnergy", canBuyEnergy);//可购买体力
        return JSONResult.success().setData(data);
    }
    
    /**
     * 接口名称: 钻石购买体力
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "diamond", method = RequestMethod.POST)
    public Object diamond(@RequestBody Map<String, Object> param) {
    	//获取钻石数量
    	int diamondCount = (int)param.get("diamondCount");
        User user = AppContext.getCurrentUser();
	    //钻石购买体力1:1
        int energyCount = diamondCount ;
        String dayBuyEnergyKey = CatchKeyFactory.getKeyOfUserBuyEnergy(user.getId());
    	//获取今天已经购买的体力
    	String dayBuyEnergy = stringRedisClusterUtil.get(dayBuyEnergyKey);
    	//获取游戏用户数据
    	UserGameData userGameData = userGameDataService.queryUserGameData(user);
    	//获取用户体力
		int energy = userGameDataService.queryUserEnergy(userGameData);
		//获取最大体力值
    	int maxEnergy = gameConfig.getEnergyConfig().getMax();
    	//用户每天可购买体力最大值
    	int userDayBuyMaxEnergy = gameConfig.getEnergyConfig().getUserDayBuyMaxEnergy();
    	//今天可购买体力
    	int canBuyEnergy = maxEnergy - energy ;
    	Map<String, Object> data = new HashMap<>();
        data.put("systime", new Date());
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
        int restEnergy = userGameDataService.addEnergy(user, energyCount);
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
