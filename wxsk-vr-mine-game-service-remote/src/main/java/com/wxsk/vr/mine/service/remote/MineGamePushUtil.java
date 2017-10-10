package com.wxsk.vr.mine.service.remote;

import com.wxsk.common.json.JSONResult;
import com.wxsk.common.json.JSONUtil;
import com.wxsk.vr.mine.common.util.CatchKeyFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;
import java.nio.charset.Charset;

public class MineGamePushUtil {
	private static MineGamePushUtil pushUtil = null;
    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;
	private static CloseableHttpClient  closeableHttpClient = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(1000).build()).build();
	private MineGamePushUtil(RedisTemplate<String, String> redisTemplate){
		this.redisTemplate = redisTemplate;
		this.valueOperations = redisTemplate.opsForValue();
	}
    public static final MineGamePushUtil instance(RedisTemplate<String, String> redisTemplate){
    	if(pushUtil == null){
    		pushUtil =  new MineGamePushUtil(redisTemplate);
    	}
    	return pushUtil;
    }

    public final JSONResult pushTouser(Long userId,String msg){
    	String ipandport = valueOperations.get(CatchKeyFactory.getKeyOfWebsoketUserServer(userId));
    	if(StringUtils.isEmpty(ipandport)){
    		return JSONResult.faild("websoket server not found，maybe user are off online");
    	}
    	String serverAddress = "http://".concat(ipandport).concat("/user/wbsocketPush?userId=") + userId;
    	HttpPost httpPost = new HttpPost(serverAddress);
    	httpPost.addHeader("Content-type","application/json; charset=utf-8");
    	httpPost.setHeader("Accept", "application/json");
    	httpPost.setEntity(new StringEntity(msg, Charset.forName("UTF-8")));
    	try {
			HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            if(entity == null){
            	return JSONResult.faild("serverAddress:" + serverAddress + ",return empty");
            }
            String rst = EntityUtils.toString(entity);
            return JSONUtil.getObjectByJsonStr(rst, JSONResult.class);
		} catch (ClientProtocolException e) {
			return JSONResult.faild(e.getMessage());
		} catch (IOException e){
			return JSONResult.faild(e.getMessage());
		}
    }
}
