package com.solovey.movieland.dao.jdbc.config;

import javax.sql.DataSource;

import com.solovey.movieland.dao.jdbc.cache.CacheSchedulerConfig;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.IOException;


@Configuration
@Import({QueryConfig.class, CacheSchedulerConfig.class})
@ComponentScan(basePackages = {"com.solovey.movieland.dao.jdbc"})
@PropertySource("classpath:database.properties")
public class DataConfig {

    @Value("${jdbc.driver}")
    private String driverClassName;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Bean
    public BasicDataSource dataSource() throws IOException {

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setInitialSize(5);
        ds.setMaxActive(10);
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }


}
