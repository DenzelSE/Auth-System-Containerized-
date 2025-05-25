package com.denzel.Auth.System.appuser.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.qos.logback.core.subst.Token;

public interface TokenRepository  extends JpaRepository<Token, Integer>{

    Optional<Token> findByToken(String token);
    
}
