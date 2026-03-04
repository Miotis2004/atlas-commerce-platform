package com.atlas.catalog.dto;

import com.atlas.catalog.entity.Product;
import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        String sku,
        BigDecimal price,
        Long categoryId,
        String categoryName
) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getSku(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }
}
