package com.denzel.Auth.System.security.config;

import org.hibernate.jdbc.Expectations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.denzel.Auth.System.appuser.Services.AppUserService;
import static org.springframework.security.config.Customizer.withDefaults;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableWebSecurity

public class WebSecurityConfig {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/v*/registration/**")
            .permitAll()
            .anyRequest().authenticated()
            )
            .formLogin(withDefaults())
            .httpBasic(withDefaults());

            return http.build();
    }

    @Bean 
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, DaoAuthenticationProvider authProvider)
                throws Exception{
                    return http.getSharedObject(AuthenticationManagerBuilder.class)
                                .authenticationProvider(authProvider)
                                .build();
                }


}
