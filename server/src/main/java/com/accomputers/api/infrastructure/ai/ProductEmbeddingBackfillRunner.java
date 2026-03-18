package com.accomputers.api.infrastructure.ai;

import com.accomputers.api.infrastructure.persistence.jpa.entities.ProductEntity;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.ProductJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Fills {@code products.embedding} via Ollama when {@code app.ai.backfill-embeddings=true}.
 * Vector catalog search requires non-null embeddings.
 */
@Component
@Order(100)
@ConditionalOnProperty(name = "app.ai.backfill-embeddings", havingValue = "true")
public class ProductEmbeddingBackfillRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ProductEmbeddingBackfillRunner.class);

    private final ProductJpaRepository productJpaRepository;
    private final SpringAiEmbeddingSupport embeddingSupport;

    public ProductEmbeddingBackfillRunner(
            ProductJpaRepository productJpaRepository,
            SpringAiEmbeddingSupport embeddingSupport) {
        this.productJpaRepository = productJpaRepository;
        this.embeddingSupport = embeddingSupport;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        List<Integer> ids = productJpaRepository.findActiveIdsWithMissingEmbedding();
        if (ids.isEmpty()) {
            log.debug("Embedding backfill: no products with missing embedding");
            return;
        }
        log.info("Embedding backfill: processing {} product(s)", ids.size());
        int ok = 0;
        for (Integer id : ids) {
            try {
                ProductEntity p = productJpaRepository.findById(id).orElse(null);
                if (p == null || p.getDeletedAt() != null) {
                    continue;
                }
                String desc = StringUtils.hasText(p.getDescription()) ? p.getDescription() : "";
                String text = (p.getName() + "\n" + desc).trim();
                if (!StringUtils.hasText(text)) {
                    continue;
                }
                String vec = SpringAiEmbeddingSupport.toPgVectorLiteral(embeddingSupport.embedToFloatArray(text));
                productJpaRepository.updateEmbeddingVectorById(id, vec);
                ok++;
            } catch (Exception e) {
                log.warn("Embedding backfill failed for product id={}: {}", id, e.getMessage());
            }
        }
        log.info("Embedding backfill finished: {} updated", ok);
    }
}
