package com.accomputers.api.application.ports.input;

import com.accomputers.api.application.dtos.CreateImageDTO;
import com.accomputers.api.application.dtos.response.ImageResponseDTO;

public interface ImageServiceInterface {
    ImageResponseDTO createImage(CreateImageDTO imageDTO);
    void deleteImage(Integer imageId);
}
