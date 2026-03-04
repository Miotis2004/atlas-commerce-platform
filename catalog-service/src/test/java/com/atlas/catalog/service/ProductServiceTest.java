package com.atlas.catalog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.atlas.catalog.dto.CategoryRequest;
import com.atlas.catalog.dto.ProductRequest;
import com.atlas.catalog.entity.Product;
import com.atlas.catalog.exception.ResourceNotFoundException;
import com.atlas.catalog.repository.CategoryRepository;
import com.atlas.catalog.repository.ProductRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private ProductService productService;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository);
        productService = new ProductService(productRepository, categoryRepository);
    }

    @Test
    void searchReturnsMatchingProductsByName() {
        var category = categoryService.create(new CategoryRequest("Books", "Book catalog"));
        productService.create(new ProductRequest("Atlas Guide", "Travel guide", "SKU-001", new BigDecimal("24.99"), category.getId()));
        productService.create(new ProductRequest("Kitchen Knife", "Chef knife", "SKU-002", new BigDecimal("39.99"), category.getId()));

        var result = productService.search("atlas", PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getSku()).isEqualTo("SKU-001");
    }

    @Test
    void createFailsWhenCategoryDoesNotExist() {
        ProductRequest request = new ProductRequest("Widget", "No category", "SKU-404", new BigDecimal("12.00"), 999L);

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found");
    }

    @Test
    void updateChangesProductFields() {
        var category = categoryService.create(new CategoryRequest("Electronics", "Devices"));
        Product created = productService.create(new ProductRequest("Mouse", "Wireless", "SKU-M1", new BigDecimal("49.00"), category.getId()));

        Product updated = productService.update(
                created.getId(),
                new ProductRequest("Mouse Pro", "Ergonomic", "SKU-M1", new BigDecimal("59.00"), category.getId())
        );

        assertThat(updated.getName()).isEqualTo("Mouse Pro");
        assertThat(updated.getPrice()).isEqualByComparingTo("59.00");
    }
}
