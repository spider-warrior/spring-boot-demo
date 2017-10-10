package com.wxsk.vr.mine.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@Service
public class SpringContainerListener implements ApplicationListener<ServletRequestHandledEvent> {

    private static final Logger logger = LogManager.getLogger(SpringContainerListener.class);

    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        if (event != null && event.getStatusCode() == 404) {
            logger.warn("resource not exists for url: {}", event.getRequestUrl());
        }
    }
}
