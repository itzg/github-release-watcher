package me.itzg.ghrelwatcher.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Geoff Bourne
 * @since Jun 2018
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/status").permitAll()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/**").permitAll()
        .and().csrf().disable()
        .oauth2Login().loginPage("/")
        .and().logout().logoutSuccessHandler((req, resp, authentication) -> {
            // override the default URL redirect on logout
            resp.setStatus(HttpStatus.OK.value());
        })
        ;
    }
}
