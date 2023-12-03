package com.tasker.cloudgateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(CorsConfig.class);

    @Bean
    public CorsFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOrigin("http://localhost:3000");
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("OPTIONS");  // Add this line to allow OPTIONS requests
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("Authorization");
        source.registerCorsConfiguration("/**", corsConfig);
        LOGGER.info("Cors Added");
        return new CorsFilter(source);
    }
}
