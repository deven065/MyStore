package com.ecom.productcatalog.controller;

import com.ecom.productcatalog.dto.ProductResponse;
import com.ecom.productcatalog.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(ProductResponse::from)
                .toList();
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductResponse> getAllProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductByCategory(categoryId).stream()
                .map(ProductResponse::from)
                .toList();
    }
}
