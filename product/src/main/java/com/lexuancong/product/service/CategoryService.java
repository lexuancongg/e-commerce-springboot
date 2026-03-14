package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.Category;
import com.lexuancong.product.repository.CategoryRepository;
import com.lexuancong.product.service.internal.ImageClient;
import com.lexuancong.product.dto.category.CategoryCreateRequest;
import com.lexuancong.product.dto.category.CategoryResponse;
import com.lexuancong.product.dto.image.ImagePreviewResponse;
import com.lexuancong.share.exception.BadRequestException;
import com.lexuancong.share.exception.DuplicatedException;
import com.lexuancong.share.exception.NotFoundException;
import com.lexuancong.share.exception.ResourceInUseException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ImageClient imageClient;

    public CategoryService(CategoryRepository categoryRepository, ImageClient imageClient) {
        this.categoryRepository = categoryRepository;
        this.imageClient = imageClient;
    }

    public List<CategoryResponse> getCategories(String categoryName) {
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(categoryName);
        List<Long> imageCategoryIds = categories.stream()
                        .map(Category::getImageId)
                                .filter(Objects::nonNull)
                                        .toList();
        List<ImagePreviewResponse> images = imageClient.getImageByIds(imageCategoryIds);
        Map<Long,String> mapImage = images.stream()
                .collect(Collectors.toMap(
                        image -> image.id(),
                        ImagePreviewResponse::url
                ));
        for (Category category : categories) {
            String url = mapImage.getOrDefault(category.getImageId(),"");
            CategoryResponse cate = CategoryResponse.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .avatarUrl(url)
                    .slug(category.getSlug())
                    .build();
            categoryResponses.add(cate);
        }
        return categoryResponses;

    }


    public CategoryResponse createCategory(CategoryCreateRequest categoryCreateRequest) {
        this.validateDuplicateName(categoryCreateRequest.name(), null);
        Long parentId = categoryCreateRequest.parentId();

        Category category = categoryCreateRequest.toCategory();
        if (parentId != null) {
            Category parent = categoryRepository.findById(categoryCreateRequest.parentId())
                    .orElseThrow(() -> new BadRequestException(Constants.ErrorKey.PARENT_CATEGORY_NOT_FOUND, categoryCreateRequest.parentId()));
            category.setParent(parent);
        }
        return CategoryResponse.fromCategory(categoryRepository.save(category));

    }

    private void validateDuplicateName(String name, Long id) {
        if (this.checkExistName(name, id)) {
            throw new DuplicatedException(Constants.ErrorKey.NAME_ALREADY_EXITED, name);
        }
    }

    private boolean checkExistName(String name, Long id) {
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
            throw new ResourceInUseException(Constants.ErrorKey.CATEGORY_CONTAIN_CHILDREN);
        }
        if(!category.getProductCategories().isEmpty()){
            throw new ResourceInUseException(Constants.ErrorKey.CATEGORY_CONTAIN_PRODUCTS);
        }

        categoryRepository.deleteById(id);
    }


}
