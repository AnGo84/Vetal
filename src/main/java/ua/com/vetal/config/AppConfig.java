package ua.com.vetal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

//@Configuration
public class AppConfig {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }
}
