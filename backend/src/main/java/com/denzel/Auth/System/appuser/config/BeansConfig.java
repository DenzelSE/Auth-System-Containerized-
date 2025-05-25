package com.denzel.Auth.System.appuser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BeansConfig {
    
    private final UserDetailsService setUserDetailsService;

    @Bean
    private AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(setUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;

    }

    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
