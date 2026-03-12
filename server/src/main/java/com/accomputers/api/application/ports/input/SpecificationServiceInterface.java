package com.accomputers.api.application.ports.input;

import java.util.List;

import com.accomputers.api.application.dtos.response.SpecificationResponseDTO;
import com.accomputers.api.application.dtos.response.SpecificationValueResponseDTO;

public interface SpecificationServiceInterface {
    List<SpecificationResponseDTO> getSpecifications(Integer subCategoryId);
    List<SpecificationValueResponseDTO> getSpecificationValues(Integer specificationId);
}
