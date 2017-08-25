package com.wxsk.vr.mine.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 领取每日体力配置
 */
@Document
public class ReceiveEnergy {
	/**
	 * 领取的时间段标记
	 */
	private int num;
	/**
	 * 领取开始时间
	 */
	private String startTime;
	/**
	 * 领取结束时间
	 */
	private String endTime;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public ReceiveEnergy() {
		super();
	}
	public ReceiveEnergy(int num, String startTime, String endTime) {
		super();
		this.num = num;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	
}
