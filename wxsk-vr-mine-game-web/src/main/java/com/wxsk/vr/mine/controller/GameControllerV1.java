package com.wxsk.vr.mine.controller;

import com.wxsk.cas.client.annotation.AccessRequired;
import com.wxsk.common.exception.BusinessException;
import com.wxsk.passport.model.User;
import com.wxsk.vr.account.model.Account;
import com.wxsk.vr.mine.common.util.AppContext;
import com.wxsk.vr.mine.common.util.JSONResult;
import com.wxsk.vr.mine.controller.response.vo.DigRecordVo;
import com.wxsk.vr.mine.controller.response.vo.LandAreaHarvestVo;
import com.wxsk.vr.mine.controller.response.vo.LandAreaVo;
import com.wxsk.vr.mine.controller.response.vo.PageLandAreaVo;
import com.wxsk.vr.mine.controller.response.wrapper.*;
import com.wxsk.vr.mine.model.AwardType;
import com.wxsk.vr.mine.model.LandArea;
import com.wxsk.vr.mine.model.PageLandArea;
import com.wxsk.vr.mine.model.UserGameData;
import com.wxsk.vr.mine.service.PageLandAreaService;
import com.wxsk.vr.mine.service.UserAccountService;
import com.wxsk.vr.mine.service.UserGameDataService;
import com.wxsk.vr.mine.service.UserLandAreaService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;

/**
 * 体力,能量
 */
@RequestMapping(value = "v1")
@Controller
@RestController
public class GameControllerV1 {

    private static final Logger logger = LogManager.getLogger(GameControllerV1.class);

    @Autowired
    private UserGameDataService userGameDataService;
    @Autowired
    private UserLandAreaService userLandAreaService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private PageLandAreaService pageLandAreaService;
    @Autowired
    private LandAreaHarvestVoWrapper landAreaHarvestVoWrapper;
    @Autowired
    private LandAreaVoWrapper landAreaVoWrapper;
    @Autowired
    private PageLandAreaVoWrapper pageLandAreaVoWrapper;
    @Autowired
    private AccountVoWrapper accountVoWrapper;
    @Autowired
    private DigRecordVoWrapper digRecordVoWrapper;

