package com.org.testApi.controllers;

import com.org.testApi.models.FinancialTransaction;
import com.org.testApi.payload.FinancialTransactionPayload;
import com.org.testApi.services.FinancialTransactionService;
import com.org.testApi.mapper.FinancialTransactionMapper;
import com.org.testApi.services.AssociationService;
import com.org.testApi.services.ActivityService;
import com.org.testApi.services.ProjectService;
import com.org.testApi.services.FinancialCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/financial-transactions")
@Tag(name = "Transaction financière", description = "Gestion des transactions financières")
public class FinancialTransactionController {

    @Autowired
    private FinancialTransactionService financialTransactionService;

    @Autowired
    private FinancialTransactionMapper financialTransactionMapper;

    @Autowired
    private AssociationService associationService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private FinancialCategoryService financialCategoryService;

    @GetMapping
    @Operation(summary= "Récupérertoutes les transactions financières", description = "Retourne une liste de toutes les transactions financières")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des transactions financières récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FinancialTransaction.class))}),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<List<FinancialTransaction>> getAllFinancialTransactions() {
        List<FinancialTransaction> transactions =financialTransactionService.getAllFinancialTransactions();
return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une transaction financière par ID", description = "Retourne une transaction financière spécifique en fonction de son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactionfinancière trouvée",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FinancialTransaction.class))}),
            @ApiResponse(responseCode = "404", description = "Transaction financière non trouvée"),
@ApiResponse(responseCode = "500", description="Erreur interne du serveur")
    })
    public ResponseEntity<FinancialTransaction> getFinancialTransactionById(
            @Parameter(description = "ID de la transaction financière à récupérer") @PathVariable Long id) {
        return financialTransactionService.getFinancialTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle transaction financière", description = "Crée une nouvelle transaction financière avec les données fournies")
    @ApiResponses(value = {
@ApiResponse(responseCode = "200", description = "Transaction financière créée avec succès",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FinancialTransaction.class),
                            examples = @ExampleObject(value = "{\"id\": 1,\"amount\": 150.75,\"transactionDate\": \"2025-10-02\",\"description\": \"Paiement cotisation annuelle\",\"type\": \"INCOME\",\"association\": {\"id\": 1},\"active\": true,\"createdDate\": \"2025-10-02T10:30:00\",\"lastModifiedDate\": \"2025-10-02T10:30:00\"}"))}),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode= "500", description = "Erreur interne du serveur")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Exemple de transaction financière à créer",
        required= true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = FinancialTransaction.class),
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                name = "Exemple de création de transaction",
                summary = "Exemple de création detransaction financière",
                value = "{\n" +
                    "  \"amount\": 150.75,\n" +
                    "  \"transactionDate\": \"2025-10-02\",\n" +
                    "  \"description\": \"Paiement cotisation annuelle\",\n" +
                    "  \"type\":\"INCOME\",\n"+
                    "  \"association\": {\n" +
                    "    \"id\": 1\n" +
                    "  }\n" +
                    "}"
            )
        )
    )
    public ResponseEntity<FinancialTransaction> createFinancialTransaction(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Données de la transaction financière à créer",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FinancialTransaction.class),
                    examples = @ExampleObject(
                        name ="Exemple de création de transaction",
                        summary = "Exemple de création de transaction financière",
                        value = "{\n" +
                            "  \"amount\": 150.75,\n" +
                            "  \"transactionDate\": \"2025-10-02\",\n" +
                           "  \"description\": \"Paiement cotisation annuelle\",\n" +
                            "  \"type\": \"INCOME\",\n" +
                            "  \"association\": {\n" +
                            "    \"id\": 1\n" +
                            "  }\n" +
                            "}"
                    )
                )
)
            @RequestBody FinancialTransaction transaction) {
        FinancialTransaction savedTransaction = financialTransactionService.saveFinancialTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    @PostMapping("/payload")
    @Operation(summary = "Créer une transaction financière à partir d'un payload", description = "Crée une transactionfinancière en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction financière créée avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FinancialTransaction.class),
examples= @ExampleObject(value = "{\"id\": 1,\"amount\": 500.00,\"transactionDate\": \"2025-10-05\",\"description\": \"Achat de matériel informatique\",\"type\": \"EXPENSE\",\"associationId\": 1,\"activityId\": 2,\"projectId\": 3,\"categoryId\": 4,\"active\": true,\"createdDate\": \"2025-10-05T10:30:00\",\"lastModifiedDate\": \"2025-10-05T10:30:00\"}"))}),
            @ApiResponse(responseCode ="400", description = "Donnéesde payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Exemple de payload pour créer une transaction financière",
        required = true,
        content = @Content(
            mediaType= "application/json",
            schema = @Schema(implementation = FinancialTransactionPayload.class),
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                name = "Exemple de payload de création",
                summary = "Exemple de création de transaction via payload",
                value = "{\n" +
                    "  \"amount\": 500.00,\n" +
                    "  \"transactionDate\": \"2025-10-05\",\n" +
                    "  \"description\": \"Achat de matériel informatique\",\n" +
                    "  \"type\": \"EXPENSE\",\n" +
                    "  \"associationId\": 1,\n" +
                    " \"activityId\": 2,\n" +
                    "  \"projectId\": 3,\n" +
                    "  \"categoryId\": 4\n" +
                    "}"
            )
        )
    )
    public ResponseEntity<FinancialTransaction> createFinancialTransactionFromPayload(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Données du payload pour créer la transaction financière",
                required = true,
                content = @Content(
                    mediaType ="application/json",
                    schema = @Schema(implementation = FinancialTransactionPayload.class),
                    examples = @ExampleObject(
                        name = "Exemple de payload decréation",
                        summary = "Exemple de création de transaction via payload",
                        value = "{\n" +
                            "  \"amount\": 500.00,\n" +
                            "  \"transactionDate\": \"2025-10-05\",\n" +
                            " \"description\": \"Achat de matériel informatique\",\n" +
                            "  \"type\": \"EXPENSE\",\n" +
                            "  \"associationId\": 1,\n" +
                            "  \"activityId\": 2,\n" +
                            "  \"projectId\": 3,\n" +
"  \"categoryId\": 4\n" +
                            "}"
                    )
                )
            )
            @RequestBody FinancialTransactionPayload payload) {
        FinancialTransaction transaction = financialTransactionMapper.toEntityFromPayload(payload);
        
        // Set the related entities based on the IDs in the payload
        associationService.getAssociationById(payload.getAssociationId()).ifPresent(transaction::setAssociation);
        if (payload.getActivityId() !=null) {
            activityService.getActivityById(payload.getActivityId()).ifPresent(transaction::setActivity);
        }
        if (payload.getProjectId() != null) {
            projectService.getProjectById(payload.getProjectId()).ifPresent(transaction::setProject);
        }
        if (payload.getCategoryId() != null) {
            financialCategoryService.getFinancialCategoryById(payload.getCategoryId()).ifPresent(transaction::setCategory);
        }
        
        FinancialTransaction savedTransaction = financialTransactionService.saveFinancialTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une transaction financière", description="Met à jour une transaction financière existante avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction financière mise à jour avec succès",
content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation= FinancialTransaction.class),
examples = @ExampleObject(value = "{\"id\": 1,\"amount\": 200.00,\"transactionDate\": \"2025-10-02\",\"description\": \"Paiement cotisation miseà jour\",\"type\": \"INCOME\",\"association\":{\"id\": 1},\"active\": true,\"createdDate\": \"2025-10-02T10:30:00\",\"lastModifiedDate\": \"2025-10-02T11:45:00\"}"))}),
            @ApiResponse(responseCode = "404", description = "Transaction financière non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides"),
            @ApiResponse(responseCode = "500", description ="Erreur interne du serveur")
   })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Exemple de transaction financière à mettre àjour",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = FinancialTransaction.class),
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                name = "Exemple de mise à jour de transaction",
                summary ="Exemple de mise à jour de transaction financière",
                value = "{\n" +
                    "  \"amount\": 200.00,\n" +
                    "  \"transactionDate\":\"2025-10-02\",\n" +
                    "  \"description\": \"Paiement cotisation miseà jour\",\n" +
                    "  \"type\": \"INCOME\",\n" +
                    "  \"association\": {\n" +
                    "    \"id\": 1\n" +
                    "  }\n" +
                    "}"
            )
        )
    )
    public ResponseEntity<FinancialTransaction> updateFinancialTransaction(
            @Parameter(description = "ID de la transaction financière à mettre à jour") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Données de mise à jour de la transaction financière",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FinancialTransaction.class),
                    examples = @ExampleObject(
                       name = "Exemple de miseà jour de transaction",
                        summary = "Exemple de mise à jour de transaction financière",
                        value = "{\n" +
                            "  \"amount\": 200.00,\n" +
                            "  \"transactionDate\": \"2025-10-02\",\n" +
                           "  \"description\": \"Paiement cotisation miseà jour\",\n" +
                            "  \"type\": \"INCOME\",\n" +
                            "  \"association\": {\n" +
                            "    \"id\": 1\n" +
                            "  }\n" +
                            "}"
                    )
                )
            )
            @RequestBody FinancialTransaction transaction) {
       try{
            FinancialTransaction updatedTransaction =financialTransactionService.updateFinancialTransaction(id, transaction);
            return ResponseEntity.ok(updatedTransaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

@PutMapping("/{id}/payload")
   @Operation(summary = "Mettre à jour une transactionfinancière avec payload",description = "Met à jour une transaction financière existante en utilisant un objet payload")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description= "Transaction financière mise à jour avec succès à partir du payload",
                    content = {@Content(mediaType = "application/json",
schema = @Schema(implementation = FinancialTransaction.class),
                            examples = @ExampleObject(value = "{\"id\": 1,\"amount\": 200.00,\"transactionDate\": \"2025-10-02\",\"description\": \"Paiement cotisation mise à jour\",\"type\": \"INCOME\",\"associationId\": 1,\"activityId\": 2,\"projectId\": 3,\"categoryId\": 4,\"active\": true,\"createdDate\": \"2025-10-02T10:30:00\",\"lastModifiedDate\": \"2025-10-02T11:45:00\"}"))}),
            @ApiResponse(responseCode ="404", description = "Transaction financière non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données de payload invalides"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Exemple de payload pour mettre à jour une transaction financière",
        required = true,
        content = @Content(
mediaType = "application/json",
            schema = @Schema(implementation = FinancialTransactionPayload.class),
            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                name = "Exemple de payload de mise à jour",
                summary = "Exemple de mise à jour de transaction via payload",
value = "{\n" +
                    "  \"amount\": 200.00,\n" +
                    "  \"transactionDate\": \"2025-10-02\",\n" +
                    "  \"description\": \"Paiement cotisation mise à jour\",\n"+
                    "  \"type\": \"INCOME\",\n" +
                    "  \"associationId\": 1,\n" +
                    "  \"activityId\": 2,\n" +
                    "  \"projectId\": 3,\n" +
                    "  \"categoryId\": 4\n" +
                    "}"
            )
        )
    )
    public ResponseEntity<FinancialTransaction> updateFinancialTransactionWithPayload(
@Parameter(description = "ID de la transaction financière à mettre à jour") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Données du payload pour mettre à jour la transaction financière",
                required = true,
                content = @Content(
                    mediaType ="application/json",
                    schema = @Schema(implementation = FinancialTransactionPayload.class),
                    examples = @ExampleObject(
                        name = "Exemple de payload de mise à jour",
                        summary = "Exemple de mise à jour de transaction via payload",
                        value = "{\n" +
                            "  \"amount\": 200.00,\n" +
                            " \"transactionDate\": \"2025-10-02\",\n" +
                            "  \"description\": \"Paiement cotisation mise à jour\",\n" +
                            "  \"type\": \"INCOME\",\n" +
                            "  \"associationId\": 1,\n" +
                            "  \"activityId\": 2,\n" +
                            "  \"projectId\": 3,\n" +
                            "  \"categoryId\": 4\n" +
                            "}"
                    )
                )
            )
            @RequestBody FinancialTransactionPayload payload) {
        return financialTransactionService.getFinancialTransactionById(id)
                .map(transaction -> {
                    financialTransactionMapper.updateEntityFromPayload(payload, transaction);
                    
                    // Set the related entities based on the IDs in the payload
                    associationService.getAssociationById(payload.getAssociationId()).ifPresent(transaction::setAssociation);
                    if (payload.getActivityId()!= null) {
                        activityService.getActivityById(payload.getActivityId()).ifPresent(transaction::setActivity);
                    }
                    if (payload.getProjectId() != null) {
                        projectService.getProjectById(payload.getProjectId()).ifPresent(transaction::setProject);
                    }
                    if (payload.getCategoryId() != null) {
                      financialCategoryService.getFinancialCategoryById(payload.getCategoryId()).ifPresent(transaction::setCategory);
                    }
                    
                    FinancialTransaction updatedTransaction = financialTransactionService.updateFinancialTransaction(id, transaction);
                    return ResponseEntity.ok(updatedTransaction);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
@Operation(summary = "Supprimerune transaction financière", description = "Supprime définitivement une transaction financière")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaction financière supprimée avec succès"),
            @ApiResponse(responseCode = "404", description= "Transaction financièrenon trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> deleteFinancialTransaction(
            @Parameter(description = "ID de la transaction financière à supprimer") @PathVariable Long id){
        financialTransactionService.deleteFinancialTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/soft")
    @Operation(summary = "Supprimer logiquementune transaction financière", description = "Marque une transaction financière comme supprimée sans la retirer dela basede données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaction financière supprimée logiquement avec succès"),
            @ApiResponse(responseCode= "404", description = "Transaction financière non trouvée"),
            @ApiResponse(responseCode ="500",description = "Erreur interne du serveur")
    })
    public ResponseEntity<Void> softDeleteFinancialTransaction(
            @Parameter(description = "ID de la transaction financière à supprimerlogiquement") @PathVariable Long id) {
        financialTransactionService.softDeleteFinancialTransaction(id);
        return ResponseEntity.noContent().build();
   }
}