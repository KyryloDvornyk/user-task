package com.user.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import static java.util.Objects.*;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {
        "com.user.task"
})
public class AppConfig {
    private static final Integer BASE_REGISTER_AGE = 0;

    private final Environment environment;

    public AppConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public Integer getRegisterAge() {
        String registerAge = environment.getProperty("register.age");
        if (nonNull(registerAge)) {
            return Integer.valueOf(registerAge);
        }
        return BASE_REGISTER_AGE;
    }
}
