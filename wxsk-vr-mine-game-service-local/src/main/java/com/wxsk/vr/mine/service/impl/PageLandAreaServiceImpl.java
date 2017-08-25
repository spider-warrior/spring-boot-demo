package com.wxsk.vr.mine.service.impl;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.util.AppContext;
import com.wxsk.vr.mine.config.GameConfig;
import com.wxsk.vr.mine.model.*;
import com.wxsk.vr.mine.service.LandAreaService;
import com.wxsk.vr.mine.service.PageLandAreaService;
import com.wxsk.vr.mine.service.UserGameDataService;
import com.wxsk.vr.mine.service.UserLandAreaService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class PageLandAreaServiceImpl extends BaseServiceImpl<PageLandArea> implements PageLandAreaService {

    private static final Logger logger = LogManager.getLogger(PageLandAreaServiceImpl.class);

    @Autowired
    private GameConfig gameConfig;
    @Autowired
    private UserLandAreaService userLandAreaService;
    @Autowired
    private UserGameDataService userGameDataService;
    @Autowired
    private LandAreaService landAreaService;

    private static final Random random = new Random();

    @Override
    public PageLandArea randomLandArea(int size, int count) {

        List<Integer> landAreaPool;
        if (count <= 0) {
            landAreaPool = gameConfig.getLandAreaTypeConfigList().get(0).getTotalList();
        }
        else if (count <= 3) {
            landAreaPool = gameConfig.getLandAreaTypeConfigList().get(1).getTotalList();
        }
        else if (count <= 6) {
            landAreaPool = gameConfig.getLandAreaTypeConfigList().get(2).getTotalList();
        }
        else if (count <= 9) {
            landAreaPool = gameConfig.getLandAreaTypeConfigList().get(3).getTotalList();
        }
        else {
            landAreaPool = gameConfig.getLandAreaTypeConfigList().get(4).getTotalList();
        }

        List<LandArea> landAreaList = new ArrayList<>(size);
        Integer[] typeArr = new Integer[landAreaPool.size()];
        Integer[] randomArr = randomLandAreaFromPool(landAreaPool.toArray(typeArr),size);
        for (int i=0; i<size; i++) {
            LandArea landArea = new LandArea();
            landArea.setIndex(i);
            landArea.setLandAreaType(gameConfig.getLandAreaTypeConfigList().get(randomArr[i]).getLandAreaType());
            landAreaList.add(landArea);
        }
        PageLandArea pageLandArea = new PageLandArea();
        pageLandArea.setLandAreaList(landAreaList);
        return pageLandArea;
    }

    @Override
    public PageLandArea queryCurrentLandArea(User user) throws BusinessException {
        UserLandArea userLandArea = userLandAreaService.queryUserLandArea(user);
        PageLandArea currentPageLandArea = userLandArea.getCurrentLandAreas();
        //检查翻页
        long now = AppContext.getCurrentRequestTimePoint().getTime();
        LandArea landArea = currentPageLandArea.getLandAreaList().get(currentPageLandArea.getLandAreaList().size() - 1);
        if (landArea.getEndTime() > 0 && landArea.getEndTime() < now && landAreaService.getLandAreaRequireEnergy(landArea) == 0) {
            userLandAreaService.turnToNextPage(user);
            currentPageLandArea = userLandAreaService.queryUserLandArea(user).getCurrentLandAreas();
        }
        return currentPageLandArea;
    }

    @Override
    public PageLandArea queryNextLandArea(User user) {
        UserLandArea userLandArea = userLandAreaService.queryUserLandArea(user);
        return userLandArea.getNextLandAreas();
    }
    
    /**
     * 从数组池子里面随机抽取30个，
     * @param poolSrc
     * @return
     */
    public static Integer[] randomLandAreaFromPool(final Integer[] poolSrc,Integer size){
    	Integer[] pool = Arrays.copyOf(poolSrc, poolSrc.length);
    	for(int i = 0; i < pool.length; i++){
        	int idx1=random.nextInt(pool.length);
        	int idx2=random.nextInt(pool.length);
        	Integer temp = pool[idx1];
        	pool[idx1] = pool[idx2];
        	pool[idx2] = temp;
    	}
    	return  Arrays.copyOf(pool,size);
    }
    
	@Override
	public Integer sumContainEnergyOfPageLandArea(PageLandArea pageLandArea) {
		Integer containEnergy = 0;
		for(LandArea landArea : pageLandArea.getLandAreaList()){
			containEnergy += landArea.getContainEnergy();
		}
		return containEnergy;
	}

	@Override
	public Long lastTimeOfPageLandArea(PageLandArea pageLandArea) {
		int len = pageLandArea.getLandAreaList().size() - 1;
		for(int i = len;i >=0; i--){
			LandArea landArea = pageLandArea.getLandAreaList().get(i);
			if(landArea != null && landArea.getEndTime() > 0){
				return landArea.getEndTime();
			}
		}
		return 0L;
	}

    @Override
    public PageLandArea queryUserPageLandAreaByIndex(long userId, int index) {
        Query query = new Query();
        query.addCriteria(new Criteria("userId").is(userId).and("index").is(index));
        List<PageLandArea> pageLandAreaList = queryByParam(query);
        if (pageLandAreaList.size() > 1) {
            logger.warn("user: {} have duplicate [PageLandArea], index: {}", userId, index);
        }
        return pageLandAreaList.size() > 0 ? pageLandAreaList.get(0) : null;
    }

    @Override
    public void updatePageLandArea(PageLandArea pageLandArea) {
        Query query = new Query();
        query.addCriteria(new Criteria("_id").is(pageLandArea.getId()));
        Update update = Update.update("flushCnt", pageLandArea.getFlushCnt());
        update.set("userId", pageLandArea.getUserId());
        update.set("landAreaList", pageLandArea.getLandAreaList());
        updateFirst(query, update);
    }

    @Override
    public void removeUserPageLandArea(User user) {
        Query query = new Query();
        query.addCriteria(new Criteria("userId").is(user.getId()));
        mongoTemplate.remove(query, clazz);
    }

    @Override
    public boolean isStarted(PageLandArea pageLandArea, long nowTime) {
        if (pageLandArea == null || pageLandArea.getLandAreaList() == null || pageLandArea.getLandAreaList().size() == 0) {
            return false;
        }
        //检查地一个地块是否开始
        LandArea landArea = pageLandArea.getLandAreaList().get(0);
        if (landArea.getEndTime() == 0 || landArea.getStartTime() > nowTime) {
            return false;
        }
        return true;
    }
}
