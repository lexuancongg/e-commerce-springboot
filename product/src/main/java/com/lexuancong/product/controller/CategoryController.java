package com.lexuancong.product.controller;

import com.lexuancong.product.service.CategoryService;
import com.lexuancong.product.dto.category.CategoryCreateRequest;
import com.lexuancong.product.dto.category.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
@RestController
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping({"/management/categories","/customer/categories"})
    public ResponseEntity<List<CategoryResponse>> getCategories(
            @RequestParam(required = false , defaultValue = "") String categoryName
    ) {
        List<CategoryResponse> categories = categoryService.getCategories(categoryName);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/management/categories")
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestBody @Valid CategoryCreateRequest categoryCreateRequest,
            UriComponentsBuilder uriComponentsBuilder
            ){
        CategoryResponse categorySaved = categoryService.createCategory(categoryCreateRequest);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/categories/{id}")
                .buildAndExpand(categorySaved.id())
                .toUri()
        ).body(categorySaved);
    }

    @PutMapping("/management/categories/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long id,
                                               @RequestBody @Valid CategoryCreateRequest categoryCreateRequest
    ){
        categoryService.updateCategory(id, categoryCreateRequest);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/management/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
