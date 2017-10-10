package com.wxsk.vr.mine.service.impl;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.PushPayload;
import com.wxsk.common.exception.BusinessException;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.dao.UserGameDataDao;
import com.wxsk.vr.mine.model.DigRecord;
import com.wxsk.vr.mine.model.LandArea;
import com.wxsk.vr.mine.model.UserGameData;
import com.wxsk.vr.mine.push.JPush;
import com.wxsk.vr.mine.service.JpushService;
import com.wxsk.vr.mine.service.PageLandAreaService;
import com.wxsk.vr.mine.service.UserGameDataService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 极光推送
 */
@Service
public class JpushServiceImpl implements JpushService {

    private static final Logger logger = LogManager.getLogger(JpushServiceImpl.class);
    private static final int PAGE_SIZE = 500;
    //查询挖矿完成推送时间间隔
    private static final JPushClient JPUSHCLIENT = new JPushClient(JPush.MASTER_SECRET, JPush.APP_KEY, null, ClientConfig.getInstance());

    @Autowired
    private UserGameDataService userGameDataService;
    @Autowired
    private PageLandAreaService pageLandAreaService;

//    @PostConstruct
//    public void init() {
//        new Thread(){
//            int period = 1000 * 10;
//            private long timePoint = System.currentTimeMillis();
//            @Override
//            public void run() {
//                while (true) {
//                    long now = System.currentTimeMillis();
//                    if (now > timePoint + period) {
//                        try {
//                            sendDigFinished();
//                            timePoint+=period;
//                        } catch (BusinessException e) {
//                            logger.error("", e);
//                        }
//                    }
//                    else {
//                        try { Thread.sleep(1000); } catch (InterruptedException e) {}
//                    }
//                }
//            }
//        }.start();
//    }

    @Override
    public void sendDigFinished() throws BusinessException {
        logger.info("pusher is going to be invoked OK!");
        UserGameDataDao.UserGameDataParam userGameDataParam = new UserGameDataDao.UserGameDataParam();
        userGameDataParam.setLimit(PAGE_SIZE);
        userGameDataParam.setJiguangIdNotNull(true);
        userGameDataParam.setInformed(false);
        while (true) {
            List<UserGameData> userGameDataList = userGameDataService.queryUserGameDataByParam(userGameDataParam);
            if (userGameDataList.size() == 0) {
                break;
            }
            List<String> registrationIds = new ArrayList<>();
            StringBuilder userIds = new StringBuilder();
            User user = new User();
            long now = System.currentTimeMillis();
            for (UserGameData userGameData : userGameDataList) {
                //有极光id
                if (userGameData != null && userGameData.getJiguangId() != null) {
                    DigRecord digRecord = userGameData.getDigRecord();
                    LandArea landArea = userGameData.getCurrentLandArea();
                    if (digRecord!= null && landArea != null) {
                        //digRecord#endTime为0并且当前地块已过期进行地块同步
                        if (digRecord.getEndTime() == 0 && landArea.getEndTime() < now) {
                            user.setId(userGameData.getUserId());
                            pageLandAreaService.synchronizedPageLandArea(user, now);
                            userGameData = userGameDataService.queryUserGameData(user);
                            digRecord = userGameData.getDigRecord();
                        }
                        if (digRecord.getEndTime() != 0 && digRecord.getEndTime() < now) {
                            registrationIds.add(userGameData.getJiguangId());
                            userIds.append(userGameData.getId()).append(" ");
                            userGameData.getDigRecord().setInformed(true);
                            userGameDataService.updateUserGameData(userGameData);
                        }
                    }
                }
            }
            PushPayload payload = JPush.buildPushObject_all_registrationid_alert(registrationIds, "挖矿完成,可查看收益");
            try {
                if (registrationIds.size() > 0) {
                    JPUSHCLIENT.sendPush(payload);
                }
            } catch (APIConnectionException e) {
                logger.error("APIConnectionException error userid:{} , should retry later", userIds.toString(), e);
            } catch (APIRequestException e) {
                logger.error("APIRequestException error  userid:{} , and fix the request", userIds.toString(), e);
            }
            logger.info("挖矿完成推送成功,推送用户{}个，userids:{}", registrationIds.size(), userIds.toString());
        }
        logger.info("pusher has been invoked OK!");
    }

}
