package com.atlas.catalog.service;

import com.atlas.catalog.dto.CategoryRequest;
import com.atlas.catalog.entity.Category;
import com.atlas.catalog.exception.ResourceNotFoundException;
import com.atlas.catalog.repository.CategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }

    public Category create(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        return categoryRepository.save(category);
    }

    public Category update(Long id, CategoryRequest request) {
        Category category = findById(id);
        category.setName(request.name());
        category.setDescription(request.description());
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        Category category = findById(id);
        categoryRepository.delete(category);
    }
}
