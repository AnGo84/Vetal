package ua.com.vetal.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
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

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/validation/messages", "classpath:/messages/i18n/messages");
        // messageSource.setBasename();
        // messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /*
     * @Bean(name = "localeResolver")
     * public LocaleResolver getLocaleResolver()
     * { CookieLocaleResolver resolver = new CookieLocaleResolver();
     * resolver.setCookieDomain("vetalAppLocaleCookie");
     * resolver.setDefaultLocale(new Locale("ru")); // 60 minutes
     * resolver.setCookieMaxAge(60 * 60); return resolver; }
     */

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());

        return bean;
    }

}
