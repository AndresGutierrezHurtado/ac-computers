package com.accomputers.api.application.ports.output;

import org.springframework.web.multipart.MultipartFile;

public interface FileManagerInterface {
    /**
     * Uploads a file and returns its URL
     * @param file The file to upload
     * @param folder Optional folder path in the storage
     * @return The URL of the uploaded file
     */
    String uploadFile(MultipartFile file, String folder);

    /**
     * Deletes a file by its URL or public ID
     * @param urlOrPublicId The URL or public ID of the file to delete
     */
    void deleteFile(String urlOrPublicId);
}

