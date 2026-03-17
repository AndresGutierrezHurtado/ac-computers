package com.accomputers.api.infrastructure.persistence.jpa.repositories;

// LIBRERÍAS JAVA ESTÁNDAR
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// SPRING FRAMEWORK
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

// JAKARTA PERSISTENCE
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.Predicate;

// DTOs Y CRITERIOS DE APLICACIÓN
import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.ProductCriteria;

// PUERTOS Y DOMINIO
import com.accomputers.api.application.ports.output.repositories.ProductRepositoryInterface;
import com.accomputers.api.domain.entities.Product;
import com.accomputers.api.domain.valueobjects.Condition;

// INFRAESTRUCTURA (AI Y JPA)
import com.accomputers.api.infrastructure.ai.SpringAiEmbeddingSupport;
import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductEntity;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.ProductJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.mappers.ProductMapper;

@Repository
public class ProductRepository implements ProductRepositoryInterface {

    private final ProductJpaRepository jpaRepository;
    private final ProductMapper mapper;
    private final SpringAiEmbeddingSupport embeddingSupport;

    @PersistenceContext
    private EntityManager entityManager;

    public ProductRepository(
            ProductJpaRepository jpaRepository,
            ProductMapper mapper,
            SpringAiEmbeddingSupport embeddingSupport) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.embeddingSupport = embeddingSupport;
    }

    @Override
    public PageDTO<Product> findAll(ProductCriteria productCriteria) {
        if (StringUtils.hasText(productCriteria.getSearch())) {
            return findAllByVectorSimilarity(productCriteria);
        }

        Pageable pageable = PageRequest.of(productCriteria.getPage() - 1, productCriteria.getPerPage());
        Specification<ProductEntity> spec = buildSpecification(productCriteria);
        Page<ProductEntity> page = jpaRepository.findAll(spec, pageable);

        List<Product> products = page.getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());

        return new PageDTO<>(products, page.getTotalElements());
    }

    private PageDTO<Product> findAllByVectorSimilarity(ProductCriteria criteria) {
        String vecLiteral = embeddingSupport.embedToPgVectorLiteral(criteria.getSearch().trim());

        StringBuilder fromWhere = new StringBuilder("""
                FROM products p
                LEFT JOIN sub_categories sc ON sc.id = p.sub_category_id
                WHERE p.deleted_at IS NULL
                  AND p.embedding IS NOT NULL
                """);
        appendVectorSearchFilters(fromWhere, criteria);

        String countSql = "SELECT COUNT(p.id) " + fromWhere;
        Query countQuery = entityManager.createNativeQuery(countSql);
        bindVectorFilterParameters(countQuery, criteria);
        @SuppressWarnings("unchecked")
        List<Object> countRows = countQuery.getResultList();
        long total = 0L;
        if (!countRows.isEmpty()) {
            Object row = countRows.get(0);
            if (row instanceof Object[] arr && arr.length > 0) {
                total = ((Number) arr[0]).longValue();
            } else if (row instanceof Number n) {
                total = n.longValue();
            }
        }

        int page = Math.max(criteria.getPage(), 1);
        int perPage = Math.max(criteria.getPerPage(), 1);
        int offset = (page - 1) * perPage;

        String idSql = """
                SELECT p.id
                """
                + fromWhere
                + """
                 ORDER BY p.embedding <-> CAST(:vec AS vector)
                 LIMIT :lim OFFSET :off
                """;
        Query idQuery = entityManager.createNativeQuery(idSql);
        bindVectorFilterParameters(idQuery, criteria);
        idQuery.setParameter("vec", vecLiteral);
        idQuery.setParameter("lim", perPage);
        idQuery.setParameter("off", offset);

        @SuppressWarnings("unchecked")
        List<Object> idRows = idQuery.getResultList();
        List<Integer> orderedIds = new ArrayList<>(idRows.size());
        for (Object row : idRows) {
            if (row instanceof Object[] arr && arr.length > 0) {
                orderedIds.add(((Number) arr[0]).intValue());
            } else if (row instanceof Number n) {
                orderedIds.add(n.intValue());
            }
        }

        if (orderedIds.isEmpty()) {
            return new PageDTO<>(List.of(), total);
        }

        List<ProductEntity> loaded = jpaRepository.findAllByIdInWithAssociations(orderedIds);
        Map<Integer, ProductEntity> byId = new HashMap<>();
        for (ProductEntity e : loaded) {
            byId.put(e.getId(), e);
        }
        List<Product> products = new ArrayList<>(orderedIds.size());
        for (Integer id : orderedIds) {
            ProductEntity e = byId.get(id);
            if (e != null) {
                products.add(mapper.toDomain(e));
            }
        }

        return new PageDTO<>(products, total);
    }

    private static void appendVectorSearchFilters(StringBuilder fromWhere, ProductCriteria criteria) {
        if (criteria.getCategoryId() != null) {
            fromWhere.append(" AND sc.category_id = :categoryId");
        }
        if (criteria.getSubCategoryId() != null) {
            fromWhere.append(" AND p.sub_category_id = :subCategoryId");
        }
        if (criteria.getBrandId() != null) {
            fromWhere.append(" AND p.brand_id = :brandId");
        }
        if (criteria.getCondition() != null && !criteria.getCondition().trim().isEmpty()) {
            Condition.ConditionType conditionType = Condition.ConditionType.fromString(criteria.getCondition());
            if (conditionType != null) {
                fromWhere.append(" AND p.product_condition = :productCondition");
            }
        }
        if (criteria.getMinPrice() != null) {
            fromWhere.append(" AND p.price >= :minPrice");
        }
        if (criteria.getMaxPrice() != null) {
            fromWhere.append(" AND p.price <= :maxPrice");
        }
        if (criteria.getMinDiscount() != null) {
            fromWhere.append(" AND p.discount >= :minDiscount");
        }
        if (criteria.getMaxDiscount() != null) {
            fromWhere.append(" AND p.discount <= :maxDiscount");
        }
    }

    /** Binds optional catalog filters present in {@link #appendVectorSearchFilters}. */
    private static void bindVectorFilterParameters(Query query, ProductCriteria criteria) {
        if (criteria.getCategoryId() != null) {
            query.setParameter("categoryId", criteria.getCategoryId());
        }
        if (criteria.getSubCategoryId() != null) {
            query.setParameter("subCategoryId", criteria.getSubCategoryId());
        }
        if (criteria.getBrandId() != null) {
            query.setParameter("brandId", criteria.getBrandId());
        }
        if (criteria.getCondition() != null && !criteria.getCondition().trim().isEmpty()) {
            Condition.ConditionType conditionType = Condition.ConditionType.fromString(criteria.getCondition());
            if (conditionType != null) {
                query.setParameter("productCondition", conditionType.name());
            }
        }
        if (criteria.getMinPrice() != null) {
            query.setParameter("minPrice", criteria.getMinPrice());
        }
        if (criteria.getMaxPrice() != null) {
            query.setParameter("maxPrice", criteria.getMaxPrice());
        }
        if (criteria.getMinDiscount() != null) {
            query.setParameter("minDiscount", criteria.getMinDiscount());
        }
        if (criteria.getMaxDiscount() != null) {
            query.setParameter("maxDiscount", criteria.getMaxDiscount());
        }
    }

    private Specification<ProductEntity> buildSpecification(ProductCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isNull(root.get("deletedAt")));

            if (criteria.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("subCategory").get("category").get("id"), criteria.getCategoryId()));
            }

            if (criteria.getSubCategoryId() != null) {
                predicates.add(cb.equal(root.get("subCategory").get("id"), criteria.getSubCategoryId()));
            }

            if (criteria.getCondition() != null && !criteria.getCondition().trim().isEmpty()) {
                Condition.ConditionType conditionType = Condition.ConditionType.fromString(criteria.getCondition());
                if (conditionType != null) {
                    ProductEntity.ConditionType entityCondition =
                            ProductEntity.ConditionType.valueOf(conditionType.name());
                    predicates.add(cb.equal(root.get("condition"), entityCondition));
                }
            }

            if (criteria.getBrandId() != null) {
                predicates.add(cb.equal(root.get("brand").get("id"), criteria.getBrandId()));
            }

            if (criteria.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
            }
            if (criteria.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
            }

            if (criteria.getMinDiscount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("discount"), criteria.getMinDiscount()));
            }
            if (criteria.getMaxDiscount() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("discount"), criteria.getMaxDiscount()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public Product findById(Integer id) {
        return jpaRepository.findByIdNotDeleted(id)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public Product save(Product product) {
        if (product == null) {
            return null;
        }

        ProductEntity entity = mapper.toEntity(product);
        Integer id = product.getId();
        if (id != null) {
            jpaRepository.findById(id).ifPresent(existing -> {
                if (StringUtils.hasText(existing.getEmbedding())) {
                    entity.setEmbedding(existing.getEmbedding());
                }
            });
        }
        ProductEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Integer id) {
        jpaRepository.softDelete(id, LocalDateTime.now());
    }
}
