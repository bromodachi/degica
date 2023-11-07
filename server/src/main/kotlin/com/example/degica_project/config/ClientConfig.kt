package com.example.degica_project.config

import feign.Retryer
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class ClientConfig {
    @Bean
    fun retryer(): Retryer = Retryer.Default(100, TimeUnit.SECONDS.toMillis(10L), 5)

    @Bean
    fun errorDecoder(): ErrorDecoder = CustomErrorDecoder()
}
