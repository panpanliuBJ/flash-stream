package com.finley.flash.stream.configuration;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
public class DataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("flash.datasource")
    public DataSourceProperties flashDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean
    @Primary
    public DataSource flashDataSource() {
        return flashDataSourceProperties().initializeDataSourceBuilder().build();
    }

}
