package com.solovey.movieland.service.impl.config;


import com.solovey.movieland.dao.jdbc.config.DataConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Configuration
@Import(DataConfig.class)
@EnableScheduling
@PropertySource("classpath:service.properties")
@ComponentScan(basePackages = {"com.solovey.movieland.service.impl"})
public class ServiceConfig {

    @Value("${thread.pool.size}")
    private int threadPoolSize;

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        return new  ThreadPoolExecutor(0, threadPoolSize,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());

    }

}
