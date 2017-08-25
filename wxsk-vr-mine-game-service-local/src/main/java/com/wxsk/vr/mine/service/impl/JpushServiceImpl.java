package com.wxsk.vr.mine.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.wxsk.vr.mine.common.util.CatchKeyFactory;
import com.wxsk.vr.mine.model.UserGameData;
import com.wxsk.vr.mine.push.JPush;
import com.wxsk.vr.mine.service.JpushService;
import com.wxsk.vr.mine.service.UserGameDataService;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.PushPayload;

/**
 * 极光推送
 */
@Service
public class JpushServiceImpl implements JpushService{
	
	private static final Logger logger = LogManager.getLogger(JpushServiceImpl.class);
	private static final int PAGE_SIZE = 500;
	//查询挖矿完成推送时间间隔
	private static final long TIME_SPAN = 30 * 60 * 1000;
	private static final JPushClient JPUSHCLIENT = new JPushClient(JPush.MASTER_SECRET, JPush.APP_KEY, null, ClientConfig.getInstance());

	private RedisTemplate<String, String> redisTemplate;
	private ValueOperations<String, String> valueOperations;

	public JpushServiceImpl (RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		valueOperations = redisTemplate.opsForValue();
	}

	@Autowired
	private UserGameDataService userGameDataService;

	@Override
	public void sendDigFinished() {
		UserGameDataService.UserGameDataParam userGameDataParam = new UserGameDataService.UserGameDataParam();
		long endTime = System.currentTimeMillis();
		long startTime = endTime - TIME_SPAN;
		String executeTimeString = valueOperations.get(CatchKeyFactory.getKeyOfPushTaskExecuteTime());
		if (executeTimeString != null) {
			startTime = Long.parseLong(executeTimeString);
		}
		int skip = 0;
		while(true) {
			List<String> registrationids = new ArrayList<String>();
			StringBuilder userIds = new StringBuilder();
			userGameDataParam.setDigEndTimeStart(startTime);
			userGameDataParam.setDigEndTimeEnd(endTime);
			userGameDataParam.setSkip(skip);
			userGameDataParam.setLimit(PAGE_SIZE);
			List<UserGameData> userGameDataList = userGameDataService.queryUserGameDataByParam(userGameDataParam);
			for (UserGameData userGameData : userGameDataList) {
				if (userGameData != null && userGameData.getJiguangId() != null) {
					registrationids.add(userGameData.getJiguangId());
					userIds.append(userGameData.getId()).append(" ");
				}
			}
			if (userGameDataList.size() < PAGE_SIZE) {
				break;
			}
			skip += PAGE_SIZE;
			PushPayload payload = JPush.buildPushObject_all_registrationid_alert(registrationids, "挖矿完成,可查看收益");
			try {
				if (registrationids.size() > 0) {
					JPUSHCLIENT.sendPush(payload);
				}
			} catch (APIConnectionException e) {
				logger.error("APIConnectionException error userid:{} , should retry later", userIds.toString(), e);
			} catch (APIRequestException e) {
				logger.error("APIRequestException error  userid:{} , and fix the request", userIds.toString(), e);
			}
			logger.info("挖矿完成推送成功,推送用户{}个，userids:{}",registrationids.size(), userIds.toString());
		}
		valueOperations.set(CatchKeyFactory.getKeyOfPushTaskExecuteTime(), new Long(endTime).toString());
	}
	
}
