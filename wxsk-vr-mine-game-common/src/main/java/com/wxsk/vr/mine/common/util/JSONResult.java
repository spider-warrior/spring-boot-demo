package com.wxsk.vr.mine.common.util;

import com.wxsk.vr.mine.common.constant.enums.ServiceErrorCode;

public class JSONResult extends com.wxsk.common.json.JSONResult{
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param serviceErrorCode
	 * @return
	 */
	public static com.wxsk.common.json.JSONResult faild(ServiceErrorCode serviceErrorCode){
		return new com.wxsk.common.json.JSONResult().setErrorCode(serviceErrorCode.code).setMessage(serviceErrorCode.msg);
	}
}
