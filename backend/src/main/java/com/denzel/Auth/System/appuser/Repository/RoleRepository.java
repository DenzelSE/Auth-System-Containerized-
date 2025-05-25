package com.denzel.Auth.System.appuser.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.denzel.Auth.System.appuser.role.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
    
    Optional<Role> findByName(String role);
    
}
