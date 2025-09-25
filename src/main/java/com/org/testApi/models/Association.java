package com.org.testApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.Size;

/**
 * Représente une association dans le système.
 * <p>
 * Une association peut avoir des membres, des activités, des transactions financières
 * et des documents associés. Elle est identifiée par son nom, son statut légal,
 * sa localisation et son numéro SIRET.
 * </p>
 */
@Entity
@Table(name = "associations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Association extends BaseEntity {

    /**
     * Nom de l'association.
     * Ce champ est obligatoire et limité à 100 caractères.
     */
    @Column(nullable = false, length = 100)
    @Size(max = 100)
    private String name;

    /**
     * Description optionnelle de l'association.
     * Maximum 500 caractères.
     */
    @Size(max = 500)
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Localisation géographique de l'association.
     * Optionnelle, limitée à 100 caractères.
     */
    @Column(length = 100)
    private String location;

    /**
     * Statut juridique de l'association (par ex. "ASBL", "ONG", etc.).
     * Optionnel, limité à 20 caractères.
     */
    @Column(length = 20)
    private String legalStatus;

    /**
     * Numéro SIRET ou identifiant administratif de l'association.
     * Optionnel, limité à 50 caractères.
     */
    @Column(length = 50)
    private String siret;

    /**
     * Liste des membres appartenant à cette association.
     */
    @OneToMany(mappedBy = "association", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Member> members = new ArrayList<>();

    /**
     * Liste des activités organisées par cette association.
     */
    @OneToMany(mappedBy = "association", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Activity> activities = new ArrayList<>();


    /**
     * Liste des transactions financières associées à l'association.
     */
    @OneToMany(mappedBy = "association", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<FinancialTransaction> transactions = new ArrayList<>();

    /**
     * Liste des documents liés à l'association.
     */
    @OneToMany(mappedBy = "association", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private List<Document> documents = new ArrayList<>();

}