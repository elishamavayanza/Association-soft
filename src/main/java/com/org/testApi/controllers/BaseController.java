package com.org.testApi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Base", description = "Contrôleur de base pour l'API")
public class BaseController {

    // Contrôleur de base pour l'API

    @Operation(summary = "Point d'entrée de l'API", description = "Point d'entrée principal de l'API de gestion des associations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API accessible avec succès")
    })
    public String home() {
        return "Bienvenue dans l'API de gestion des associations";
    }
}
