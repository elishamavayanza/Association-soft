package com.org.testApi.models;

/**
 * Enumération des types de cotisations possibles.
 */
public enum MembershipFeeType {
    WEEKLY("par semaine"),
    MONTHLY("par mois"), 
    YEARLY("par an"),
    INSTALLMENT("par donation");

    private final String frenchLabel;

    MembershipFeeType(String frenchLabel) {
        this.frenchLabel = frenchLabel;
    }

    public String getFrenchLabel() {
        return frenchLabel;
    }

    @Override
    public String toString() {
        return frenchLabel;
    }
}