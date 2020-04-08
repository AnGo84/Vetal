package ua.com.vetal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import ua.com.vetal.service.AppUserDetailsServiceImpl;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AppUserDetailsServiceImpl appUserDetailsService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // Setting Service to find User in the database.
        // And Setting PassswordEncoder
        auth.userDetailsService(appUserDetailsService).passwordEncoder(passwordEncoder());

        // auth.inMemoryAuthentication().withUser("user").password("user").roles("USER");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        // The pages does not require login
        http.authorizeRequests()
                .antMatchers("/", "/main", "/login", "/logout", "/forgotPassword", "/resetPassword",
                        "/manager", "/clients", "/contractor", "/productions", "/printer", "/worker",
                        "/paper", "/chromaticity", "/laminate", "/cringle", "/format", "/stock", "/numberBases",
                        "/printingUnit", "/productionType",
                        "/tasks/view**", "/tasks/clearFilter", "/tasks/Filter", "/tasks",
                        "/stencils/clearFilter", "/stencils/Filter", "/stencils", "/stencils/view**",
                        "/statistic/clearFilter", "/statistic/Filter", "/statistic",
                        "/folder**", "/file**", "/catalog**")
                .permitAll();

        // /userInfo page requires login as ROLE_USER or ROLE_ADMIN.
        // If no login, it will redirect to /login page.
        // ROLE_ACCOUNTANT ROLE_ADMIN ROLE_MANAGER
        http.authorizeRequests().antMatchers("/user/**").authenticated();
        http.authorizeRequests().antMatchers("/userInfo").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/manager/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/printer/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/worker/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/clients/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/contractor/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/productions/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/productionType/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/paper/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/printingUnit/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/chromaticity/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/laminate/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/cringle/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/format/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/stock/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/numberBases/**").access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/tasks/add", "/tasks/update", "/tasks/edit**", "/tasks/delete**", "/tasks/sendEmail**")
                .access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/stencils/add", "/stencils/update", "/stencils/edit**", "/stencils/delete**")
                .access("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN')");

        // For ADMIN only.
        http.authorizeRequests().antMatchers("/admin").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/users").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/users/**").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/links").access("hasRole('ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/links/**").access("hasRole('ROLE_ADMIN')");

        // When the user has logged in as XX.
        // But access a page that requires role YY,
        // AccessDeniedException will be thrown.
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        // Config for Login Form
        http.authorizeRequests().and().formLogin()//
                // Submit URL of login page.
                .loginProcessingUrl("/j_spring_security_check") // Submit URL
                .loginPage("/login")//
                .defaultSuccessUrl("/main")// /userInfo
                .failureUrl("/login?error=true")//
                .usernameParameter("username")//
                .passwordParameter("password")
                // Config for Logout Page
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login");
        //logoutSuccessUrl("/logoutSuccessful");

        // Config Remember Me.
        http.authorizeRequests().and() //
                .rememberMe()//
                .rememberMeCookieName("vetal-remember-me")//
                .tokenRepository(this.persistentTokenRepository()) //
                .tokenValiditySeconds(1 * 24 * 60 * 60); // 24h

    }

    // Token stored in Table (Persistent_Logins)
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

    // Token stored in Memory (Of Web Server).
    // @Bean
    // public PersistentTokenRepository persistentTokenRepository() {
    // InMemoryTokenRepositoryImpl memory = new InMemoryTokenRepositoryImpl();
    // return memory;
    // }

}
