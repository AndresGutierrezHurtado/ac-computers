package com.accomputers.api.application.ports.input;

import com.accomputers.api.application.dtos.response.CategoryResponseDTO;
import java.util.List;

public interface CategoryServiceInterface {
    List<CategoryResponseDTO> getAllCategories();
}
