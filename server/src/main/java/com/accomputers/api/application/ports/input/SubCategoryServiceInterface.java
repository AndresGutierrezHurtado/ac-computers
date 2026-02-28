package com.accomputers.api.application.ports.input;

import com.accomputers.api.application.dtos.response.SubCategoryDTO;
import java.util.List;

public interface SubCategoryServiceInterface {
    List<SubCategoryDTO> getAllSubCategories(Integer categoryId);
}
