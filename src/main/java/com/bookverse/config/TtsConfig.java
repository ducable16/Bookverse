package com.bookverse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TtsConfig {
    @Bean
    RestClient ttsRestClient() {
        return RestClient.builder()
                .baseUrl("http://127.0.0.1:7861")
                .build();
    }
}

