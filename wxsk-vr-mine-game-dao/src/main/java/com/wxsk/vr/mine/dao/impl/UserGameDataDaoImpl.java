package com.wxsk.vr.mine.dao.impl;

import com.mongodb.WriteResult;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.dao.UserGameDataDao;
import com.wxsk.vr.mine.model.LandArea;
import com.wxsk.vr.mine.model.PageLandArea;
import com.wxsk.vr.mine.model.UserGameData;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserGameDataDaoImpl extends BaseDaoImpl<UserGameData> implements UserGameDataDao {

    private static final String currentPageLandArea = "currentPageLandArea";
    private static final String currentLandArea = "currentLandArea";
    private static final String landAreaList = "landAreaList";

    @Override
    public PageLandArea queryUserCurrentPageLandArea(User user) {
        UserGameDataDao.UserGameDataParam param = new UserGameDataDao.UserGameDataParam();
        param.setUserId(user.getId());
        param.addIncludeColumn(currentPageLandArea);
        Query query = param.buildQuery();
        List<UserGameData> userGameDataList = queryByNativeQuery(query);
        return userGameDataList == null || userGameDataList.size() == 0 ? null : userGameDataList.get(0).getCurrentPageLandArea();
    }

    @Override
    public LandArea queryUserCurrentLandArea(User user) {
        UserGameDataDao.UserGameDataParam param = new UserGameDataDao.UserGameDataParam();
        param.setUserId(user.getId());
        param.addIncludeColumn(currentLandArea);
        Query query = param.buildQuery();
        List<UserGameData> userGameDataList = queryByNativeQuery(query);
        return userGameDataList == null || userGameDataList.size() == 0 ? null : userGameDataList.get(0).getCurrentLandArea();
    }

    @Override
    public LandArea queryUserCurrentPageLandArea(User user, int index) {
        UserGameDataDao.UserGameDataParam param = new UserGameDataDao.UserGameDataParam();
        param.setUserId(user.getId());
        param.addIncludeColumn(currentPageLandArea + "." + landAreaList + ".$");
        param.addRestriction(UserGameDataDaoImpl.currentPageLandArea + "." + landAreaList + ".index", index);
        Query query = param.buildQuery();
        List<UserGameData> userGameDataList = queryByNativeQuery(query);
        if (userGameDataList == null && userGameDataList.size() > 0) {
            PageLandArea pageLandArea = userGameDataList.get(0).getCurrentPageLandArea();
            if (pageLandArea != null) {
                List<LandArea> landAreaList = pageLandArea.getLandAreaList();
                if (landAreaList != null && landAreaList.size() > 0) {
                    return landAreaList.get(0);
                }
            }
        }
        return null;
    }

    @Override
    public WriteResult updateUserCurrentPageLandArea(User user, PageLandArea currentPageLandArea) {
        UserGameDataDao.UserGameDataParam param = new UserGameDataDao.UserGameDataParam();
        param.setUserId(user.getId());
        Query query = param.buildCriteriaQuery();
        Update update = Update.update(UserGameDataDaoImpl.currentPageLandArea, currentPageLandArea);
        return mongoTemplate.updateFirst(query, update, clazz);
    }

    @Override
    public WriteResult updateUserCurrentLandArea(User user, LandArea currentLandArea) {
        UserGameDataDao.UserGameDataParam param = new UserGameDataDao.UserGameDataParam();
        param.setUserId(user.getId());
        Query query = param.buildCriteriaQuery();
        Update update = Update.update(UserGameDataDaoImpl.currentLandArea, currentLandArea);
        return mongoTemplate.updateFirst(query, update, clazz);
    }

    @Override
    public WriteResult updateUserCurrentPageLandArea(User user, LandArea landArea) {
        UserGameDataDao.UserGameDataParam param = new UserGameDataDao.UserGameDataParam();
        param.setUserId(user.getId());
        param.addRestriction(UserGameDataDaoImpl.currentPageLandArea + "." + landAreaList + ".index", landArea.getIndex());
        Query query = param.buildCriteriaQuery();
        Update update = Update.update(UserGameDataDaoImpl.currentPageLandArea + "." + landAreaList + ".$", landArea);
        return mongoTemplate.updateFirst(query, update, clazz);
    }

    @Override
    public Update buildUpdate(UserGameData userGameData) {
        Update update = Update.update("energy", userGameData.getEnergy());
        update.set("lastConsumeEnergyTime", userGameData.getLastConsumeEnergyTime());
        update.set("predictEnergyFullTime", userGameData.getPredictEnergyFullTime());
        update.set("empiric", userGameData.getEmpiric());
        update.set("jiguangId", userGameData.getJiguangId());
        update.set("digRecord", userGameData.getDigRecord());
        update.set("currentLandArea", userGameData.getCurrentLandArea());
        update.set("currentPageLandArea", userGameData.getCurrentPageLandArea());
        update.set("buffs", userGameData.getBuffs());
        return update;
    }

}
