package com.accomputers.api.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false)
    private Float price;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_condition", nullable = false)
    private ConditionType condition;

    @Column(name = "discount", nullable = false)
    private Float discount;

    @Column(name = "brand_id", nullable = false)
    private Integer brandId;

    @Column(name = "sub_category_id", nullable = false)
    private Integer subCategoryId;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "embedding", columnDefinition = "vector(2560)", nullable = true)
    @ColumnTransformer(read = "CAST(embedding AS text)", write = "CAST(? AS vector)")
    private String embedding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", insertable = false, updatable = false)
    private SubCategoryEntity subCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", insertable = false, updatable = false)
    private BrandEntity brand;

    @BatchSize(size = 32)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageEntity> images = new ArrayList<>();

    @BatchSize(size = 32)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSpecificationEntity> productSpecifications = new ArrayList<>();

    public enum ConditionType {
        NEW,
        USED,
        REFURBISHED,
        FOR_PARTS
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public ProductEntity() {
    }

    public ProductEntity(Integer id, String name, String description, Float price, ConditionType condition,
            Float discount, Integer brandId, Integer subCategoryId, LocalDateTime deletedAt,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.discount = discount;
        this.brandId = brandId;
        this.subCategoryId = subCategoryId;
        this.deletedAt = deletedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Float getPrice() {
        return price;
    }

    public ConditionType getCondition() {
        return condition;
    }

    public Float getDiscount() {
        return discount;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public BrandEntity getBrand() {
        return brand;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public SubCategoryEntity getSubCategory() {
        return subCategory;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getEmbedding() {
        return embedding;
    }

    public List<ImageEntity> getImages() {
        return images;
    }

    public List<ProductSpecificationEntity> getProductSpecifications() {
        return productSpecifications;
    }

    // Getters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setCondition(ConditionType condition) {
        this.condition = condition;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public void setBrand(BrandEntity brand) {
        this.brand = brand;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public void setSubCategory(SubCategoryEntity subCategory) {
        this.subCategory = subCategory;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setImages(List<ImageEntity> images) {
        this.images = images;
    }

    public void setProductSpecifications(List<ProductSpecificationEntity> productSpecifications) {
        this.productSpecifications = productSpecifications;
    }

    public void setEmbedding(String embedding) {
        this.embedding = embedding;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductEntity that = (ProductEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
