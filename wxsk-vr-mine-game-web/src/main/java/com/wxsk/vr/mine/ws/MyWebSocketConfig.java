package com.wxsk.vr.mine.ws;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;  

@Component  
@EnableWebMvc  
@EnableWebSocket  
public class MyWebSocketConfig  extends WebMvcConfigurerAdapter implements WebSocketConfigurer{
	@Resource  
    MyWebSocketHandler handler;  
	@Autowired
	HandShake HandShake;
    @Override  
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {  
        registry.addHandler(handler, "/wx_handshake").addInterceptors(HandShake).setAllowedOrigins("*");
        registry.addHandler(handler, "/wx_handshake/sockjs").setAllowedOrigins("*").addInterceptors(new HandShake()).withSockJS();  
    }  
}
