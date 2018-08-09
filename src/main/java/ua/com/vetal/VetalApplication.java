package ua.com.vetal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
// @EnableAutoConfiguration
public class VetalApplication {

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:/messages/validation/messages", "classpath:/messages/i18n/messages");
		// messageSource.setBasename();

		// messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource());

		return bean;
	}

	/*
	 * @Bean(name = "localeResolver") public LocaleResolver getLocaleResolver()
	 * { CookieLocaleResolver resolver = new CookieLocaleResolver();
	 * resolver.setCookieDomain("vetalAppLocaleCookie");
	 * resolver.setDefaultLocale(new Locale("ru")); // 60 minutes
	 * resolver.setCookieMaxAge(60 * 60); return resolver; }
	 */

	public static void main(String[] args) {
		SpringApplication.run(VetalApplication.class, args);
	}

}
