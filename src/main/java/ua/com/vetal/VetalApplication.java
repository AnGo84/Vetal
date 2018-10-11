package ua.com.vetal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.WebApplicationContext;

@SpringBootApplication
// @EnableAutoConfiguration
public class VetalApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(VetalApplication.class, args);
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
     * @Bean(name = "localeResolver") public LocaleResolver getLocaleResolver()
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

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(VetalApplication.class);
    }


    @Override
    protected WebApplicationContext run(SpringApplication application) {
        return super.run(application);
    }
}
