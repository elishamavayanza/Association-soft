package com.org.testApi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @Size(max = 100, message = "Le nom du document ne doit pas dépasser 100 caractères")
    private String name;

    @Column(length = 50)
    @Size(max = 50, message = "Le type de fichier ne doit pas dépasser 50 caractères")
    private String fileType;

    @Column(nullable = false)
    private String filePath;

    private LocalDateTime uploadDate;
    private Long fileSize;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "association_id")
    @ToString.Exclude
    private Association association;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    @ToString.Exclude
    private User uploadedBy;

    @PrePersist
    protected void onCreate() {
        this.uploadDate = LocalDateTime.now();
    }
    private boolean active = true;

}