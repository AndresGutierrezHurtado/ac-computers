package com.accomputers.api.infrastructure.filemanager;

import com.accomputers.api.application.ports.output.FileManagerInterface;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryFileManager implements FileManagerInterface {

    private final Cloudinary cloudinary;

    public CloudinaryFileManager(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        try {
            Map<String, Object> uploadParams = ObjectUtils.asMap("folder", folder, "resource_type", "auto");

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    uploadParams);

            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }
    }

    @Override
    public void deleteFile(String urlOrPublicId) {
        if (urlOrPublicId == null || urlOrPublicId.trim().isEmpty()) {
            throw new IllegalArgumentException("URL or public ID cannot be null or empty");
        }

        try {
            String publicId = extractPublicId(urlOrPublicId);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file from Cloudinary", e);
        }
    }

    private String extractPublicId(String urlOrPublicId) {
        if (!urlOrPublicId.startsWith("http://") && !urlOrPublicId.startsWith("https://")) {
            return urlOrPublicId;
        }

        try {
            String[] parts = urlOrPublicId.split("/upload/");
            if (parts.length > 1) {
                String afterUpload = parts[1];
                if (afterUpload.matches("^v\\d+/.*")) {
                    afterUpload = afterUpload.substring(afterUpload.indexOf('/') + 1);
                }
                int lastDot = afterUpload.lastIndexOf('.');
                if (lastDot > 0) {
                    afterUpload = afterUpload.substring(0, lastDot);
                }
                return afterUpload;
            }
        } catch (Exception e) {
        }

        return urlOrPublicId;
    }
}
