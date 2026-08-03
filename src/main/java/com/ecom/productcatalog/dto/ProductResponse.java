package com.ecom.productcatalog.dto;

import com.ecom.productcatalog.model.Category;
import com.ecom.productcatalog.model.Product;

import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        String description,
        String imageUrl,
        Double price,
        CategorySummary category
) {
    public static ProductResponse from(Product product) {
        Category category = product.getCategory();

        CategorySummary categorySummary = category == null
                ? null
                : new CategorySummary(category.getId(), category.getName(), List.of());

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getImageUrl(),
                product.getPrice(),
                categorySummary
        );
    }

    public record CategorySummary(
            Long id,
            String name,
            List<ProductResponse> products
    ) {
    }
}
