package com.wxsk.vr.mine.ws;

import com.wxsk.common.json.JSONResult;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.util.AppContext;
import com.wxsk.vr.mine.model.JsonData;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class HandlerTemplate {
	public static final Map<String,HandlerTemplate> handlerMap= new ConcurrentHashMap<String,HandlerTemplate>();
	public abstract JSONResult dealWithData(JsonData data);
	public final JSONResult getAnswer(JsonData data, User user){
		AppContext.initRequestContext(user,new Date());
		JSONResult rst = dealWithData(data);
		rst.addValue("opt", data.getOpt());
		AppContext.clearRequestContext();
		return rst;
	}

}

@Component
class TestHandler extends HandlerTemplate {
	TestHandler(){
		handlerMap.put("/test/testHandler", this);
	}
	@Override
	public JSONResult dealWithData(JsonData data) {
		return new JSONResult().success();
	}

}

@Component
class  HeartbeatHandler extends HandlerTemplate {
	HeartbeatHandler(){
		handlerMap.put("/ws/heartbeat", this);
	}

	@SuppressWarnings("static-access")
	@Override
	public JSONResult dealWithData(JsonData data) {
		return new JSONResult().success();
	}

}

//@Component
//class  QueryCurrentLandAreaSetailHandler extends HandlerTemplate{
//	@Autowired
//	GameControllerV1 gameControllerV1;
//	QueryCurrentLandAreaSetailHandler(){
//		handlerMap.put("/v1/query_current_land_area_detail", this);
//	}
//
//	@Override
//	public JSONResult dealWithData(JsonData data) {
//		// {"opt":"/v1//v1/query_current_land_area_detail","data":{"index":0}}
//		JSONResult rst = JSONResult.faild();
//		int index = Integer.parseInt(data.getData().get("index"));
//		try {
//			rst = gameControllerV1.queryCurrentLandAreaByIndex(index);
//		} catch (BusinessException e) {
//			e.printStackTrace();
//		}
//		return rst;
//	}

//}


