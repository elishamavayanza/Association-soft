package com.org.testApi.config;

import com.org.testApi.models.LoanType;
import com.org.testApi.models.LoanPenaltyConfig;
import com.org.testApi.repository.LoanTypeRepository;
import com.org.testApi.repository.LoanPenaltyConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class LoanTypeDataInitializer implements CommandLineRunner {

    @Autowired
    private LoanTypeRepository loanTypeRepository;

    @Autowired
    private LoanPenaltyConfigRepository loanPenaltyConfigRepository;

    @Override
    public void run(String... args) throws Exception {
        // Vérifier si les types de prêts existent déjà
        if (loanTypeRepository.count() == 0) {
            // Créer les types de prêts par destination des fonds
            createEquipmentLoanType();
            createOperatingLoanType();
            createSpecificProjectLoanType();
            createRealEstateLoanType();
            createEmergencyLoanType();
            createTreasuryLoanType();
            
            // Créer les types de prêts par source de financement
            createInternalLoanType();
            createPartnerBankLoanType();
            createInstitutionalLoanType();
            createDonorLoanType();
            
            // Créer les types de prêts par durée associative
            createShortTermLoanType();
            createMediumTermLoanType();
            createLongTermLoanType();
        }
    }

    private void createEquipmentLoanType() {
        LoanType equipmentLoan = LoanType.builder()
                .name("Prêt Équipement")
                .description("Prêt pour l'achat d'équipements")
                .monthlyInterestRate(new BigDecimal("0.01"))
                .maxPenaltyRate(new BigDecimal("0.50"))
                .gracePeriodDays(30)
                .category("DESTINATION_FUNDS")
                .subCategory("Équipement")
                .active(true)
                .build();
        
        equipmentLoan = loanTypeRepository.save(equipmentLoan);
        
        // Créer la configuration de pénalité associée
        LoanPenaltyConfig penaltyConfig = LoanPenaltyConfig.builder()
                .loanType(equipmentLoan)
                .penaltyDelayDays(30)
                .dailyPenaltyRate(new BigDecimal("0.001"))
                .maxPenaltyRate(new BigDecimal("0.50"))
                .progressivePenalty(true)
                .calculationIntervalDays(1)
                .reminderMessage("Rappel: Votre prêt équipement est en retard de paiement")
                .build();
        
        loanPenaltyConfigRepository.save(penaltyConfig);
    }

    private void createOperatingLoanType() {
        LoanType operatingLoan = LoanType.builder()
                .name("Prêt Fonctionnement")
                .description("Prêt pour les besoins de fonctionnement")
                .monthlyInterestRate(new BigDecimal("0.005"))
                .maxPenaltyRate(new BigDecimal("0.30"))
                .gracePeriodDays(45)
                .category("DESTINATION_FUNDS")
                .subCategory("Fonctionnement")
                .active(true)
                .build();
        
        operatingLoan = loanTypeRepository.save(operatingLoan);
        
        // Créer la configuration de pénalité associée
        LoanPenaltyConfig penaltyConfig = LoanPenaltyConfig.builder()
                .loanType(operatingLoan)
                .penaltyDelayDays(45)
                .dailyPenaltyRate(new BigDecimal("0.0005"))
                .maxPenaltyRate(new BigDecimal("0.30"))
                .progressivePenalty(true)
                .calculationIntervalDays(1)
                .reminderMessage("Rappel: Votre prêt fonctionnement est en retard de paiement")
                .build();
        
        loanPenaltyConfigRepository.save(penaltyConfig);
    }

    private void createSpecificProjectLoanType() {
        LoanType projectLoan = LoanType.builder()
                .name("Prêt Projet Spécifique")
                .description("Prêt pour un projet spécifique")
                .monthlyInterestRate(new BigDecimal("0.0075"))
                .maxPenaltyRate(new BigDecimal("0.75"))
                .gracePeriodDays(60)
                .category("DESTINATION_FUNDS")
                .subCategory("Projet Spécifique")
                .active(true)
                .build();
        
        projectLoan = loanTypeRepository.save(projectLoan);
        
        // Créer la configuration de pénalité associée
        LoanPenaltyConfig penaltyConfig = LoanPenaltyConfig.builder()
                .loanType(projectLoan)
                .penaltyDelayDays(60)
                .dailyPenaltyRate(new BigDecimal("0.00075"))
                .maxPenaltyRate(new BigDecimal("0.75"))
                .progressivePenalty(true)
                .calculationIntervalDays(1)
                .reminderMessage("Rappel: Votre prêt projet spécifique est en retard de paiement")
                .build();
        
        loanPenaltyConfigRepository.save(penaltyConfig);
    }

    private void createRealEstateLoanType() {
        LoanType realEstateLoan = LoanType.builder()
                .name("Prêt Immobilier")
                .description("Prêt pour l'achat de biens immobiliers")
                .monthlyInterestRate(new BigDecimal("0.02"))
                .maxPenaltyRate(new BigDecimal("1.00"))
                .gracePeriodDays(15)
                .category("DESTINATION_FUNDS")
                .subCategory("Immobilier")
                .active(true)
                .build();
        
        realEstateLoan = loanTypeRepository.save(realEstateLoan);
        
        // Créer la configuration de pénalité associée
        LoanPenaltyConfig penaltyConfig = LoanPenaltyConfig.builder()
                .loanType(realEstateLoan)
                .penaltyDelayDays(15)
                .dailyPenaltyRate(new BigDecimal("0.002"))
                .maxPenaltyRate(new BigDecimal("1.00"))
                .progressivePenalty(true)
                .calculationIntervalDays(1)
                .reminderMessage("Rappel: Votre prêt immobilier est en retard de paiement")
                .build();
        
        loanPenaltyConfigRepository.save(penaltyConfig);
    }

    private void createEmergencyLoanType() {
        LoanType emergencyLoan = LoanType.builder()
                .name("Prêt Urgence")
                .description("Prêt pour les situations d'urgence")
                .monthlyInterestRate(BigDecimal.ZERO)
                .maxPenaltyRate(BigDecimal.ZERO)
                .gracePeriodDays(90)
                .category("DESTINATION_FUNDS")
                .subCategory("Urgence")
                .active(true)
                .build();
        
        emergencyLoan = loanTypeRepository.save(emergencyLoan);
        
        // Créer la configuration de pénalité associée (aucune pénalité)
        LoanPenaltyConfig penaltyConfig = LoanPenaltyConfig.builder()
                .loanType(emergencyLoan)
                .penaltyDelayDays(90)
                .dailyPenaltyRate(BigDecimal.ZERO)
                .maxPenaltyRate(BigDecimal.ZERO)
                .progressivePenalty(false)
                .calculationIntervalDays(1)
                .reminderMessage("Information: Votre prêt urgence approche sa date d'échéance")
                .build();
        
        loanPenaltyConfigRepository.save(penaltyConfig);
    }

    private void createTreasuryLoanType() {
        LoanType treasuryLoan = LoanType.builder()
                .name("Prêt Trésorerie")
                .description("Prêt pour les besoins de trésorerie")
                .monthlyInterestRate(new BigDecimal("0.015"))
                .maxPenaltyRate(new BigDecimal("2.00"))
                .gracePeriodDays(20)
                .category("DESTINATION_FUNDS")
                .subCategory("Trésorerie")
                .active(true)
                .build();
        
        treasuryLoan = loanTypeRepository.save(treasuryLoan);
        
        // Créer la configuration de pénalité associée
        LoanPenaltyConfig penaltyConfig = LoanPenaltyConfig.builder()
                .loanType(treasuryLoan)
                .penaltyDelayDays(20)
                .dailyPenaltyRate(new BigDecimal("0.0015"))
                .maxPenaltyRate(new BigDecimal("2.00"))
                .progressivePenalty(true)
                .calculationIntervalDays(1)
                .reminderMessage("Rappel: Votre prêt trésorerie est en retard de paiement")
                .build();
        
        loanPenaltyConfigRepository.save(penaltyConfig);
    }

    private void createInternalLoanType() {
        LoanType internalLoan = LoanType.builder()
                .name("Prêt Interne")
                .description("Prêt interne à l'organisation")
                .monthlyInterestRate(new BigDecimal("0.0025"))
                .maxPenaltyRate(new BigDecimal("0.10"))
                .gracePeriodDays(60)
                .category("FUNDING_SOURCE")
                .subCategory("Interne")
                .active(true)
                .build();
        
        internalLoan = loanTypeRepository.save(internalLoan);
        
        // Créer la configuration de pénalité associée
        LoanPenaltyConfig penaltyConfig = LoanPenaltyConfig.builder()
                .loanType(internalLoan)
                .penaltyDelayDays(60)
                .dailyPenaltyRate(new BigDecimal("0.00025"))
                .maxPenaltyRate(new BigDecimal("0.10"))
                .progressivePenalty(true)
                .calculationIntervalDays(1)
                .reminderMessage("Rappel: Votre prêt interne est en retard de paiement")
                .build();
        
        loanPenaltyConfigRepository.save(penaltyConfig);
    }

    private void createPartnerBankLoanType() {
        LoanType bankLoan = LoanType.builder()
                .name("Prêt Banque Partenaire")
                .description("Prêt obtenu auprès d'une banque partenaire")
                .monthlyInterestRate(new BigDecimal("0.01"))
                .maxPenaltyRate(new BigDecimal("0.50"))
                .gracePeriodDays(15)
                .category("FUNDING_SOURCE")
                .subCategory("Banque Partenaire")
                .active(true)
                .build();
        
        bankLoan = loanTypeRepository.save(bankLoan);
        
        // Créer la configuration de pénalité associée
        LoanPenaltyConfig penaltyConfig = LoanPenaltyConfig.builder()
                .loanType(bankLoan)
                .penaltyDelayDays(15)
                .dailyPenaltyRate(new BigDecimal("0.001"))
                .maxPenaltyRate(new BigDecimal("0.50"))
                .progressivePenalty(true)
                .calculationIntervalDays(1)
                .reminderMessage("Rappel: Votre prêt banque partenaire est en retard de paiement")
                .build();
        
        loanPenaltyConfigRepository.save(penaltyConfig);
    }

    private void createInstitutionalLoanType() {
        LoanType institutionalLoan = LoanType.builder()
                .name("Prêt Institutionnel")
                .description("Prêt obtenu auprès d'une institution")
                .monthlyInterestRate(new BigDecimal("0.0075"))
                .maxPenaltyRate(new BigDecimal("0.40"))
                .gracePeriodDays(30)
                .category("FUNDING_SOURCE")
                .subCategory("Institutionnel")
                .active(true)
                .build();
        
        institutionalLoan = loanTypeRepository.save(institutionalLoan);
        
        // Créer la configuration de pénalité associée
        LoanPenaltyConfig penaltyConfig = LoanPenaltyConfig.builder()
                .loanType(institutionalLoan)
                .penaltyDelayDays(30)
                .dailyPenaltyRate(new BigDecimal("0.00075"))
                .maxPenaltyRate(new BigDecimal("0.40"))
                .progressivePenalty(true)
                .calculationIntervalDays(1)
                .reminderMessage("Rappel: Votre prêt institutionnel est en retard de paiement")
                .build();
        
        loanPenaltyConfigRepository.save(penaltyConfig);
    }

    private void createDonorLoanType() {
        LoanType donorLoan = LoanType.builder()
                .name("Prêt Donateur")
                .description("Prêt obtenu auprès d'un donateur")
                .monthlyInterestRate(BigDecimal.ZERO)
                .maxPenaltyRate(BigDecimal.ZERO)
                .gracePeriodDays(90)
                .category("FUNDING_SOURCE")
                .subCategory("Donateur")
                .active(true)
                .build();
        
        donorLoan = loanTypeRepository.save(donorLoan);
        
        // Créer la configuration de pénalité associée (aucune pénalité)
        LoanPenaltyConfig penaltyConfig = LoanPenaltyConfig.builder()
                .loanType(donorLoan)
                .penaltyDelayDays(90)
                .dailyPenaltyRate(BigDecimal.ZERO)
                .maxPenaltyRate(BigDecimal.ZERO)
                .progressivePenalty(false)
                .calculationIntervalDays(1)
                .reminderMessage("Information: Votre prêt donateur approche sa date d'échéance")
                .build();
        
        loanPenaltyConfigRepository.save(penaltyConfig);
    }

    private void createShortTermLoanType() {
        LoanType shortTermLoan = LoanType.builder()
                .name("Prêt Court Terme")
                .description("Prêt d'une durée inférieure à 1 an")
                .monthlyInterestRate(new BigDecimal("0.02"))
                .maxPenaltyRate(new BigDecimal("0.50"))
                .gracePeriodDays(15)
                .category("ASSOCIATIVE_DURATION")
                .subCategory("Court Terme")
                .active(true)
                .build();
        
        shortTermLoan = loanTypeRepository.save(shortTermLoan);
        
        // Créer la configuration de pénalité associée
        LoanPenaltyConfig penaltyConfig = LoanPenaltyConfig.builder()
                .loanType(shortTermLoan)
                .penaltyDelayDays(15)
                .dailyPenaltyRate(new BigDecimal("0.002"))
                .maxPenaltyRate(new BigDecimal("0.50"))
                .progressivePenalty(true)
                .calculationIntervalDays(1)
                .reminderMessage("Rappel: Votre prêt court terme est en retard de paiement")
                .build();
        
        loanPenaltyConfigRepository.save(penaltyConfig);
    }

    private void createMediumTermLoanType() {
        LoanType mediumTermLoan = LoanType.builder()
                .name("Prêt Moyen Terme")
                .description("Prêt d'une durée comprise entre 1 et 3 ans")
                .monthlyInterestRate(new BigDecimal("0.015"))
                .maxPenaltyRate(new BigDecimal("0.75"))
                .gracePeriodDays(30)
                .category("ASSOCIATIVE_DURATION")
                .subCategory("Moyen Terme")
                .active(true)
                .build();
        
        mediumTermLoan = loanTypeRepository.save(mediumTermLoan);
        
        // Créer la configuration de pénalité associée (pénalité progressive)
        LoanPenaltyConfig penaltyConfig = LoanPenaltyConfig.builder()
                .loanType(mediumTermLoan)
                .penaltyDelayDays(30)
                .dailyPenaltyRate(new BigDecimal("0.0015"))
                .maxPenaltyRate(new BigDecimal("0.75"))
                .progressivePenalty(true)
                .calculationIntervalDays(1)
                .reminderMessage("Rappel: Votre prêt moyen terme est en retard de paiement")
                .build();
        
        loanPenaltyConfigRepository.save(penaltyConfig);
    }

    private void createLongTermLoanType() {
        LoanType longTermLoan = LoanType.builder()
                .name("Prêt Long Terme")
                .description("Prêt d'une durée supérieure à 3 ans")
                .monthlyInterestRate(new BigDecimal("0.005"))
                .maxPenaltyRate(new BigDecimal("0.25"))
                .gracePeriodDays(45)
                .category("ASSOCIATIVE_DURATION")
                .subCategory("Long Terme")
                .active(true)
                .build();
        
        longTermLoan = loanTypeRepository.save(longTermLoan);
        
        // Créer la configuration de pénalité associée (pénalité modulable)
        LoanPenaltyConfig penaltyConfig = LoanPenaltyConfig.builder()
                .loanType(longTermLoan)
                .penaltyDelayDays(45)
                .dailyPenaltyRate(new BigDecimal("0.0005"))
                .maxPenaltyRate(new BigDecimal("0.25"))
                .progressivePenalty(true)
                .calculationIntervalDays(1)
                .reminderMessage("Rappel: Votre prêt long terme est en retard de paiement")
                .build();
        
        loanPenaltyConfigRepository.save(penaltyConfig);
    }
}