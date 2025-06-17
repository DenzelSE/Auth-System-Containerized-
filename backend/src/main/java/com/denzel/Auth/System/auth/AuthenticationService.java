package com.denzel.Auth.System.auth;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.denzel.Auth.System.email.EmailService;
import com.denzel.Auth.System.email.EmailTemplateName;
import com.denzel.Auth.System.role.RoleRepository;
import com.denzel.Auth.System.user.Token;
import com.denzel.Auth.System.user.TokenRepository;
import com.denzel.Auth.System.user.User;
import com.denzel.Auth.System.user.UserRepository;
import com.denzel.Auth.System.security.JwtService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService; 
    private String activationUrl = "http://localhost:4200/activate-account" ;

    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
        .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));
        
        var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .accoutnLocked(false)
        .enabled(false)
        .roles(List.of(userRole))
        .build();
        userRepository.save(user);
        sendValidationEmail(user);
        }
        
    private void sendValidationEmail(User user) throws MessagingException {

        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(user.getEmail(), 
                                user.fullname(),
                                EmailTemplateName.ACTIVATE_ACCOUNT, 
                                activationUrl, 
                                newToken, 
                                "Account activation");
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

    public AuthenticationResponse authenticate(AuthenticationResquest request) {
        var auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        ); 
        var claims = new HashMap<String, Object>();
        var user = ((User)auth.getPrincipal());
        claims.put("fullname", user.fullname());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}
