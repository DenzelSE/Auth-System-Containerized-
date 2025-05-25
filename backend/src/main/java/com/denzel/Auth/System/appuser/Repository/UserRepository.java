package com.denzel.Auth.System.appuser.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.denzel.Auth.System.appuser.user.User;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer>{
    
    Optional<User> findByEmail(String email);
}
