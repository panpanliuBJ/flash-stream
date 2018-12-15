package com.finley.flash.stream.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@Configuration
public class MybatisConfiguration {

    public static final int DEFAULT_STATEMENT_TIMEOUT = 10000;
    public static final int DEFAULT_FETCH_SIZE = 100;

    @Primary
    @Bean
    public SqlSessionFactory flashSqlSessionFactory(DataSource flashDataSource) throws Exception {
        return buildSqlSessionFactory(flashDataSource);
    }

    @Bean
    @Primary
    public MapperScannerConfigurer flashMapperScannerConfigurer() {
        MapperScannerConfigurer mapperScanner = new MapperScannerConfigurer();
        mapperScanner.setBasePackage("com.finley.flash.stream.dao");
//        mapperScanner.setSqlSessionFactoryBeanName(Beans.FLASH_SQL_SESSION_FACTORY);
        return mapperScanner;
    }


    @Bean
    public ObjectMapper jacksonObjectMapper() {
        return new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    protected SqlSessionFactory buildSqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setDefaultFetchSize(DEFAULT_FETCH_SIZE);
        configuration.setDefaultStatementTimeout(DEFAULT_STATEMENT_TIMEOUT);
        sessionFactory.setConfiguration(configuration);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mybatis/*.xml"));
        sessionFactory.setTypeAliasesPackage("com.finley.flash.stream.domain");
        return sessionFactory.getObject();
    }
}
