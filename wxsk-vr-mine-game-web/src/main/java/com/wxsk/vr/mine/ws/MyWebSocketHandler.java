package com.wxsk.vr.mine.ws;

import com.wxsk.common.json.JSONResult;
import com.wxsk.common.json.JSONUtil;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.util.AppContext;
import com.wxsk.vr.mine.common.util.CatchKeyFactory;
import com.wxsk.vr.mine.controller.interceptor.UserInterceptor;
import com.wxsk.vr.mine.model.JsonData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@Component
public class MyWebSocketHandler implements WebSocketHandler,ApplicationContextAware  {
    private static final Logger logger = LogManager.getLogger(MyWebSocketHandler.class);
	public static final Map<Long, WebSocketSession> userSocketSessionMap = new ConcurrentHashMap <Long, WebSocketSession>();
	public static final String SESSION_LAST_ACTIVE_TIME_KEY = "session_last_active_time";
	public static final int SESSION_TIMEOUT_TIMEMILLIS = 1000*60;
	public static final int SESSION_IN_CACHE_TIMEOUT_SECCONDS = 60*60*8;

    @Autowired
	private static ApplicationContext applicationContext; // Spring应用上下文环境

    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;

    public MyWebSocketHandler (RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	User u = (User)session.getAttributes().get(UserInterceptor.CAS_USER_IN_ATTRBUTE_KEY);
        if (userSocketSessionMap.get(u.getId()) == null) {
        	session.getAttributes().put(SESSION_LAST_ACTIVE_TIME_KEY, System.currentTimeMillis());
            userSocketSessionMap.put(u.getId(), session);
            valueOperations.set(CatchKeyFactory.getKeyOfWebsoketUserServer(u.getId()), session.getAttributes().get("ipAndPort").toString(), SESSION_IN_CACHE_TIMEOUT_SECCONDS, TimeUnit.SECONDS);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
    	session.getAttributes().put(SESSION_LAST_ACTIVE_TIME_KEY, System.currentTimeMillis());
    	User user = (User)session.getAttributes().get(UserInterceptor.CAS_USER_IN_ATTRBUTE_KEY);
    	JsonData data = JSONUtil.getObjectByJsonStr(message.getPayload().toString(), JsonData.class);
    	HandlerTemplate handler = HandlerTemplate.handlerMap.get(data.getOpt());
    	if(handler != null){
    		session.sendMessage(new TextMessage(handler.getAnswer(data, user).addValue("opt", data.getOpt()).jsonStr()));
    		return;
    	}
		RequestMappingHandlerMapping rmhp = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        for(RequestMappingInfo info : map.keySet()){
            String mappingUrl = info.getPatternsCondition().toString().replaceFirst("\\[","").replaceFirst("\\]","");
            if(mappingUrl.equals(data.getOpt())){
            	Object bean = map.get(info).getBean();
        		AppContext.initRequestContext(user,new Date());
        		JSONResult rst = (JSONResult) map.get(info).getMethod().invoke(applicationContext.getBean(bean.toString()));
        		rst.addValue("opt", data.getOpt());
            	session.sendMessage(new TextMessage(rst.jsonStr()));
        		AppContext.clearRequestContext();
            	return;
            }
        }
        logger.error("handleMessage error Opt:" + data.getOpt());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    	User u = (User)session.getAttributes().get(UserInterceptor.CAS_USER_IN_ATTRBUTE_KEY);
        session.close();
        userSocketSessionMap.remove(u.getId());
        redisTemplate.delete(CatchKeyFactory.getKeyOfWebsoketUserServer(u.getId()));
        logger.info("handleTransportError Socket会话已经移除:用户ID" + u.getId() +",错误："+ exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    	User u = (User)session.getAttributes().get(UserInterceptor.CAS_USER_IN_ATTRBUTE_KEY);
        Iterator<Entry<Long, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
        session.close();
        userSocketSessionMap.remove(u.getId());
        redisTemplate.delete(CatchKeyFactory.getKeyOfWebsoketUserServer(u.getId()));
        logger.info("Websocket:" + u.getId() + "已经关闭当前连接数：" + userSocketSessionMap.size());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 群发
     */
    public void broadcast(final TextMessage message) throws IOException {
        Iterator<Entry<Long, WebSocketSession>> it = userSocketSessionMap
                .entrySet().iterator();
        while (it.hasNext()) {
        	it.next().getValue().sendMessage(message);
        }
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		  this.applicationContext = applicationContext;
	}

	@Component
	private class TaskJob {
		@Scheduled(fixedRate = 1000*10)
	    public void job1() {
			Long now =  System.currentTimeMillis();
			for (Entry<Long, WebSocketSession> entry : userSocketSessionMap.entrySet()) {
				if(entry.getValue().getAttributes().get(SESSION_LAST_ACTIVE_TIME_KEY) == null){
					entry.getValue().getAttributes().put(SESSION_LAST_ACTIVE_TIME_KEY, System.currentTimeMillis());
					continue;
				}
				Long last_active_time = Long.valueOf(entry.getValue().getAttributes().get(SESSION_LAST_ACTIVE_TIME_KEY).toString());
				if(now - last_active_time > SESSION_TIMEOUT_TIMEMILLIS){
					if(entry.getValue().isOpen()){
						try {
							entry.getValue().close();
						} catch (IOException e) {
							//e.printStackTrace();
						}
					}
					User u = (User)entry.getValue().getAttributes().get(UserInterceptor.CAS_USER_IN_ATTRBUTE_KEY);
					userSocketSessionMap.remove(entry.getKey());
                    redisTemplate.delete(CatchKeyFactory.getKeyOfWebsoketUserServer(u.getId()));
				}
	        }
			logger.info("当前共持有连接数：" + userSocketSessionMap.size());
	    }
	}

	/**
	 * 向用户推送消息
	 * @param userId
	 * @param msg
	 * @return
	 */
	public static JSONResult pushToUser(Long userId,String msg){
		WebSocketSession session = userSocketSessionMap.get(userId);
		if(session == null || !session.isOpen()){
			return JSONResult.faild("连接已关闭");
		}
		try {
			session.sendMessage(new TextMessage(msg));
		} catch (IOException e) {
			logger.error(e);
			return JSONResult.faild("连接已关闭");
		}
		return JSONResult.success();
	}

	public static void main(String[] args) {
		System.out.println("[/test/test2]".replaceFirst("\\[", "").replaceFirst("\\]",""));
//		for(int i = 0; i < 300; i++){
//			System.out.println("del lottery_log_code_key_" + i);
//		}

		Map map = new HashMap();
		map.put("aaa", "bbb");
		map.put("ccc", "ddd");
		JsonData jd = new JsonData();
		jd.setData(map);
		jd.setOpt("/test/ttt");
		System.out.println(JSONUtil.getJsonStr(jd));
		String data = "{\"aaa\":\"bbb\",\"ccc\":\"ddd\"}";
	}
}
