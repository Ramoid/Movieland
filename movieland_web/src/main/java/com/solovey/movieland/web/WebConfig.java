package com.solovey.movieland.web;

import com.solovey.movieland.web.security.cache.TokenCacheConfig;
import com.solovey.movieland.web.util.currency.cache.CurrencyCacheConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableWebMvc
@Import({CurrencyCacheConfig.class, TokenCacheConfig.class})
@ComponentScan("com.solovey.movieland.web")
public class WebConfig extends WebMvcConfigurerAdapter {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor());
    }

}
