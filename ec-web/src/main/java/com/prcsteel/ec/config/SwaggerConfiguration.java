package com.prcsteel.ec.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by Rolyer on 2016/4/23.
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
@ComponentScan(basePackages ={"com.prcsteel.ec.controller"})
public class SwaggerConfiguration {
    public static final String DEFAULT_INCLUDE_PATTERN = "/api.*";

    @Value("${swagger.switch}")
    private boolean swaggerSwitch;  // swagger开关

    /**
     * Swagger Springfox configuration.
     */
    @Bean
    public Docket swaggerSpringfoxDocket() {
        if (!swaggerSwitch) return null;
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .paths(regex(DEFAULT_INCLUDE_PATTERN))
                .build();
    }

    /**
     * Api information
     * @return
     */
    private ApiInfo getApiInfo() {
        ApiInfo apiInfo = new ApiInfo("超市2.0 API",
                "超市 V2.0 API",
                "v2.0",
                "",
                "market.prcsteel.com",
                "The Apache License, Version 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0.html"
        );

        return apiInfo;
    }
}
