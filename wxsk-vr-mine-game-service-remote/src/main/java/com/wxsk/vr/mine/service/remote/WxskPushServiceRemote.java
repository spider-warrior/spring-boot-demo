package com.wxsk.vr.mine.service.remote;

import com.wxsk.common.json.JSONResult;
import com.wxsk.vr.mine.model.JsonData;

public interface WxskPushServiceRemote {
	void pushWebsoketMsg(JsonData.optEnum e, JSONResult jSONResult);
}
