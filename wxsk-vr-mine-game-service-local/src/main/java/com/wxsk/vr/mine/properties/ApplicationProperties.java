package com.wxsk.vr.mine.properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.lang.reflect.Array;

@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private static final Logger logger = LogManager.getLogger(ApplicationProperties.class);

    private String vrCoinRedisKey;

    public String getVrCoinRedisKey() {
        return vrCoinRedisKey;
    }

    public void setVrCoinRedisKey(String vrCoinRedisKey) {
        this.vrCoinRedisKey = vrCoinRedisKey;
    }

    @PostConstruct
    public void init() {
        logger.info("vrCoinRedisKey: {}", vrCoinRedisKey);
    }


    public static void main(String[] args) {
        int[] x = {5, 4, 3, 2, 1, 1};
        Object obj = Array.newInstance(double.class, x);
        System.out.println(obj.getClass());
    }
}
