package com.org.testApi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Représente un document stocké par l'association.
 * <p>
 * Un document est associé à une entité {@link Association} et peut être
 * lié à un utilisateur ayant effectué le téléversement.
 * Il contient des informations comme le nom, le type de fichier, la taille,
 * le chemin de stockage, ainsi que la date d'upload.
 * </p>
 */
@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Document extends BaseEntity {

    /**
     * Nom du document.
     * Ne doit pas dépasser 100 caractères.
     */
    @Column(nullable = false, length = 100)
    @Size(max = 100, message = "Le nom du document ne doit pas dépasser 100 caractères")
    private String name;

    /**
     * Type MIME ou extension du fichier (ex: "application/pdf", "image/png").
     * Limité à 50 caractères.
     */
    @Column(length = 50)
    @Size(max = 50, message = "Le type de fichier ne doit pas dépasser 50 caractères")
    private String fileType;

    /**
     * Chemin de stockage du fichier sur le serveur ou dans le système de fichiers.
     * Ce champ est obligatoire.
     */
    @Column(nullable = false)
    private String filePath;

    /**
     * Date et heure du téléversement du document.
     * Définie automatiquement à la création.
     */
    private LocalDateTime uploadDate;

    /**
     * Taille du fichier en octets.
     */
    private Long fileSize;

    /**
     * Association à laquelle ce document est lié.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_id")
    @ToString.Exclude
    private Association association;

    /**
     * Utilisateur ayant téléversé le document.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    @ToString.Exclude
    private User uploadedBy;

    /**
     * Méthode de cycle de vie JPA appelée avant l'insertion en base de données.
     * Initialise automatiquement la date de téléversement.
     */
    @PrePersist
    protected void onCreate() {
        this.uploadDate = LocalDateTime.now();
    }

}