package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.Category;
import com.lexuancong.product.repository.CategoryRepository;
import com.lexuancong.product.service.internal.ImageService;
import com.lexuancong.product.dto.category.CategoryCreateRequest;
import com.lexuancong.product.dto.category.CategoryGetResponse;
import com.lexuancong.product.dto.image.ImagePreviewGetResponse;
import com.lexuancong.share.exception.BadRequestException;
import com.lexuancong.share.exception.DuplicatedException;
import com.lexuancong.share.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    public CategoryService(CategoryRepository categoryRepository, ImageService imageService) {
        this.categoryRepository = categoryRepository;
        this.imageService = imageService;
    }

    public List<CategoryGetResponse> getCategories(String categoryName) {
        List<CategoryGetResponse> categoryGetResponses = new ArrayList<>();
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(categoryName);
        categories.forEach(category -> {
            ImagePreviewGetResponse image = null;
            if (category.getImageId() != null) {
                image = new ImagePreviewGetResponse(category.getImageId(), imageService.getImageById(category.getImageId()).url());
            }
            CategoryGetResponse categoryGetResponse = CategoryGetResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .slug(category.getSlug())
                    .avatarUrl(image != null ? image.url() : "")
                    .build();
            categoryGetResponses.add(categoryGetResponse);
        });
        return categoryGetResponses;

    }

    public CategoryGetResponse createCategory(CategoryCreateRequest categoryCreateRequest) {
        this.validateDuplicateName(categoryCreateRequest.name(), null);
        Category category = categoryCreateRequest.toCategory();
        if (categoryCreateRequest.parentId() != null) {
            Category parent = categoryRepository.findById(categoryCreateRequest.parentId())
                    .orElseThrow(() -> new BadRequestException(Constants.ErrorKey.PARENT_CATEGORY_NOT_FOUND, categoryCreateRequest.parentId()));
            category.setParent(parent);
        }
        return CategoryGetResponse.fromCategory(categoryRepository.saveAndFlush(category));

    }

    private void validateDuplicateName(String name, Long id) {
        if (this.checkIsExistName(name, id)) {
            throw new DuplicatedException(Constants.ErrorKey.NAME_ALREADY_EXITED, name);
        }
    }

    private boolean checkIsExistName(String name, Long id) {
        return categoryRepository.findByNameAndIdNot(name, id) != null;
    }

    public void updateCategory(Long id, CategoryCreateRequest categoryCreateRequest) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.CATEGORY_NOT_FOUND, id));
        this.validateDuplicateName(categoryCreateRequest.name(), id);
        this.updateCategoryAttribute(category, categoryCreateRequest);

        categoryRepository.save(category);
    }

    private void updateCategoryAttribute(Category category, CategoryCreateRequest categoryCreateRequest) {
        category.setName(categoryCreateRequest.name());
        category.setSlug(categoryCreateRequest.slug());
        category.setDescription(categoryCreateRequest.description());

        category.setPublic(categoryCreateRequest.isPublic());
        category.setImageId(categoryCreateRequest.imageId());
        if (categoryCreateRequest.parentId() != null) {
            Category parent = categoryRepository.findById(categoryCreateRequest.parentId())
                    .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.PARENT_CATEGORY_NOT_FOUND, categoryCreateRequest.parentId()));
           this.validateCircularParent(category.getId(), parent);
           category.setParent(parent);
        }

    }

    private void  validateCircularParent(Long categoryId, Category parent) {
        if (this.checkIsCircularParent(categoryId, parent)) {
            throw new BadRequestException(Constants.ErrorKey.CATEGORY_PARENT_LOOP);
        }
    }

    private boolean checkIsCircularParent(Long id, Category parent) {
        if (id.equals(parent.getId())) {
            return true;
        }
        if (parent.getParent() != null) {
            return checkIsCircularParent(id, parent.getParent());
        }
        return false;
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.CATEGORY_NOT_FOUND, id));
        if(!category.getChild().isEmpty()){
            throw new BadRequestException(Constants.ErrorKey.CATEGORY_CONTAIN_CHILDREN);
        }
        if(!category.getProductCategories().isEmpty()){
            throw new BadRequestException(Constants.ErrorKey.CATEGORY_CONTAIN_PRODUCTS);
        }

        categoryRepository.deleteById(id);
    }


}
