package com.denzel.Auth.System.registration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.denzel.Auth.System.Repository.RegistrationService;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/registration")
@AllArgsConstructor
@ToString
@EqualsAndHashCode

public class RegistrationController {
    
    private RegistrationService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
        @RequestBody @Valid RegistrationRequest request
    ){
        service.register(request);
        return ResponseEntity.accepted().build();
    }
}
