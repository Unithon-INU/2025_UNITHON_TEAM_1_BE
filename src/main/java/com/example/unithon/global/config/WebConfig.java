package com.example.unithon.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:8080",
                        "https://43.203.125.32:8080",
                        "https://unithon1.shop",
                        "https://unithon-inu.github.io/2025_UNITHON_TEAM_1_FE"
                )

                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")

                .allowedHeaders(
                        "Origin",
                        "Content-Type",
                        "Accept",
                        "Authorization",
                        "X-Requested-With",
                        "Cache-Control"
                )

                .exposedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
