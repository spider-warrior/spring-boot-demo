package com.wxsk.vr.mine.controller;

import com.wxsk.cas.client.annotation.AccessRequired;
import com.wxsk.common.exception.BusinessException;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.constant.enums.ConsumeType;
import com.wxsk.vr.mine.common.constant.enums.GameBuff;
import com.wxsk.vr.mine.common.util.AppContext;
import com.wxsk.vr.mine.common.util.JSONResult;
import com.wxsk.vr.mine.controller.response.vo.*;
import com.wxsk.vr.mine.controller.response.wrapper.*;
import com.wxsk.vr.mine.controller.util.ResponseUtil;
import com.wxsk.vr.mine.model.AwardType;
import com.wxsk.vr.mine.model.LandArea;
import com.wxsk.vr.mine.model.PageLandArea;
import com.wxsk.vr.mine.model.UserGameData;
import com.wxsk.vr.mine.service.LandAreaService;
import com.wxsk.vr.mine.service.MagicBoxService;
import com.wxsk.vr.mine.service.PageLandAreaService;
import com.wxsk.vr.mine.service.UserGameDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体力,能量
 */
@RequestMapping(value = "v1")
@Controller
@RestController
public class GameControllerV1 {

    @Autowired
    private LandAreaService landAreaService;
    @Autowired
    private PageLandAreaService pageLandAreaService;
    @Autowired
    private UserGameDataService userGameDataService;
    @Autowired
    private MagicBoxService magicBoxService;

    @Autowired
    private LandAreaVoWrapper landAreaVoWrapper;
    @Autowired
    private PageLandAreaVoWrapper pageLandAreaVoWrapper;
    @Autowired
    private LandAreaHarvestVoWrapper landAreaHarvestVoWrapper;
    @Autowired
    private LoginResponseWrapper loginResponseWrapper;
    @Autowired
    private DigRecordVoWrapper digRecordVoWrapper;
    @Autowired
    private GameBuffVoWrapper gameBuffVoWrapper;

