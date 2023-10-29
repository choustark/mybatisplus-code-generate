package com.fengwenyi.codegenerator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author chou
 * @since 2021-12-20
 */
@Data
@Configuration
@ConfigurationProperties("chou")
public class ErwinProperties {

    private App app;

    @Data
    public static class App {
        private String name;
        private String version;
    }

}
