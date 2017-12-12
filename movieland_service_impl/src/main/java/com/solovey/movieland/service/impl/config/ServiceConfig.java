package com.solovey.movieland.service.impl.config;


import com.solovey.movieland.dao.jdbc.config.DataConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.*;


@Configuration
@Import(DataConfig.class)
@EnableScheduling
@PropertySource("classpath:service.properties")
@ComponentScan(basePackages = {"com.solovey.movieland.service.impl"})
public class ServiceConfig {

    @Value("${thread.pool.size}")
    private int threadPoolSize;

    @Bean
    @Qualifier("cachedThreadPoolExecutor")
    public ExecutorService cachedThreadPoolExecutor() {
        return new ThreadPoolExecutor(0, threadPoolSize,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());

    }

    @Bean
    @Qualifier("singleThreadPoolExecutor")
    public ExecutorService singleThreadPoolExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public Properties emailAppenderProperties() {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("email.sender.properties");) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }

}