    /**
     * 接口名称: 启动离线挂机模式
     */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "start_offline_digging", method = RequestMethod.GET)
    public Object beginOfflineDigging() throws BusinessException {
        User user = AppContext.getCurrentUser();
        Date now = AppContext.getCurrentRequestTimePoint();
        LandArea landArea = landAreaService.startOfflineDigging(user, now.getTime());
        LandAreaVo landAreaVo = landAreaVoWrapper.buildLandAreaVo(landArea, now.getTime());
        Map<String, Object> data = ResponseUtil.getResultMap();
        data.put("landArea", landAreaVo);
        return JSONResult.success().setData(data);
    }


    /**
     * 接口名称: 查询用户体力
     */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_user_energy", method = RequestMethod.GET)
    public Object queryUserEnergy() throws BusinessException {
        User user = AppContext.getCurrentUser();
        Date now = AppContext.getCurrentRequestTimePoint();
        UserGameData userGameData =userGameDataService.queryUserGameData(user);
        int energy = userGameDataService.queryUserCurrentEnergy(userGameData, now.getTime());
        Map<String, Object> data = ResponseUtil.getResultMap();
        data.put("energy", energy);
        return JSONResult.success().setData(data);
    }


    /**
     * 请求收益情况
     */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_user_harvest", method = RequestMethod.GET)
    public Object queryHarvest() throws BusinessException {
        User user = AppContext.getCurrentUser();
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        Collection<AwardType> awardTypeCollection = userGameDataService.queryDigRecordLandAreaAward(user, userGameData, userGameData.getDigRecord());
        List<LandAreaHarvestVo> landAreaHarvestVoList = landAreaHarvestVoWrapper.buildLandAreaAward(awardTypeCollection);
        Map<String, Object> data = ResponseUtil.getResultMap();
        data.put("harvests", landAreaHarvestVoList);
        return JSONResult.success().setData(data);
    }

    /**
     * 获取当前页地块
     */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_current_page_land_area", method = RequestMethod.GET)
    public Object queryCurrentLandAreaList() throws BusinessException {
        User user = AppContext.getCurrentUser();
        Date now = AppContext.getCurrentRequestTimePoint();
        PageLandArea pageLandArea = pageLandAreaService.queryUserCurrentPageLandArea(user, now.getTime());
        PageLandAreaVo pageLandAreaVo = pageLandAreaVoWrapper.buildPageLandAreaVo(pageLandArea, now.getTime());
        Map<String, Object> data = ResponseUtil.getResultMap();
        data.put("pageLandArea", pageLandAreaVo);
        return JSONResult.success().setData(data);
    }

    /**
     * 查询当前挖掘地块信息
     */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_current_land_area_detail", method = RequestMethod.GET)
    public Object queryCurrentLandArea() throws BusinessException {
        User user = AppContext.getCurrentUser();
        Date now = AppContext.getCurrentRequestTimePoint();
        LandArea currentLandArea = userGameDataService.queryCurrentLandArea(user, now.getTime());
        LandAreaVo landAreaVo = landAreaVoWrapper.buildLandAreaVo(currentLandArea, now.getTime());
        Map<String, Object> data = ResponseUtil.getResultMap();
        data.put("landArea", landAreaVo);
        return JSONResult.success().setData(data);
    }


    /**
     * 获取系统当前时间
     */
    @RequestMapping(value = "query_system_time", method = RequestMethod.GET)
    public Object querySystemTime() {
        Map<String, Object> data = ResponseUtil.getResultMap();
        return JSONResult.success().setData(data);
    }

    /**
     * 接口名称: 领取体力
     * @throws ParseException
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "receive_energy_daily", method = RequestMethod.GET)
    public Object receiveEnergyDaily() throws BusinessException, ParseException {
        User user = AppContext.getCurrentUser();
        Date now = AppContext.getCurrentRequestTimePoint();
        long addEnergy= userGameDataService.receiveEnergyDaily(user, now.getTime());
        Map<String, Object> data = ResponseUtil.getResultMap();
        data.put("addEnergy", addEnergy);
        return JSONResult.success().setData(data);
    }

    /**
     * 查询用户PageLandArea
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "user_page_land_area_detail/{index}", method = RequestMethod.GET)
    public Object queryUserPageLandArea(@PathVariable("index") Integer index) throws BusinessException {
        User user = AppContext.getCurrentUser();
        Date now = AppContext.getCurrentRequestTimePoint();
        PageLandArea pageLandArea = pageLandAreaService.queryUserPageLandAreaByIndex(user, index);
        PageLandAreaVo pageLandAreaVo = pageLandAreaVoWrapper.buildPageLandAreaVo(pageLandArea, now.getTime());
        Map<String, Object> data = ResponseUtil.getResultMap();
        data.put("pageLandArea", pageLandAreaVo);
        return JSONResult.success().setData(data);
    }


    /**
     * 重新加载所有数据
     * @throws BusinessException
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "reload_full_info", method = RequestMethod.GET)
    public Object reload_full_info() throws BusinessException {
        User user = AppContext.getCurrentUser();
        Map<String, Object> data = ResponseUtil.getResultMap();
        Date now = AppContext.getCurrentRequestTimePoint();
        data.putAll(loginResponseWrapper.buildFullLoginResponse(user, now.getTime()));
        return JSONResult.success().setData(data);
    }

    /**
     * 本次挖矿详情
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "user_dig_record_detail", method = RequestMethod.GET)
    public Object queryDigRecordDetail() throws BusinessException {
        User user = AppContext.getCurrentUser();
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        DigRecordVo digRecordVo = digRecordVoWrapper.buildDigRecordVo(userGameData.getDigRecord());
        Map<String, Object> data = ResponseUtil.getResultMap();
        data.put("digRecord", digRecordVo);
        return JSONResult.success().setData(data);
    }


    /**
     * 触发火山buff
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "trigger_volcano_buff", method = RequestMethod.GET)
    public Object triggerVolcanoBuff() throws BusinessException {
        User user = AppContext.getCurrentUser();
        Date now = AppContext.getCurrentRequestTimePoint();
        userGameDataService.triggerVolcanoBuff(user, now.getTime());
        Map<String, Object> data = ResponseUtil.getResultMap();
        return JSONResult.success().setData(data);
    }

    /**
     * 移除水潭buff
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "remove_swag_buff", method = RequestMethod.GET)
    public Object removeSwagBuff() throws BusinessException {
        User user = AppContext.getCurrentUser();
        Date now = AppContext.getCurrentRequestTimePoint();
        userGameDataService.removeSwagBuff(user, now.getTime());
        Map<String, Object> data = ResponseUtil.getResultMap();
        return JSONResult.success().setData(data);
    }

    /**
     * 移除藤蔓buff
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "remove_plant_buff", method = RequestMethod.GET)
    public Object removePlantBuff() throws BusinessException {
        User user = AppContext.getCurrentUser();
        Date now = AppContext.getCurrentRequestTimePoint();
        userGameDataService.removePlantBuff(user, now.getTime());
        Map<String, Object> data = ResponseUtil.getResultMap();
        return JSONResult.success().setData(data);
    }

    /**
     * 移除老鼠buff
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "remove_rat_buff", method = RequestMethod.GET)
    public Object removeRatBuff() throws BusinessException {
        User user = AppContext.getCurrentUser();
        Date now = AppContext.getCurrentRequestTimePoint();
        userGameDataService.removeRatBuff(user, now.getTime());
        Map<String, Object> data = ResponseUtil.getResultMap();
        return JSONResult.success().setData(data);
    }

    /**
     * 查询buff列表
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "query_user_game_buff", method = RequestMethod.GET)
    public Object queryUserGameBuff() throws BusinessException {
        User user = AppContext.getCurrentUser();
        Date now = AppContext.getCurrentRequestTimePoint();
        List<GameBuff> gameBuffList = userGameDataService.queryUserGameBuff(user, now.getTime());
        List<GameBuffVo> gameBuffVoList = gameBuffVoWrapper.buildGameBuffVo(gameBuffList);
        Map<String, Object> data = ResponseUtil.getResultMap();
        data.put("gameBuffs", gameBuffVoList);
        return JSONResult.success().setData(data);
    }

    /**
     * 获取当前用户宝箱数量
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "count_user_magic_box", method = RequestMethod.GET)
    public Object countUserMagicBox() throws BusinessException {
        User user = AppContext.getCurrentUser();
        long count = magicBoxService.countUserUnusedMagicBox(user);
        Map<String, Object> data = ResponseUtil.getResultMap();
        data.put("magicBoxCount", count);
        return JSONResult.success().setData(data);
    }

    /**
     * 打开宝箱
     * */
    @AccessRequired(respongseType = AccessRequired.RespongseType.JSON)
    @RequestMapping(value = "consume_magic_box/{type}", method = RequestMethod.GET)
    public Object consumeMagicBox(@PathVariable("type") Byte type, @RequestParam(value = "amount", defaultValue = "1") Byte amount) throws BusinessException {
        User user = AppContext.getCurrentUser();
        Date now = AppContext.getCurrentRequestTimePoint();
        Collection<AwardType> awardTypeCollection = magicBoxService.consumeMagicBox(user, ConsumeType.getConsumeType(type), amount, now.getTime());
        List<LandAreaHarvestVo> landAreaHarvestVoList = landAreaHarvestVoWrapper.buildLandAreaAward(awardTypeCollection);
        Map<String, Object> data = ResponseUtil.getResultMap();
        data.put("harvests", landAreaHarvestVoList);
        return JSONResult.success().setData(data);
    }

}
