package ua.com.vetal.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ua.com.vetal.entity.filter.FilterData;

@Configuration
public class AppConfig {
    /*    @Bean
        @Primary
        @ConfigurationProperties(prefix = "spring.datasource")
        public DataSource primaryDataSource() {
            return DataSourceBuilder.create().build();
        }*/

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FilterData filterDataBean() {
        return new FilterData();
    }
}
