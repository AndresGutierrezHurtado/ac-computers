package com.accomputers.api.application.ports.input;

import com.accomputers.api.application.dtos.response.BrandResponseDTO;
import java.util.List;

public interface BrandServiceInterface {
    List<BrandResponseDTO> getAllBrands();
}
