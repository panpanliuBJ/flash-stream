package com.finley.flash.stream.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DataSourceConfiguration.class, MybatisConfiguration.class})
@ComponentScan("com.finley.flash.stream.*")
public class FlashConfiguration {

}
