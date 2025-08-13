package com.org.testApi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload-dir:uploads/}")
    private String uploadDir;

    @Value("${cloud.aws.s3.bucket-name:}")
    private String bucketName;

    @Value("${file.storage.type:local}") // "local" ou "s3"
    private String storageType;

    private List<Observer<String>> observers = new ArrayList<>();

    @Autowired(required = false)
    private S3Client s3Client;

    public FileUploadServiceImpl() {
        // Créer le répertoire d'upload s'il n'existe pas (pour le stockage local)
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        // Générer un nom de fichier unique
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        if ("s3".equals(storageType) && s3Client != null) {
            // Stockage sur Amazon S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileName)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } else {
            // Stockage local
            Path filePath = Paths.get(uploadDir, uniqueFileName);
            Files.write(filePath, file.getBytes());
        }

        // Notifier les observateurs qu'un fichier a été uploadé
        notifyObservers("FILE_UPLOADED", uniqueFileName);

        // Retourner le chemin ou l'identifiant du fichier
        return uniqueFileName;
    }

    @Override
    public byte[] downloadFile(String fileName) {
        try {
            byte[] fileData;

            if ("s3".equals(storageType) && s3Client != null) {
                // Téléchargement depuis Amazon S3
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build();

                fileData = s3Client.getObject(getObjectRequest).readAllBytes();
            } else {
                // Téléchargement depuis le stockage local
                Path filePath = Paths.get(uploadDir, fileName);
                fileData = Files.readAllBytes(filePath);
            }

            // Notifier les observateurs qu'un fichier a été téléchargé
            notifyObservers("FILE_DOWNLOADED", fileName);

            return fileData;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du téléchargement du fichier: " + fileName, e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            if ("s3".equals(storageType) && s3Client != null) {
                // Suppression depuis Amazon S3
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build();

                s3Client.deleteObject(deleteObjectRequest);
            } else {
                // Suppression du stockage local
                Path filePath = Paths.get(uploadDir, fileName);
                Files.deleteIfExists(filePath);
            }

            // Notifier les observateurs qu'un fichier a été supprimé
            notifyObservers("FILE_DELETED", fileName);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la suppression du fichier: " + fileName, e);
        }
    }

    @Override
    public void addObserver(Observer<String> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<String> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, String entity) {
        for (Observer<String> observer : observers) {
            observer.update(event, entity);
        }
    }
}
