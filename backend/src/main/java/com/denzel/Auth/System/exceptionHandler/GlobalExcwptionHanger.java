package com.denzel.Auth.System.exceptionHandler;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.mail.MessagingException;

import static com.denzel.Auth.System.exceptionHandler.BusinessErrorCodes.ACCOUNT_DISABLED;
import static com.denzel.Auth.System.exceptionHandler.BusinessErrorCodes.ACCOUNT_LOCKED;
import static com.denzel.Auth.System.exceptionHandler.BusinessErrorCodes.BAD_CREDENTIALS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@RestControllerAdvice
public class GlobalExcwptionHanger {
    
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exp){
        
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                    .businessErrorCode(ACCOUNT_LOCKED.getCode())
                    .businessExceptionDescription(ACCOUNT_LOCKED.getDescription())
                    .error(exp.getMessage())
                    .build()
            );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exp){
        
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                    .businessErrorCode(ACCOUNT_DISABLED.getCode())
                    .businessExceptionDescription(ACCOUNT_DISABLED.getDescription())
                    .error(exp.getMessage())
                    .build()
            );
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exp){
        
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                    .businessErrorCode(BAD_CREDENTIALS.getCode())
                    .businessExceptionDescription(BAD_CREDENTIALS.getDescription())
                    .error(BAD_CREDENTIALS.getDescription())
                    .build()
            );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException exp){
            
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                    .error(exp.getMessage())
                    .build()
            );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exp){
            
        Set <String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
        });

        return ResponseEntity.status(BAD_REQUEST).body(
                ExceptionResponse.builder()
                    .validationErrors(errors)
                    .build()
            );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exp){
            
        exp.printStackTrace(); //log the exception
    
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                    .businessExceptionDescription("INternal error, contact admin")
                    .error(exp.getMessage())
                    .build()
            );
    }
    
}
