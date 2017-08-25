package com.wxsk.vr.mine.service.impl;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.constant.enums.LandAwardType;
import com.wxsk.vr.mine.common.util.AppContext;
import com.wxsk.vr.mine.model.*;
import com.wxsk.vr.mine.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.wxsk.vr.mine.common.constant.enums.ServiceErrorCode.CURRENT_PAGE_LAND_AREA_ING;
import static com.wxsk.vr.mine.common.constant.enums.ServiceErrorCode.GAME_SERVER_INTERNAL_EXCEPTION;

@Service
public class UserLandAreaServiceImpl extends BaseServiceImpl<UserLandArea> implements UserLandAreaService {

    private static final Logger logger = LogManager.getLogger(UserLandAreaServiceImpl.class);

    @Autowired
    private UserGameDataService userGameDataService;
    @Autowired
    private LandAreaService landAreaService;
    @Autowired
    private UserLandAreaService userLandAreaService;
    @Autowired
    private PageLandAreaService pageLandAreaService;
    @Autowired
    private VrCoinService vrCoinService;
    @Autowired
    private UserAccountService userAccountService;

    @Override
    public UserLandArea queryUserLandArea(User user) {
        Query query = new Query();
        query.addCriteria(new Criteria("userId").is(user.getId()));
        List<UserLandArea> userLandAreaList = queryByParam(query);
        if (userLandAreaList.size() == 0) {
            logger.info("no [UserLandArea] found, username: {}", user.getUsername());
            initUserLandArea(user);
            logger.info("username: {}, [UserLandArea] has been initialized", user.getUsername());
            userLandAreaList = queryByParam(query);
            pageLandAreaService.insert(userLandAreaList.get(0).getCurrentLandAreas());
        }
        if (userLandAreaList.size() > 1) {
            logger.warn("duplicated [UserLandArea] in DB, username: {}", user.getUsername());
        }
        return userLandAreaList.get(0);
    }

    @Override
    public void updateUserLandArea(UserLandArea userLandArea) {
        Query query = new Query();
        query.addCriteria(new Criteria("_id").is(userLandArea.getId()));
        Update update = Update.update("currentLandAreas", userLandArea.getCurrentLandAreas());
        update.set("nextLandAreas", userLandArea.getNextLandAreas());
        updateFirst(query, update);
    }

    @Override
    public void initUserLandArea(User user) {
        UserLandArea userLandArea = new UserLandArea();
        userLandArea.setUserId(user.getId());
        PageLandArea currentLandAreas = pageLandAreaService.randomLandArea(29, 0);
        currentLandAreas.setUserId(user.getId());
        currentLandAreas.setIndex(1);
        userLandArea.setCurrentLandAreas(currentLandAreas);
        PageLandArea nextLandAreas = pageLandAreaService.randomLandArea(29, 0);
        nextLandAreas.setUserId(user.getId());
        nextLandAreas.setIndex(2);
        userLandArea.setNextLandAreas(nextLandAreas);
        insert(userLandArea);
    }

    @Override
    public DigRecord consumeEnergyOnLandAreaList(int energy, long timeMark, PageLandArea... pageLandAreas) throws BusinessException {
        long startTime = timeMark;
        List<Integer> pageIndexes = new ArrayList<>(pageLandAreas.length);
        out: for (PageLandArea pageLandArea: pageLandAreas) {
            pageIndexes.add(pageLandArea.getIndex());
            for (LandArea area: pageLandArea) {
                //体力已消耗完
                if (energy <= 0) {
                    break out;
                }
                int oldContainedEnergy = area.getContainEnergy();
                boolean dealt = userLandAreaService.consumeEnergyOnLandArea(area, energy, timeMark);
                if (dealt) {
                    //减能量, 设置结束时间
                    energy -= (area.getContainEnergy() - oldContainedEnergy);
                    timeMark = area.getEndTime() + 1;
                }
            }
        }
        DigRecord digRecord = new DigRecord();
        digRecord.setEndTime(timeMark - 1);
        digRecord.setTotalDigTime(timeMark - startTime);
        digRecord.setInformed(false);
        digRecord.setPageIndex(pageIndexes);
        return digRecord;
    }

