package com.solovey.movieland.web;

import com.solovey.movieland.web.util.currency.cache.CurrencyCacheConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebMvc
@Import({CurrencyCacheConfig.class})
@ComponentScan("com.solovey.movieland.web")
public class WebConfig  {

}
