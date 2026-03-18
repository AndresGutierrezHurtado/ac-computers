package com.accomputers.api.infrastructure.ai;

import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.ProductCriteria;
import com.accomputers.api.application.dtos.ProductSearchHitDto;
import com.accomputers.api.application.dtos.response.ProductResponseDTO;
import com.accomputers.api.application.ports.output.repositories.ProductRepositoryInterface;
import com.accomputers.api.domain.entities.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Tool for the sales chat model: semantic (vector) search over the catalog via {@link ProductRepositoryInterface}.
 */
@Component
public class ProductVectorSearchTool {

    private static final int PAGE = 1;
    private static final int PER_PAGE = 5;

    private final ProductRepositoryInterface productRepository;
    private final ObjectMapper objectMapper;
    private final SalesChatToolTraceHolder traceHolder;

    public ProductVectorSearchTool(
            ProductRepositoryInterface productRepository,
            ObjectMapper objectMapper,
            SalesChatToolTraceHolder traceHolder) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
        this.traceHolder = traceHolder;
    }

    @Tool(description = """
            Busca en el catálogo real de AC Computers los productos más semejantes a una consulta en lenguaje natural.
            Devuelve hasta 5 productos con datos verificados (nombre, precio, condición, marca).
            Úsala siempre que el cliente pregunte por equipos, precios, marcas o quiera recomendaciones concretas.""")
    public String searchProductsBySimilarity(
            @ToolParam(description = "Consulta o intención de búsqueda en español (ej. laptop barata para estudiar)") String searchQuery) {
        if (!StringUtils.hasText(searchQuery)) {
            return "[]";
        }
        ProductCriteria criteria = new ProductCriteria(
                PAGE,
                PER_PAGE,
                searchQuery.trim(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        PageDTO<Product> page = productRepository.findAll(criteria);
        List<Product> products = page.data();
        if (products == null || products.isEmpty()) {
            return "[]";
        }
        List<Map<String, Object>> compact = products.stream()
                .map(p -> {
                    ProductResponseDTO dto = ProductResponseDTO.fromProduct(p);
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("id", dto.id());
                    row.put("name", dto.name() != null ? dto.name() : "");
                    row.put("description", truncate(dto.description(), 400));
                    row.put("price", dto.price() != null ? dto.price() : 0f);
                    row.put("condition", dto.condition() != null ? dto.condition() : "");
                    row.put("discount", dto.discount() != null ? dto.discount() : 0f);
                    row.put("brand", dto.brand() != null ? dto.brand().name() : "");
                    return row;
                })
                .collect(Collectors.toList());

        List<ProductSearchHitDto> hits = products.stream()
                .map(p -> {
                    ProductResponseDTO dto = ProductResponseDTO.fromProduct(p);
                    return new ProductSearchHitDto(
                            dto.id(),
                            dto.name() != null ? dto.name() : "",
                            dto.price() != null ? dto.price() : 0f,
                            dto.condition() != null ? dto.condition() : "",
                            dto.brand() != null ? dto.brand().name() : "",
                            truncate(dto.description(), 400));
                })
                .collect(Collectors.toList());
        traceHolder.addAll(hits);
        try {
            return objectMapper.writeValueAsString(compact);
        } catch (JsonProcessingException e) {
            return compact.toString();
        }
    }

    private static String truncate(String s, int max) {
        if (s == null) {
            return "";
        }
        String t = s.trim();
        return t.length() <= max ? t : t.substring(0, max) + "…";
    }
}
