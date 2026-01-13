package com.accomputers.api.application.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.accomputers.api.application.dtos.CreateImageDTO;
import com.accomputers.api.application.dtos.response.ImageResponseDTO;
import com.accomputers.api.application.ports.input.ImageServiceInterface;
import com.accomputers.api.application.ports.output.FileManagerInterface;
import com.accomputers.api.application.ports.output.LoggerPort;
import com.accomputers.api.application.ports.output.repositories.ImageRepositoryInterface;
import com.accomputers.api.application.ports.output.repositories.ProductRepositoryInterface;
import com.accomputers.api.domain.entities.Image;
import com.accomputers.api.domain.exceptions.EntityNotFoundException;
import com.accomputers.api.domain.valueobjects.Url;

@Service
public class ImageService implements ImageServiceInterface {
    private final ImageRepositoryInterface imageRepository;
    private final ProductRepositoryInterface productRepository;
    private final FileManagerInterface fileManagerInterface;
    private final LoggerPort loggerPort;

    @Autowired
    public ImageService(
            ImageRepositoryInterface imageRepository,
            ProductRepositoryInterface productRepository,
            FileManagerInterface fileManagerInterface,
            LoggerPort loggerPort) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
        this.fileManagerInterface = fileManagerInterface;
        this.loggerPort = loggerPort;
    }

    @Override
    @Transactional
    public ImageResponseDTO createImage(CreateImageDTO imageDTO) {
        // Validate that the product exists
        if (productRepository.findById(imageDTO.productId()) == null) {
            throw new EntityNotFoundException("Product", imageDTO.productId());
        }

        // Validate that an image was provided
        MultipartFile file = imageDTO.image();
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        // If the image is main, update all other images of the product as not main
        if (Boolean.TRUE.equals(imageDTO.isMain())) {
            imageRepository.setAllImagesAsNotMainByProductId(imageDTO.productId());
        }

        // Upload the image to storage
        String url = fileManagerInterface.uploadFile(file, "/medias");

        // Create the Image entity
        Image image = new Image(
                null,
                new Url(url),
                imageDTO.isMain() != null ? imageDTO.isMain() : false,
                imageDTO.productId());

        // Save the image
        Image savedImage = imageRepository.save(image);

        loggerPort.info(String.format("Image created successfully - ID: %d, Product ID: %d, Is Main: %s", 
            savedImage.getId(), savedImage.getProductId(), savedImage.getIsMain()));

        return ImageResponseDTO.fromImage(savedImage);
    }

    @Override
    @Transactional
    public void deleteImage(Integer imageId) {
        // Find the image
        Image image = imageRepository.findById(imageId);

        if (image == null) {
            throw new EntityNotFoundException("Image", imageId);
        }

        loggerPort.info(String.format("Image deleted successfully - ID: %d, Product ID: %d", 
            image.getId(), image.getProductId()));

        // Delete the file from storage
        if (image.getUrl() != null && image.getUrl().getValue() != null) {
            fileManagerInterface.deleteFile(image.getUrl().getValue());
        }

        // Delete the image from the database
        imageRepository.delete(imageId);
    }
}
