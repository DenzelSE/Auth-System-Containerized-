package com.denzel.Auth.System.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {
   
    @NotEmpty(message = "Firstname is mandatory")
    @NotBlank(message = "Firstname is mandatory")
    private String firstname;
    @NotEmpty(message = "lasttname is mandatory")
    @NotBlank(message = "lasttname is mandatory")
    private String lastname;
    @Email(message = "Email is not well formmated")
    @NotEmpty(message = "emailname is mandatory")
    @NotBlank(message = "emailname is mandatory")
    private String email;
    @NotEmpty(message = "password is mandatory")
    @NotBlank(message = "password is mandatory")
    @Size(min = 6, message = "Passwaord should be 6 characters long minimum")
    private String password;

}
