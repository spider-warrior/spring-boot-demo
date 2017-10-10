package com.wxsk.vr.mine.ws;

import com.wxsk.cas.client.interceptor.AccessRequiredInteceptor;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.controller.interceptor.UserInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Scope("prototype")
@Component
public class HandShake  implements HandshakeInterceptor{
    private static final Logger logger = LogManager.getLogger(HandShake.class);

    private UserInterceptor userInterceptor;

    public HandShake(UserInterceptor userInterceptor) {
        this.userInterceptor = userInterceptor;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
		ServletServerHttpRequest servletServerHttpRequest = ((ServletServerHttpRequest) request);
		HttpServletRequest httpServletRequest = servletServerHttpRequest.getServletRequest();
//		ServletServerHttpResponse servletServerHttpResponse = ((ServletServerHttpResponse) response);
		httpServletRequest.getRemoteAddr();    //取得客户端的IP
		httpServletRequest.getRemotePort();
        User u = userInterceptor.getCurrentLoginUser(httpServletRequest);
        // 标记用户
        if(u == null){
        	logger.error("non login : " + httpServletRequest.getRequestURL() + "?" + httpServletRequest.getQueryString());
        	return false;
        }
        attributes.put(AccessRequiredInteceptor.CAS_USER_IN_ATTRBUTE_KEY, u);
        attributes.put("ipAndPort", httpServletRequest.getRemoteAddr().concat(":")+httpServletRequest.getRemotePort());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception exception) {

    }
}