    @Override
    public boolean consumeEnergyOnLandArea(LandArea landArea, int energy, long timeMark) throws BusinessException {
        //体力已消耗完
        if (energy <= 0) {
            return false;
        }
        int requireEnergy = landAreaService.getLandAreaRequireEnergy(landArea);
        //挖完的地块
        if (landArea.getEndTime() > 0 && requireEnergy == 0) {
            return false;
        }
        boolean energyEnough;
        long startTime = timeMark;
        boolean firstTime = landArea.getEndTime() == 0;
        int oldContainEnergy = landArea.getContainEnergy();
        //设置地块消耗体力
        if (requireEnergy > energy) {
            landArea.setContainEnergy(landArea.getContainEnergy() + energy);
            energyEnough = false;
        }
        else {
            landArea.setContainEnergy(landArea.getContainEnergy() + requireEnergy);
            energyEnough = true;
        }
        int totalSpendTimeInMinute = landArea.getLandAreaType().getSpendTimeInSecond();
        int totalConsumeEnergy = landArea.getLandAreaType().getConsumeEnergy();
        // 体力足够且未开始过
        if (energyEnough && firstTime) {
            timeMark += TimeUnit.SECONDS.toMillis(totalSpendTimeInMinute);
        }
        // 体力不够且未开始过
        else if (!energyEnough && firstTime) {
            // 兑换时间 = 所需总时间 * (地块已消耗体力 / 地块所需总体力)
            int exchangeMinute = new BigDecimal(totalSpendTimeInMinute).multiply(new BigDecimal(landArea.getContainEnergy()).divide(new BigDecimal(totalConsumeEnergy), 2, RoundingMode.DOWN)).intValue();
            timeMark += TimeUnit.SECONDS.toMillis(exchangeMinute);
        }
        // 体力足够且开始过 || 体力不够且开始过
        else {
            // 兑换时间 = 所需总时间 * (本次增加体力 / 地块所需总体力)
            int energyAdd = landArea.getContainEnergy() - oldContainEnergy;
            int exchangeMinute = new BigDecimal(totalSpendTimeInMinute).multiply(new BigDecimal(energyAdd).divide(new BigDecimal(totalConsumeEnergy), 2, RoundingMode.DOWN)).intValue();
            timeMark += TimeUnit.SECONDS.toMillis(exchangeMinute);
        }
        landArea.setStartTime(startTime);
        landArea.setEndTime(timeMark);

        //无限币类型
        if (landArea.getLandAreaType().getAwardType().getValue() == LandAwardType.VR_COIN.value) {
            landAreaService.generateVrCoinAward(landArea);
        }
        return true;
    }

    @Override
    public List<LandArea> filterUserLandAreaByTimeline(PageLandArea pageLandArea, long startTime, long endTime) throws BusinessException {
        if (pageLandArea == null) {
            return Collections.emptyList();
        }
        List<LandArea> landAreaList = new ArrayList<>();
        for (LandArea landArea: pageLandArea) {
            if (landArea.getEndTime() > startTime && landArea.getEndTime() < endTime) {
                landAreaList.add(landArea);
            }
        }
        return landAreaList;
    }

