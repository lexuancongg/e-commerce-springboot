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
@RequestMapping("/api/category")
@RestController
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    // đã check
    @GetMapping({"/management/categories","/customer/categories"})
    public ResponseEntity<List<CategoryVm>> getCategories(
            @RequestParam(required = false , defaultValue = "") String categoryName
    ) {
        List<CategoryVm> categoryVms = categoryService.getCategories(categoryName);
        return new ResponseEntity<>(categoryVms, HttpStatus.OK);
    }

    // đã check
    @PostMapping("/management/categories")
    public ResponseEntity<CategoryVm> createCategory(
            @RequestBody @Valid CategoryPostVm categoryPostVm,
            UriComponentsBuilder uriComponentsBuilder
            ){
        CategoryVm categorySaved = categoryService.createCategory(categoryPostVm);
        return ResponseEntity.created(uriComponentsBuilder.replacePath("/categories/{id}")
                .buildAndExpand(categorySaved.id())
                .toUri()
        ).body(categorySaved);
    }

    // đã check
    @PutMapping("/management/categories/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long id,
                                               @RequestBody @Valid CategoryPostVm categoryPostVm
    ){
        categoryService.updateCategory(id,categoryPostVm);
        return ResponseEntity.noContent().build();
    }


    // đã check
    @DeleteMapping("/management/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
