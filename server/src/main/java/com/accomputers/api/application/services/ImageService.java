package com.accomputers.api.application.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.accomputers.api.application.dtos.CreateImageDTO;
import com.accomputers.api.application.dtos.response.ImageResponseDTO;
import com.accomputers.api.application.ports.input.ImageServiceInterface;
import com.accomputers.api.application.ports.output.FileManagerInterface;
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

    @Autowired
    public ImageService(
            ImageRepositoryInterface imageRepository,
            ProductRepositoryInterface productRepository,
            FileManagerInterface fileManagerInterface) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
        this.fileManagerInterface = fileManagerInterface;
    }

    @Override
    @Transactional
    public ImageResponseDTO createImage(CreateImageDTO imageDTO) {
        // Validar que el producto existe
        if (productRepository.findById(imageDTO.productId()) == null) {
            throw new EntityNotFoundException("Product", imageDTO.productId());
        }

        // Validar que se proporcionó una imagen
        MultipartFile file = imageDTO.image();
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        // Si la imagen es principal, actualizar todas las demás imágenes del producto como no principales
        if (Boolean.TRUE.equals(imageDTO.isMain())) {
            imageRepository.setAllImagesAsNotMainByProductId(imageDTO.productId());
        }

        // Subir la imagen al almacenamiento
        String url = fileManagerInterface.uploadFile(file, "/medias");

        // Crear la entidad Image
        Image image = new Image(
                null,
                new Url(url),
                imageDTO.isMain() != null ? imageDTO.isMain() : false,
                imageDTO.productId());

        // Guardar la imagen
        Image savedImage = imageRepository.save(image);

        return ImageResponseDTO.fromImage(savedImage);
    }

    @Override
    @Transactional
    public void deleteImage(Integer imageId) {
        // Buscar la imagen
        Image image = imageRepository.findById(imageId);

        if (image == null) {
            throw new EntityNotFoundException("Image", imageId);
        }

        // Eliminar el archivo del almacenamiento
        if (image.getUrl() != null && image.getUrl().getValue() != null) {
            fileManagerInterface.deleteFile(image.getUrl().getValue());
        }

        // Eliminar la imagen de la base de datos
        imageRepository.delete(imageId);
    }
}
