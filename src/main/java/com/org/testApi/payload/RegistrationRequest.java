package com.org.testApi.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Objet de requête pour l'inscription d'un utilisateur")
@Getter
@Setter
public class RegistrationRequest {
    @Schema(description = "Nom d'utilisateur unique", example = "elishamavayanza")
    private String username;

    @Schema(description = "Adresse email unique", example = "elishama.vayanza@example.com")
    private String email;

    @Schema(description = "Mot de passe de l'utilisateur", example = "motdepasse123")
    private String password;

    @Schema(description = "Prénom de l'utilisateur", example = "Elishama")
    private String firstName;

    @Schema(description = "Nom de famille de l'utilisateur", example = "VAYANZA")
    private String lastName;

    @Schema(description = "Numéro de téléphone de l'utilisateur", example = "+234991471988")
    private String phoneNumber;
}
