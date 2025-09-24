package com.org.testApi.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserPayload {
    private Long id;
    
    @NotBlank(message = "Le nom d'utilisateur ne peut pas être vide")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
    private String username;
    
    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "L'email doit être une adresse valide")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    private String email;
    
    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;
    
    private Boolean enabled;
    private Long roleId;
    
    @Size(max = 50, message = "Le prénom ne doit pas dépasser 50 caractères")
    private String firstName;
    
    @Size(max = 50, message = "Le nom de famille ne doit pas dépasser 50 caractères")
    private String lastName;
    
    @Size(max = 20, message = "Le numéro de téléphone ne doit pas dépasser 20 caractères")
    private String phoneNumber;
}