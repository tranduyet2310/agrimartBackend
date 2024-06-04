package com.example.agriecommerce.config;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

public class SwaggerConfiguration implements ApplicationListener<ApplicationPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();
        Properties props = new Properties();
        props.put("springdoc.swagger-ui.path", swaggerPath());
        environment.getPropertySources()
                .addFirst(new PropertiesPropertySource("programmatically", props));
    }

    private String swaggerPath() {
        return "/api/swagger";
    }
}
