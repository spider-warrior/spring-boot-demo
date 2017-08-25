package com.wxsk.vr.mine;

import com.wxsk.vr.mine.common.constant.MineConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties
@SpringBootConfiguration
public class MineGame {

    private static final Logger logger = LogManager.getLogger(MineGame.class);
    private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";

    private final Environment env;

    public MineGame(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(MineConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(MineConstants.SPRING_PROFILE_PRODUCTION)) {
            logger.error("You have misConfigured your application! It should not run " +
                    "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(MineConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(MineConstants.SPRING_PROFILE_CLOUD)) {
            logger.error("You have misConfigured your application! It should not " +
                    "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(MineGame.class);
        Map<String, Object> defProperties =  new HashMap<>();
        defProperties.put(SPRING_PROFILE_DEFAULT, MineConstants.SPRING_PROFILE_DEVELOPMENT);
        app.setDefaultProperties(defProperties);
        Environment env = app.run(args).getEnvironment();
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        logger.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}\n\t" +
                        "External: \t{}://{}:{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                env.getProperty("server.port"),
                protocol,
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getActiveProfiles());
    }

}
