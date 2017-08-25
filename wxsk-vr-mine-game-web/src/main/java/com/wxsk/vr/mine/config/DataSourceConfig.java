package com.wxsk.vr.mine.config;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    private final MongoProperties mongoProperties;

    public DataSourceConfig(MongoProperties mongoProperties) {
        this.mongoProperties = mongoProperties;
    }

}
