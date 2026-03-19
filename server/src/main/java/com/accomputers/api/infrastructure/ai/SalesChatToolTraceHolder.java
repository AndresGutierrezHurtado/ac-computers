package com.accomputers.api.infrastructure.ai;

import com.accomputers.api.application.dtos.ProductSearchHitDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Collects catalog rows from {@link ProductVectorSearchTool} during a single {@link com.accomputers.api.infrastructure.ai.AIPortImpl#chat} call (same request thread).
 */
@Component
public class SalesChatToolTraceHolder {

    private final ThreadLocal<List<ProductSearchHitDto>> accumulated = ThreadLocal.withInitial(ArrayList::new);

    public void clear() {
        accumulated.get().clear();
    }

    public void addAll(List<ProductSearchHitDto> hits) {
        if (hits == null || hits.isEmpty()) {
            return;
        }
        accumulated.get().addAll(hits);
    }

    /**
     * Returns a snapshot of accumulated hits and clears the buffer for this thread.
     */
    public List<ProductSearchHitDto> drain() {
        List<ProductSearchHitDto> copy = List.copyOf(accumulated.get());
        accumulated.get().clear();
        return copy;
    }
}
