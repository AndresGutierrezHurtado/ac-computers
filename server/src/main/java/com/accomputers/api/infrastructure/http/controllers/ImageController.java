package com.accomputers.api.infrastructure.http.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// DTOs
import com.accomputers.api.application.dtos.CreateImageDTO;
import com.accomputers.api.application.dtos.response.ImageResponseDTO;
import com.accomputers.api.infrastructure.http.responses.ResponseDTO;

// Ports
import com.accomputers.api.application.ports.input.ImageServiceInterface;

@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageServiceInterface imageServiceInterface;

    @Autowired
    public ImageController(ImageServiceInterface imageServiceInterface) {
        this.imageServiceInterface = imageServiceInterface;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<ImageResponseDTO>> createImage(
            @RequestParam("productId") Integer productId,
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "isMain", required = false, defaultValue = "false") Boolean isMain) {

        CreateImageDTO imageDTO = new CreateImageDTO(productId, image, isMain);
        ImageResponseDTO createdImage = imageServiceInterface.createImage(imageDTO);

        ResponseDTO<ImageResponseDTO> responseDTO = new ResponseDTO<>(
                "Image created successfully",
                true,
                createdImage);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteImage(@PathVariable Integer id) {
        imageServiceInterface.deleteImage(id);

        ResponseDTO<Void> responseDTO = new ResponseDTO<>(
                "Image deleted successfully",
                true);

        return ResponseEntity.ok(responseDTO);
    }
}
