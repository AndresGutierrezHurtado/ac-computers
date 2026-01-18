package com.accomputers.api.Integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.accomputers.api.application.dtos.createProductDTO;
import com.accomputers.api.application.ports.input.ProductServiceInterface;
import com.accomputers.api.application.ports.output.repositories.BrandRepositoryInterface;
import com.accomputers.api.application.ports.output.repositories.CategoryRepositoryInterface;
import com.accomputers.api.application.ports.output.repositories.SubCategoryRepositoryInterface;
import com.accomputers.api.domain.entities.Brand;
import com.accomputers.api.domain.entities.Category;
import com.accomputers.api.domain.entities.SubCategory;
import com.accomputers.api.domain.valueobjects.Slug;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Rollback
public class ProductIntegrationTest {

    private final ProductServiceInterface productService;
    private final BrandRepositoryInterface brandRepository;
    private final SubCategoryRepositoryInterface subCategoryRepository;
    private final CategoryRepositoryInterface categoryRepository;

    @Autowired
    public ProductIntegrationTest(
            ProductServiceInterface productService,
            BrandRepositoryInterface brandRepository,
            SubCategoryRepositoryInterface subCategoryRepository,
            CategoryRepositoryInterface categoryRepository) {
        this.productService = productService;
        this.brandRepository = brandRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    private Brand createTestBrand() {
        String uniqueName = "TestBrand" + System.currentTimeMillis();
        Brand brand = new Brand(null, uniqueName);
        return brandRepository.save(brand);
    }

    private Category createTestCategory() {
        String uniqueName = "TestCategory" + System.currentTimeMillis();
        String uniqueSlug = "test-category-" + System.currentTimeMillis();
        Category category = new Category(null, uniqueName, new Slug(uniqueSlug), "Test category description");
        return categoryRepository.save(category);
    }

    private SubCategory createTestSubCategory() {
        Category category = createTestCategory();
        String uniqueName = "TestSubCategory" + System.currentTimeMillis();
        String uniqueSlug = "test-sub-category-" + System.currentTimeMillis();
        SubCategory subCategory = new SubCategory(
                null,
                uniqueName,
                new Slug(uniqueSlug),
                "Test description",
                category.getId());
        return subCategoryRepository.save(subCategory);
    }

    private createProductDTO createValidProductDTO(Integer brandId, Integer subCategoryId) {
        return new createProductDTO(
                "Test Product",
                "Test product description",
                99.99f,
                "new",
                10.0f,
                brandId,
                subCategoryId,
                null,
                null);
    }
}
