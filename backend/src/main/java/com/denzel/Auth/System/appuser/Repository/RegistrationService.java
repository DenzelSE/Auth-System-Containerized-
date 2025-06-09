package com.denzel.Auth.System.appuser.Repository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.denzel.Auth.System.appuser.user.Token;
import com.denzel.Auth.System.appuser.user.User;
import com.denzel.Auth.System.registration.RegistrationRequest;
import com.denzel.Auth.System.security.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationService {
private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public void register(RegistrationRequest request) {
        var userRole = roleRepository.findByName("USER")
        .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));
        
        var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password("")
        .accoutnLocked(false)
        .enabled(false)
        .roles(List.of(userRole))
        .build();
        userRepository.save(user);
        sendValidationEmail(user);
        }
        
    private void sendValidationEmail(User user) {
        var newToken = generateAndSaveActivationToken(user);
        // send email 
        }
    
    private String generateAndSaveActivationToken(User user){
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                        .token(generatedToken)
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusMinutes(15))
                        .user(user)
                        .build();
            tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length){
        
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();

        for(int i =0; i < length; i++){
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));

        }
        
        return codeBuilder.toString();
    }
}
