package com.wxsk.vr.mine.common.util;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 生成缓存key
 */
public class CatchKeyFactory {
	private static final String DAY_BUY_ENERGY_KEY = "day:buy:energy:key:";
	private static final String RECEIVE_ENERGY_DAILY_KEY = "receive:energy:daily:key:";
	private static final String PUSH_TASK_EXECUTE_TIME_KEY = "push:task:execute:time:key";
	
	/**
	 * 生成用户今日购买体力总值的缓存key
	 * 
	 * @param userId
	 * @return
	 */
	public static String getKeyOfUserBuyEnergy(Long userId) {
		return new StringBuilder(DAY_BUY_ENERGY_KEY).append(DateFormatUtils.format(new Date(), "yyyy-MM-dd"))
				.append(":").append(userId).toString();
	}

	/**
	 * 用户每日领取体力缓存key
	 * 
	 * @param userId
	 * @return
	 */
	public static String getKeyOfReceiveEnergyDaily(Long userId, int batchNum) {
		return new StringBuilder(RECEIVE_ENERGY_DAILY_KEY).append(DateFormatUtils.format(new Date(), "yyyy-MM-dd"))
				.append(":").append(userId).append(":").append(batchNum).toString();
	}
	
	/**
	 * 推送定时任务执行时间缓存key
	 * 
	 * @return
	 */
	public static String getKeyOfPushTaskExecuteTime() {
		return RECEIVE_ENERGY_DAILY_KEY;
	}
}
