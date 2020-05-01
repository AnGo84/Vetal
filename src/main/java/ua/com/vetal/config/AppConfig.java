package ua.com.vetal.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.SessionScope;
import ua.com.vetal.entity.filter.OrderViewFilter;
import ua.com.vetal.entity.filter.ViewFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public OrderViewFilter filterDataBean() {
        return new OrderViewFilter();
    }

    @Bean
    @SessionScope
    public Map<String, ViewFilter> getViewFilters() {
        return new HashMap<>();
    }
}
