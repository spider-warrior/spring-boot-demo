package com.wxsk.vr.mine.config;

import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.wxsk.common.redis.StringRedisClusterUtil;
import com.wxsk.vr.mine.config.properties.DubboProperties;
import com.wxsk.vr.mine.controller.aspect.ControllerAspect;
import com.wxsk.vr.mine.controller.interceptor.UserInterceptor;
import com.wxsk.vr.mine.controller.json.GameObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import redis.clients.jedis.JedisCluster;

import java.text.SimpleDateFormat;

@ComponentScan(
        basePackages = "com.wxsk.vr.mine"
)
@EnableAspectJAutoProxy
@EnableConfigurationProperties(value = {GameConfig.class, ApplicationConfig.class, MongoProperties.class, DubboProperties.class})
@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private DubboProperties dubboProperties;
    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        registry.addResourceHandler("/**/*.html").addResourceLocations("/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor);
    }

    @Bean
    public JedisCluster jedisCluster(RedisConnectionFactory redisConnectionFactory) {
        return (JedisCluster)redisConnectionFactory.getClusterConnection().getNativeConnection();
    }

    @Bean
    public StringRedisClusterUtil stringRedisClusterUtil(JedisCluster jedisCluster) {
        StringRedisClusterUtil stringRedisClusterUtil = new StringRedisClusterUtil();
        stringRedisClusterUtil.setJedisCluster(jedisCluster);
        return stringRedisClusterUtil;
    }

    @Bean
    public UserInterceptor userInterceptor(JedisCluster jedisCluster) {
        StringRedisClusterUtil stringRedisClusterUtil = new StringRedisClusterUtil();
        stringRedisClusterUtil.setJedisCluster(jedisCluster);
        UserInterceptor userInterceptor = new UserInterceptor();
        userInterceptor.setStringRedisClusterUtil(stringRedisClusterUtil);
        return userInterceptor;
    }

    @Bean
    public ControllerAspect loggingAspect() {
        return new ControllerAspect();
    }

    @Bean
    public GameObjectMapper gameObjectMapper() {
        GameObjectMapper mapper = new GameObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return mapper;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(10485760); //上传文件为10MB
        return resolver;
    }

    /*与<dubbo:annotation/>相当.提供方扫描带有@com.alibaba.dubbo.config.annotation.Service的注解类*/
    @Bean
    public static AnnotationBean annotationBean() {
        AnnotationBean annotationBean = new AnnotationBean();
        annotationBean.setPackage("com.wxsk.vr.mine");//所以含有@com.alibaba.dubbo.config.annotation.Service的注解类都应在此包中,多个包名可以使用英文逗号分隔.
        return annotationBean;
    }

    /*与<dubbo:application/>相当.*/
    @Bean
    public com.alibaba.dubbo.config.ApplicationConfig applicationConfig() {
        com.alibaba.dubbo.config.ApplicationConfig applicationConfig = new com.alibaba.dubbo.config.ApplicationConfig();
        applicationConfig.setLogger("slf4j");
        applicationConfig.setName(dubboProperties.getApplicationName());
        return applicationConfig;
    }

    @Bean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setDefault(true);
        consumerConfig.setCheck(false);
        return consumerConfig;
    }

    /*与<dubbo:registry/>相当*/
    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(dubboProperties.getRegistryAddress());
        return registryConfig;
    }

    /*与<dubbo:protocol/>相当*/
    @Bean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig("dubbo", dubboProperties.getPort());
        protocolConfig.setSerialization("java");//默认为hessian2,但不支持无参构造函数类,而这种方式的效率很低
        return protocolConfig;
    }

}
