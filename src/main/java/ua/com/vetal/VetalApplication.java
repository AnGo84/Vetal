package ua.com.vetal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.context.WebApplicationContext;

@SpringBootApplication
public class VetalApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(VetalApplication.class, args);
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
