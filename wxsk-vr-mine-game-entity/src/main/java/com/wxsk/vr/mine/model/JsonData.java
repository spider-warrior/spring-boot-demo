package com.wxsk.vr.mine.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JsonData implements Serializable{
	private static final long serialVersionUID = 1L;
	private String opt = "";
	private Map<String,String> data = new HashMap<String,String>();
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public static enum optEnum{
		HEARTBEAT("/ws/heartbeat"),
		QUERY_SYSTEM_TIME("/v1/query_system_time"),
		QUERY_CURRENT_LAND_AREA_DETAIL("/v1/query_current_land_area_detail");
		private String value;
		optEnum(String value){
			this.value = value;
		}
	}
}
