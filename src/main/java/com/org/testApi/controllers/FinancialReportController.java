package com.org.testApi.controllers;

import com.org.testApi.dto.reports.*;
import com.org.testApi.payload.ResponsePayload;
import com.org.testApi.services.FinancialReportingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/financial-reports")
@Tag(name = "Rapports financiers", description = "API pour générer des rapports financiers pour les groupes de rotation")
public class FinancialReportController {

    @Autowired
    private FinancialReportingService financialReportingService;

    @GetMapping("/groups/{groupId}/member-contributions")
    @Operation(summary = "Historique des contributions des membres", 
               description = "Récupère l'historique des contributions individuelles de tous les membres d'un groupe de rotation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historique des contributions récupéré avec succès",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = MemberContributionHistoryDTO.class))}),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponsePayload<List<MemberContributionHistoryDTO>>> getMemberContributionHistory(
            @Parameter(description = "ID du groupe de rotation") @PathVariable Long groupId) {
        
        try {
            List<MemberContributionHistoryDTO> history = financialReportingService.generateMemberContributionHistory(groupId);
            ResponsePayload<List<MemberContributionHistoryDTO>> response = new ResponsePayload<>();
            response.setSuccess(true);
            response.setMessage("Historique des contributions récupéré avec succès");
            response.setData(history);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponsePayload<List<MemberContributionHistoryDTO>> response = new ResponsePayload<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/groups/{groupId}/performance-metrics")
    @Operation(summary = "Métriques de performance du groupe", 
               description = "Récupère les métriques de performance d'un groupe de rotation")
    @ApiResponses(value = {
       @ApiResponse(responseCode = "200", description = "Métriques de performance récupérées avec succès",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = GroupPerformanceMetricsDTO.class))}),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponsePayload<GroupPerformanceMetricsDTO>> getGroupPerformanceMetrics(
            @Parameter(description = "ID du groupe de rotation") @PathVariable Long groupId) {
        
        try {
            GroupPerformanceMetricsDTO metrics = financialReportingService.generateGroupPerformanceMetrics(groupId);
            ResponsePayload<GroupPerformanceMetricsDTO> response = new ResponsePayload<>();
            response.setSuccess(true);
            response.setMessage("Métriques de performance récupérées avec succès");
            response.setData(metrics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponsePayload<GroupPerformanceMetricsDTO> response = new ResponsePayload<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/groups/{groupId}/member-balances")
    @Operation(summary = "Soldes impayés et pénalités par membre", 
               description = "Récupère les soldes impayés et pénalités pour chaque membre d'un groupe de rotation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Soldes des membres récupérés avec succès",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = MemberUnpaidBalanceDTO.class))}),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponsePayload<List<MemberUnpaidBalanceDTO>>> getMemberUnpaidBalances(
            @Parameter(description = "ID du groupe de rotation") @PathVariable Long groupId) {
        
        try {
            List<MemberUnpaidBalanceDTO> balances = financialReportingService.generateMemberUnpaidBalances(groupId);
            ResponsePayload<List<MemberUnpaidBalanceDTO>> response= new ResponsePayload<>();
            response.setSuccess(true);
            response.setMessage("Soldes des membres récupérés avec succès");
            response.setData(balances);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponsePayload<List<MemberUnpaidBalanceDTO>> response = new ResponsePayload<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/groups/{groupId}/distribution-history")
    @Operation(summary = "Historique des distributions", 
               description = "Récupère l'historique des distributions effectuées dans un groupe de rotation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historique des distributions récupéré avec succès",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = DistributionHistoryDTO.class))}),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponsePayload<List<DistributionHistoryDTO>>> getDistributionHistory(
            @Parameter(description = "ID du groupe de rotation") @PathVariable Long groupId) {
        
        try {
            List<DistributionHistoryDTO> history = financialReportingService.generateDistributionHistory(groupId);
            ResponsePayload<List<DistributionHistoryDTO>> response = new ResponsePayload<>();
            response.setSuccess(true);
            response.setMessage("Historique des distributions récupéré avec succès");
            response.setData(history);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponsePayload<List<DistributionHistoryDTO>> response = new ResponsePayload<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/groups/{groupId}/member-participation")
    @Operation(summary = "Suivi de participation des membres", 
               description = "Récupère le suivi détaillé de la participation des membres, y compris l'historique des participations, des contributions manquées et des paiements en retard")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Suivi de participation récupéré avec succès",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = MemberParticipationTrackingDTO.class))}),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponsePayload<List<MemberParticipationTrackingDTO>>> getMemberParticipationTracking(
            @Parameter(description = "ID du groupe de rotation") @PathVariable Long groupId) {
        
        try {
            List<MemberParticipationTrackingDTO> tracking = financialReportingService.generateMemberParticipationTracking(groupId);
            ResponsePayload<List<MemberParticipationTrackingDTO>> response = new ResponsePayload<>();
            response.setSuccess(true);
            response.setMessage("Suivi de participation récupéré avec succès");
            response.setData(tracking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponsePayload<List<MemberParticipationTrackingDTO>> response = new ResponsePayload<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/groups/{groupId}/full-report")
    @Operation(summary = "Rapport financier complet", 
               description = "Génère un rapport financier complet pour un groupe de rotation incluant toutes les métriques")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rapport financier généré avec succès",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = FinancialReportDTO.class))}),
        @ApiResponse(responseCode = "400", description = "Requête invalide"),
        @ApiResponse(responseCode = "404", description = "Groupe non trouvé"),
        @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<ResponsePayload<FinancialReportDTO>> getFullFinancialReport(
            @Parameter(description = "ID du groupe de rotation") @PathVariable Long groupId) {
        
        try {
            FinancialReportDTO report = new FinancialReportDTO();
            report.setMemberContributions(financialReportingService.generateMemberContributionHistory(groupId));
            report.setGroupMetrics(financialReportingService.generateGroupPerformanceMetrics(groupId));
            report.setMemberBalances(financialReportingService.generateMemberUnpaidBalances(groupId));
            report.setDistributionHistory(financialReportingService.generateDistributionHistory(groupId));
            
            ResponsePayload<FinancialReportDTO> response = new ResponsePayload<>();
            response.setSuccess(true);
            response.setMessage("Rapport financier généré avec succès");
            response.setData(report);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponsePayload<FinancialReportDTO> response = new ResponsePayload<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setData(null);
            return ResponseEntity.badRequest().body(response);
        }
    }
}