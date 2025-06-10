package com.denzel.Auth.System.auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.denzel.Auth.System.Repository.RoleRepository;
import com.denzel.Auth.System.Repository.TokenRepository;
import com.denzel.Auth.System.Repository.UserRepository;
import com.denzel.Auth.System.email.EmailService;
import com.denzel.Auth.System.email.EmailTemplateName;
import com.denzel.Auth.System.user.Token;
import com.denzel.Auth.System.user.User;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private String activationUrl = "http://localhost4200/activate-account" ;

    public void register(RegistrationRequest request) throws MessagingException {
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
        
    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(user.getEmail(), user.fullname(),EmailTemplateName.ACTIVATE_ACCOUNT, activationUrl, newToken, "Acoount_activation");
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
