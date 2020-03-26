package com.hui.sessionredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400*30)
@SpringBootApplication
public class SessionredisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SessionredisApplication.class, args);
    }

}
