package com.denzel.Auth.System.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.denzel.Auth.System.Repository.UserRepository;

import jakarta.transaction.Transactional;

public class UserDetailsServiceImpl implements UserDetailsService{

    private UserRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                    .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
    
}
