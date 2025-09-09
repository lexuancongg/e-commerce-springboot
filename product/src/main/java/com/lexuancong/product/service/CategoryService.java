package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.Category;
import com.lexuancong.product.repository.CategoryRepository;
import com.lexuancong.product.service.internal.ImageService;
import com.lexuancong.product.viewmodel.category.CategoryPostVm;
import com.lexuancong.product.viewmodel.category.CategoryVm;
import com.lexuancong.product.viewmodel.image.ImageVm;
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

    public List<CategoryVm> getCategories(String categoryName) {
        List<CategoryVm> categoryVms = new ArrayList<>();
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(categoryName);
        categories.forEach(category -> {
            ImageVm image = null;
            if (category.getImageId() != null) {
                image = new ImageVm(category.getImageId(), imageService.getImageById(category.getImageId()).url());
            }
            Category parent = category.getParent();

            Long parentId = parent == null ? null : parent.getId();
            CategoryVm categoryVm = CategoryVm.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .slug(category.getSlug())
                    .avatarUrl(image != null ? image.url() : "")
                    .build();
            categoryVms.add(categoryVm);
        });
        return categoryVms;

    }

    public CategoryVm createCategory(CategoryPostVm categoryPostVm) {
        this.validateDuplicateName(categoryPostVm.name(), null);
        Category category = categoryPostVm.toModel();
        if (categoryPostVm.parentId() != null) {
            Category parent = categoryRepository.findById(categoryPostVm.parentId())
                    .orElseThrow(() -> new BadRequestException(Constants.ErrorKey.PARENT_CATEGORY_NOT_FOUND, categoryPostVm.parentId()));
            category.setParent(parent);
        }
        return CategoryVm.fromModel(categoryRepository.saveAndFlush(category));

    }

    private void validateDuplicateName(String name, Long id) {
        if (this.checkIsExistName(name, id)) {
            throw new DuplicatedException(Constants.ErrorKey.NAME_ALREADY_EXITED, name);
        }
    }

    private boolean checkIsExistName(String name, Long id) {
        return categoryRepository.findByNameAndIdNot(name, id) != null;
    }

    public void updateCategory(Long id, CategoryPostVm categoryPostVm) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.CATEGORY_NOT_FOUND, id));
        this.validateDuplicateName(categoryPostVm.name(), id);
        this.updateCategoryAttribute(category, categoryPostVm);

        categoryRepository.save(category);
    }

    private void updateCategoryAttribute(Category category, CategoryPostVm categoryPostVm) {
        category.setName(categoryPostVm.name());
        category.setSlug(categoryPostVm.slug());
        category.setDescription(categoryPostVm.description());

        category.setPublic(categoryPostVm.isPublic());
        category.setImageId(categoryPostVm.imageId());
        if (categoryPostVm.parentId() != null) {
            Category parent = categoryRepository.findById(categoryPostVm.parentId())
                    .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.PARENT_CATEGORY_NOT_FOUND, categoryPostVm.parentId()));
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