    @Override
    public Map<Byte, AwardType> queryUserUninformedLandAreaAward(User user) throws BusinessException {
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        DigRecord digRecord = userGameData.getDigRecord();
        //未开始或者已通知
        if (digRecord == null || digRecord.getEndTime() <= 0 || digRecord.isInformed()) {
            return Collections.emptyMap();
        }
        UserLandArea userLandArea = userLandAreaService.queryUserLandArea(user);
        Map<Byte, AwardType> awardTypeMapping = new HashMap<>();
        Map<Byte, Long> awardTypeMappingToIncrease = new HashMap<>();
        List<Integer> pageIndexList = digRecord.getPageIndex();
        long now = AppContext.getCurrentRequestTimePoint().getTime();
        long startTime = digRecord.getEndTime() - digRecord.getTotalDigTime();
        long endTime = digRecord.getEndTime();
        if (endTime > now) {
            endTime = now;
        }
        else {
            digRecord.setInformed(true);
        }
        //记录涉及的地块
        for (Integer index: pageIndexList) {
            PageLandArea pageLandArea = pageLandAreaService.queryUserPageLandAreaByIndex(user.getId(), index);
            if (pageLandArea == null) {
                //如果是下一页
                if (index == userLandArea.getNextLandAreas().getIndex()) {
                    pageLandArea = userLandArea.getNextLandAreas();
                    boolean started = pageLandAreaService.isStarted(pageLandArea, now);
                    if (started) {
                        //翻页
                        userLandAreaService.turnToNextPage(user);
                        pageLandArea = pageLandAreaService.queryUserPageLandAreaByIndex(user.getId(), index);
                        if (pageLandArea == null) {
                            logger.error("翻页异常");
                            continue;
                        }
                        userLandArea = userLandAreaService.queryUserLandArea(user);
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            List<LandArea> landAreaList = filterUserLandAreaByTimeline(pageLandArea, startTime, endTime);
            if (!landAreaList.isEmpty()) {
                for (LandArea landArea: landAreaList) {
                    AwardType awardType = landArea.getLandAreaType().getAwardType();
                    AwardType saved = awardTypeMapping.get(awardType.getValue());
                    if (digRecord.getEndTime() < now || !landArea.isInformed()) {
                        if (saved == null) {
                            try {
                                awardTypeMapping.put(awardType.getValue(), (AwardType)awardType.clone());
                            } catch (CloneNotSupportedException e) {
                                logger.error("AwardType Clone Exception", e);
                                throw new BusinessException(GAME_SERVER_INTERNAL_EXCEPTION.msg, null, GAME_SERVER_INTERNAL_EXCEPTION.code);
                            }
                        }
                        else {
                            saved.setAmount(saved.getAmount() + awardType.getAmount());
                        }
                    }
                    if (!landArea.isInformed()) {
                        Long savedAmount = awardTypeMappingToIncrease.getOrDefault(awardType.getValue(), 0L);
                        awardTypeMappingToIncrease.put(awardType.getValue(), savedAmount + (long)awardType.getAmount());
                        landArea.setInformed(true);
                    }
                }
            }
            //更新PageLandArea
            pageLandAreaService.updatePageLandArea(pageLandArea);
            if (pageLandArea.getIndex() == userLandArea.getCurrentLandAreas().getIndex()) {
                userLandArea.setCurrentLandAreas(pageLandArea);
                userLandAreaService.updateUserLandArea(userLandArea);
            }
        }
        //累计收益
        if (awardTypeMappingToIncrease.size() > 0) {
            for (Map.Entry<Byte, Long> entry: awardTypeMappingToIncrease.entrySet()) {
                userAccountService.increaseAccount(user, entry.getKey(), entry.getValue());
            }
        }
        //级联更新UserLandArea
        userGameDataService.updateUserGameData(userGameData);
        return awardTypeMapping;
    }

    @Override
    public Map<Byte, AwardType> queryUserInformedLandAreaAward(User user) throws BusinessException {
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        DigRecord digRecord = userGameData.getDigRecord();
        //未开始
        if (digRecord == null || digRecord.getEndTime() <= 0) {
            return Collections.emptyMap();
        }
        Map<Byte, AwardType> awardTypeMapping = new HashMap<>();
        List<Integer> pageIndexList = digRecord.getPageIndex();
        long now = AppContext.getCurrentRequestTimePoint().getTime();
        long startTime = digRecord.getEndTime() - digRecord.getTotalDigTime();
        long endTime = digRecord.getEndTime();
        if (endTime > now) {
            endTime = now;
        }
        //记录涉及的地块
        for (Integer index: pageIndexList) {
            PageLandArea pageLandArea = pageLandAreaService.queryUserPageLandAreaByIndex(user.getId(), index);
            if (pageLandArea != null) {
                List<LandArea> landAreaList = filterUserLandAreaByTimeline(pageLandArea, startTime, endTime);
                if (!landAreaList.isEmpty()) {
                    for (LandArea landArea: landAreaList) {
                        if (digRecord.getEndTime() < now || landArea.isInformed()) {
                            AwardType awardType = landArea.getLandAreaType().getAwardType();
                            AwardType saved = awardTypeMapping.get(awardType.getValue());
                            if (saved == null) {
                                try {
                                    awardTypeMapping.put(awardType.getValue(), (AwardType)awardType.clone());
                                } catch (CloneNotSupportedException e) {
                                    logger.error("AwardType Clone Exception", e);
                                    throw new BusinessException(GAME_SERVER_INTERNAL_EXCEPTION.msg, null, GAME_SERVER_INTERNAL_EXCEPTION.code);
                                }
                            }
                            else {
                                saved.setAmount(saved.getAmount() + awardType.getAmount());
                            }
                        }
                    }
                }
            }
        }
        return awardTypeMapping;
    }

    @Override
    public LandArea queryCurrentLandAreaByIndex(User user, int index) {
        UserLandArea userLandArea = queryUserLandArea(user);
        PageLandArea pageLandArea = userLandArea.getCurrentLandAreas();
        return pageLandArea.getLandAreaList().get(index);
    }

    @Override
    public LandArea queryNextLandAreaByIndex(User user, int index) {
        UserLandArea userLandArea = queryUserLandArea(user);
        PageLandArea pageLandArea = userLandArea.getNextLandAreas();
        return pageLandArea.getLandAreaList().get(index);
    }

    @Override
    public void turnToNextPage(User user) throws BusinessException {
        UserLandArea userLandArea = queryUserLandArea(user);
        //地块状态检查
        long now = AppContext.getCurrentRequestTimePoint().getTime();
        PageLandArea pageLandArea = userLandArea.getCurrentLandAreas();
        for (LandArea landArea: pageLandArea) {
            if (landArea.getEndTime() == 0 || landAreaService.getLandAreaRequireEnergy(landArea) > 0 || landArea.getEndTime() > now) {
                throw new BusinessException(CURRENT_PAGE_LAND_AREA_ING.msg, null, CURRENT_PAGE_LAND_AREA_ING.code);
            }
        }
        userLandArea.setCurrentLandAreas(userLandArea.getNextLandAreas());
        PageLandArea nextPageLandArea = pageLandAreaService.randomLandArea(29, 0);
        nextPageLandArea.setUserId(user.getId());
        nextPageLandArea.setIndex(userLandArea.getCurrentLandAreas().getIndex() + 1);
        userLandArea.setNextLandAreas(nextPageLandArea);
        updateUserLandArea(userLandArea);
        pageLandAreaService.insert(userLandArea.getCurrentLandAreas());
    }

    @Override
    public int queryWorkerIndex(User user) {
        UserGameData userGameData = userGameDataService.queryUserGameData(user);
        UserLandArea userLandArea = queryUserLandArea(user);
        DigRecord digRecord = userGameData.getDigRecord();
        int index = -1;
        if (digRecord != null) {
            index = queryWorkerIndex(userLandArea.getCurrentLandAreas(), digRecord.getEndTime());
            if (index < -1) {
                index = queryWorkerIndex(userLandArea.getNextLandAreas(), digRecord.getEndTime());
            }
        }
        return index;
    }

    @Override
    public int queryWorkerIndex(PageLandArea pageLandArea, long endTime) {
        long now = System.currentTimeMillis();
        for (LandArea landArea: pageLandArea) {
            boolean ing = isWorkerOn(now, endTime, landArea);
            if (ing) {
                return landArea.getIndex();
            }
            int requireEnergy = landAreaService.getLandAreaRequireEnergy(landArea);
            if (requireEnergy > 0) {
                return landArea.getIndex();
            }
        }
        return -1;
    }

    @Override
    public boolean isWorkerOn(long now,long endTime, LandArea landArea) {
        long areaEndTime = landArea.getEndTime();
        long totalTime = TimeUnit.SECONDS.toMillis(landArea.getLandAreaType().getSpendTimeInSecond());
        if (areaEndTime < now) {
            if (areaEndTime != 0 && (endTime == areaEndTime || (landArea.getContainEnergy() <  landArea.getLandAreaType().getConsumeEnergy()))) {
                return true;
            }
            return false;
        }
        long range = areaEndTime - now;
        if (range <= totalTime) {
            return true;
        }
        else {
            return false;
        }
    }

}
