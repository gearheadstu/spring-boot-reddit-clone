package org.kellyjones.videos.redditclone.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()

                // Allow any request against an actuator endpoint
                .antMatchers("/actuator/**")
                    .permitAll()

                // Allow any request against an auth-related api endpoint
                .antMatchers("/api/auth/**") // FIXME: constant here
                    .permitAll()

                // Allow any request against typical Swagger endpoints
                .antMatchers(
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/configuration/security",
                        "/webjars/**")
                    .permitAll()

                // Any other requests need to be authenticated
                .anyRequest()
                    .authenticated();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