    /**
     * 接口名称: 消耗所有体力挖矿
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "consume_all_energy_4_mining", method = RequestMethod.GET)
    public Object consumeAllEnergy4Mining() throws BusinessException {
        User user = AppContext.getCurrentUser();
        userGameDataService.consumeAllEnergy4Mining(user);
        PageLandArea pageLandArea = pageLandAreaService.queryCurrentLandArea(user);
        List<LandAreaVo> landAreaVoList = new ArrayList<>();
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        pageLandArea.forEach(landArea -> landAreaVoList.add(landAreaVoWrapper.buildLandAreaVo(userGameData, landArea)));
        Map<String, Object> data = new HashMap<>();
        data.put("pageIndex", pageLandArea.getIndex());
        data.put("systime", AppContext.getCurrentRequestTimePoint());
        data.put("landAreas", landAreaVoList);
        return JSONResult.success().setData(data);
    }

    /**
     * 接口名称: 查询用户体力
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_user_energy", method = RequestMethod.GET)
    public Object queryUserEnergy() throws BusinessException {
        User user = AppContext.getCurrentUser();
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        int energy = userGameDataService.queryUserEnergy(userGameData);
        Map<String, Object> data = new HashMap<>();
        data.put("systime", AppContext.getCurrentRequestTimePoint());
        data.put("energy", energy);
        return JSONResult.success().setData(data);
    }

    /**
     * 接口名称: 查询挖矿结束时间
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_mine_end_time", method = RequestMethod.GET)
    public Object queryMineEndTime() {
        User user = AppContext.getCurrentUser();
        Date endTime = userGameDataService.queryMineEndTime(user);
        Map<String, Object> data = new HashMap<>();
        data.put("systime", AppContext.getCurrentRequestTimePoint());
        data.put("endTime", endTime);
        return JSONResult.success().setData(data);
    }

    /**
     * 请求收益情况
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_user_harvest", method = RequestMethod.GET)
    public Object queryHarvest() throws BusinessException {
        User user = AppContext.getCurrentUser();
        Map<Byte, AwardType> landAreaAward =  userLandAreaService.queryUserUninformedLandAreaAward(user);
        List<LandAreaHarvestVo> landAreaHarvestVoList = landAreaHarvestVoWrapper.buildLandAreaAward(landAreaAward);
        DigRecordVo digRecordVo = digRecordVoWrapper.buildDigRecordVo(userGameDataService.queryUserGameData(user).getDigRecord());
        Map<String, Object> data = new HashMap<>();
        data.put("systime", AppContext.getCurrentRequestTimePoint());
        data.put("harvests", landAreaHarvestVoList);
        data.put("digRecord", digRecordVo);
        return JSONResult.success().setData(data);
    }

    /**
     * 获取第一页矿状态
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_current_land_area", method = RequestMethod.GET)
    public Object queryCurrentLandAreaList() throws BusinessException {
        User user = AppContext.getCurrentUser();
        PageLandArea pageLandArea = pageLandAreaService.queryCurrentLandArea(user);
        List<LandAreaVo> landAreaVoList = new ArrayList<>();
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        pageLandArea.forEach(landArea -> landAreaVoList.add(landAreaVoWrapper.buildLandAreaVo(userGameData, landArea)));
        Map<String, Object> data = new HashMap<>();
        data.put("pageIndex", pageLandArea.getIndex());
        data.put("systime", AppContext.getCurrentRequestTimePoint());
        data.put("landAreas", landAreaVoList);
        return JSONResult.success().setData(data);
    }

    /**
     * 获取第二页矿状态
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_next_land_area", method = RequestMethod.GET)
    public Object queryNextLandAreaList() {
        User user = AppContext.getCurrentUser();
        PageLandArea pageLandArea = pageLandAreaService.queryNextLandArea(user);
        List<LandAreaVo> landAreaVoList = new ArrayList<>();
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        pageLandArea.forEach(landArea -> landAreaVoList.add(landAreaVoWrapper.buildLandAreaVo(userGameData, landArea)));
        Map<String, Object> data = new HashMap<>();
        data.put("pageIndex", pageLandArea.getIndex());
        data.put("systime", AppContext.getCurrentRequestTimePoint());
        data.put("landAreas", landAreaVoList);
        return JSONResult.success().setData(data);
    }

    /**
     * 查询第一页矿区地块详细信息
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_current_land_area_detail/{index}", method = RequestMethod.GET)
    public Object queryCurrentLandAreaByIndex(@PathVariable(value = "index") int index) {
        User user = AppContext.getCurrentUser();
        LandArea landArea = userLandAreaService.queryCurrentLandAreaByIndex(user, index);
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        LandAreaVo landAreaVo = landAreaVoWrapper.buildLandAreaVo(userGameData, landArea);
        Map<String, Object> data = new HashMap<>();
        data.put("systime", AppContext.getCurrentRequestTimePoint());
        data.put("landArea", landAreaVo);
        return JSONResult.success().setData(data);
    }

    /**
     * 查询第二页矿区地块详细信息
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_next_land_area_detail/{index}", method = RequestMethod.GET)
    public Object queryNextLandAreaByIndex(@PathVariable(value = "index") int index) {
        User user = AppContext.getCurrentUser();
        LandArea landArea = userLandAreaService.queryNextLandAreaByIndex(user, index);
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        LandAreaVo landAreaVo = landAreaVoWrapper.buildLandAreaVo(userGameData, landArea);
        Map<String, Object> data = new HashMap<>();
        data.put("systime", AppContext.getCurrentRequestTimePoint());
        data.put("landArea", landAreaVo);
        return JSONResult.success().setData(data);
    }

    /**
     * 获取系统当前时间
     * */
    @RequestMapping(value = "query_system_time", method = RequestMethod.GET)
    public Object querySystemTime() {
        Date now = new Date();
        Map<String, Object> data = new HashMap<>();
        data.put("systime", AppContext.getCurrentRequestTimePoint());
        data.put("systimeMills", now.getTime());
        return JSONResult.success().setData(data);
    }

