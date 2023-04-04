package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

@Validated
@PropertySource(value = "classpath:application.properties")
@ConfigurationProperties(prefix = "swagger", ignoreInvalidFields = true)
@ComponentScan("ru/tinkoff/edu/java/scrapper/dto")
public class ApplicationConfig {

}