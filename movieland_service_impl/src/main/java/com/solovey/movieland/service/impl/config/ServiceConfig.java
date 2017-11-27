package com.solovey.movieland.service.impl.config;


import com.solovey.movieland.dao.jdbc.config.DataConfig;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@Import(DataConfig.class)
@EnableScheduling
@PropertySource("classpath:rate.service.properties")
@ComponentScan(basePackages = {"com.solovey.movieland.service.impl"})
public class ServiceConfig {

}
