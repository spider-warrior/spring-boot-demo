package com.wxsk.vr.mine.config.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@ConfigurationProperties(prefix = "application.dubbo")
public class DubboProperties {

    /**
     * applicationName
     * */
    private String applicationName;

    /**
     * registryAddress
     * */
    private String registryAddress;

    /**
     * port
     * */
    private int port;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println();
    }
}
