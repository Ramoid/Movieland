package com.solovey.movieland.service.impl.config;


import com.solovey.movieland.dao.jdbc.config.DataConfig;
import org.springframework.context.annotation.*;


@Configuration
@Import(DataConfig.class)
@ComponentScan(basePackages = {"com.solovey.movieland.service.impl"})
public class ServiceConfig {

}
