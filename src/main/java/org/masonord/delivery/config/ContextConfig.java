package org.masonord.delivery.config;

import org.masonord.delivery.service.classes.GeoCodingApiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ContextConfig {

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {return new BCryptPasswordEncoder();}
    @Bean
    public static GeoCodingApiService geoCodingApiService() {return new GeoCodingApiService(WebClientConfig.webClientWithTimeout());}
}