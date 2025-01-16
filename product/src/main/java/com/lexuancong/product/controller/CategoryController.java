package com.lexuancong.product.controller;

import com.lexuancong.product.service.CategoryService;
import com.lexuancong.product.viewmodel.category.CategoryPostVm;
import com.lexuancong.product.viewmodel.category.CategoryVm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping({"/backoffice/categories","/storefront/categories"})
    public ResponseEntity<List<CategoryVm>> getCategories(
            @RequestParam(required = false , defaultValue = "") String categoryName
    ) {
        List<CategoryVm> categoryVms = categoryService.getCategories(categoryName);
        return new ResponseEntity<>(categoryVms, HttpStatus.OK);
    }
    @PostMapping("/backoffice/categories")
    public ResponseEntity<CategoryVm> createCategory(
            @RequestBody @Valid CategoryPostVm categoryPostVm,
            UriComponentsBuilder uriComponentsBuilder,
            Principal principal
            ){
        CategoryVm categorySaved = categoryService.createCategory(categoryPostVm);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/categories/{id}")
                .buildAndExpand(categorySaved.id())
                .toUri()
        ).body(categorySaved);
    }

    @PutMapping("/backoffice/categories/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long id,
                                               @RequestBody @Valid CategoryPostVm categoryPostVm,
                                               Principal principal
    ){
        categoryService.updateCategory(id,categoryPostVm);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/backoffice/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
