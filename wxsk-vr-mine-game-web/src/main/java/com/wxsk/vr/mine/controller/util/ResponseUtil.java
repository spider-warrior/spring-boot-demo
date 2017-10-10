package com.wxsk.vr.mine.controller.util;

import com.wxsk.vr.mine.common.util.AppContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    public static Map<String, Object> getResultMap() {
        Map<String, Object> data = new HashMap<>();
        Date now = AppContext.getCurrentRequestTimePoint();
        data.put("systime", now);
        data.put("systimeMills", now.getTime());
        return data;
    }
}
