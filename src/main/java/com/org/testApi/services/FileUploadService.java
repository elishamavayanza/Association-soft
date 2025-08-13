package com.org.testApi.services;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileUploadService extends ObservableService<String> {
    String uploadFile(MultipartFile file) throws IOException;
    byte[] downloadFile(String fileName);
    void deleteFile(String fileName);
}
