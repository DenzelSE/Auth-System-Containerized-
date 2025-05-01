package com.denzel.Auth.System.appuser.Repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.denzel.Auth.System.appuser.AppUser;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository {
    
    Optional<AppUser> findByEmail(String email);
}