    /**
     * 查询账户信息
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_user_account", method = RequestMethod.GET)
    public Object queryUserAccount() {
        User user = AppContext.getCurrentUser();
        Account account = userAccountService.queryUserAccount(user);
        Map<String, Object> data = new HashMap<>();
        data.put("systime", AppContext.getCurrentRequestTimePoint());
        data.put("account", account);
        return JSONResult.success().setData(data);
    }
    
    /**
     * 刷新矿区
     * 取矿区第一块矿石，如果第一块矿石有结束时间，并且结束时间减所需时长大于当前时间，说明该矿区还没开始挖，可以刷新
     * 前置条件，用户点开始挂机，区块才有结束时间，刷新矿区
     * @throws BusinessException
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "flush_area/{index}", method = RequestMethod.GET)
    public Object flushArea(@PathVariable(value = "index") int index) throws BusinessException {
        User user = AppContext.getCurrentUser();
        PageLandArea pageLandArea = userGameDataService.flushArea(user, index);
        List<LandAreaVo> landAreaVoList = new ArrayList<>();
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        pageLandArea.forEach(landArea -> landAreaVoList.add(landAreaVoWrapper.buildLandAreaVo(userGameData, landArea)));
        return JSONResult.success().addValue("landAreas", landAreaVoList)
                .addValue("pageIndex", pageLandArea.getIndex())
                .addValue("systime", AppContext.getCurrentRequestTimePoint())
                .addValue("account", accountVoWrapper.buildAccountVo(userAccountService.queryUserAccount(user)));
    }

    /**
     * 翻页
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "turn_to_next_page", method = RequestMethod.GET)
    public Object turnToNextPage() throws BusinessException {
        User user = AppContext.getCurrentUser();
        userLandAreaService.turnToNextPage(user);
        return JSONResult.success().addValue("systime", AppContext.getCurrentRequestTimePoint());
    }

    /**
     * 接口名称: 领取体力
     * @throws ParseException
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "receive_energy_daily", method = RequestMethod.GET)
    public Object receiveEnergyDaily() throws BusinessException, ParseException {
        User user = AppContext.getCurrentUser();
        long addEnergy= userGameDataService.receiveEnergyDaily(user);
        return JSONResult.success().addValue("addEnergy", addEnergy);
    }

    /**
     * 查询矿工位置
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "where_is_the_boy", method = RequestMethod.GET)
    public Object whereIsTheBoy() throws BusinessException {
        User user = AppContext.getCurrentUser();
        int index = userLandAreaService.queryWorkerIndex(user);
        Map<String, Object> data = new HashMap<>();
        data.put("index", index);
        return JSONResult.success().setData(data);
    }

    /**
     * 查询用户PageLandArea
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "user_page_land_area_detail/{index}", method = RequestMethod.GET)
    public Object queryUserPageLandArea(@PathVariable("index") Integer index) {
        User user = AppContext.getCurrentUser();
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        PageLandArea pageLandArea = pageLandAreaService.queryUserPageLandAreaByIndex(user.getId(), index);
        PageLandAreaVo pageLandAreaVo = pageLandAreaVoWrapper.buildPageLandAreaVo(userGameData, pageLandArea);
        Map<String, Object> data = new HashMap<>();
        data.put("pageLandArea", pageLandAreaVo);
        return JSONResult.success().setData(data);
    }

    /**
     * 本次挖矿详情
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "user_dig_record_detail", method = RequestMethod.GET)
    public Object queryDigRecordDetail() {
        User user = AppContext.getCurrentUser();
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        DigRecordVo digRecordVo = digRecordVoWrapper.buildDigRecordVo(userGameData.getDigRecord());
        Map<String, Object> data = new HashMap<>();
        data.put("digRecord", digRecordVo);
        data.put("systime", AppContext.getCurrentRequestTimePoint());
        return JSONResult.success().setData(data);
    }
    
    /**
     * 本次挖矿详情
     * @throws BusinessException 
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "reload_full_info", method = RequestMethod.GET)
    public Object reload_full_info() throws BusinessException {
        User user = AppContext.getCurrentUser();
    	Map<String, Object> data = new HashMap<String, Object>();
    	//用户账户信息
        data.put("account", accountVoWrapper.buildAccountVo(userAccountService.queryUserAccount(user)));
        //用户当前体力
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        data.put("energy", userGameDataService.queryUserEnergy(userGameData));
        //第一层矿
        PageLandArea pageLandArea = pageLandAreaService.queryCurrentLandArea(user);
        List<LandAreaVo> landAreaVoList = new ArrayList<>();
        pageLandArea.forEach(landArea -> landAreaVoList.add(landAreaVoWrapper.buildLandAreaVo(userGameData, landArea)));
        data.put("landAreas", landAreaVoList);
        //页码
        data.put("pageIndex", pageLandArea.getIndex());
        data.put("user", user);
        data.put("systime", AppContext.getCurrentRequestTimePoint());
        return JSONResult.success().setData(data);
    }

    /**
     * 查询本次挖矿已经取得的所有收益
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_all_gotten_award", method = RequestMethod.GET)
    public Object queryAllGottenAward() throws BusinessException {
        User user = AppContext.getCurrentUser();
        Map<Byte, AwardType> landAreaAward =  userLandAreaService.queryUserInformedLandAreaAward(user);
        List<LandAreaHarvestVo> landAreaHarvestVoList = landAreaHarvestVoWrapper.buildLandAreaAward(landAreaAward);
        Map<String, Object> data = new HashMap<>();
        data.put("systime", AppContext.getCurrentRequestTimePoint());
        data.put("harvests", landAreaHarvestVoList);
        return JSONResult.success().setData(data);
    }


}
