package com.lexuancong.product.service;

import com.lexuancong.product.model.Category;
import com.lexuancong.product.repository.CategoryRepository;
import com.lexuancong.product.viewmodel.category.CategoryPostVm;
import com.lexuancong.product.viewmodel.category.CategoryVm;
import com.lexuancong.product.viewmodel.image.ImageVm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MediaService mediaService;

    public CategoryService(CategoryRepository categoryRepository, MediaService mediaService) {
        this.categoryRepository = categoryRepository;
        this.mediaService = mediaService;
    }
    public List<CategoryVm> getCategories(String categoryName){
        List<CategoryVm> categoryVms = new ArrayList<>();
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(categoryName);
        categories.forEach(category -> {
            ImageVm image = null;
            if(category.getImageId()!=null){
                image = new ImageVm(category.getImageId(),mediaService.getMediaById(category.getImageId()).url());
            }
            Category parent =category.getParent();
            Long parentId =  parent == null ? null : parent.getId();
            CategoryVm categoryVm = CategoryVm.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .slug(category.getSlug())
                    .image(image)
                    .build();
            categoryVms.add(categoryVm);
        });
        return categoryVms;

    }

    public CategoryVm createCategory(CategoryPostVm categoryPostVm){
        this.validateDuplicateName(categoryPostVm.name(),null);
        Category category = categoryPostVm.toModel();
        if(categoryPostVm.parentId()!=null){
            Category parent = categoryRepository.findById(categoryPostVm.parentId())
                    // new exception
                    .orElseThrow(()->null);
            category.setParent(parent);
        }
        return CategoryVm.fromModel(categoryRepository.saveAndFlush(category));

    }
    private void validateDuplicateName(String name,Long id ){
        if(this.checkIsExistName(name,id)){
            // throw exception
        }
    }
    private boolean checkIsExistName(String name,Long id){
        return categoryRepository.findByNameAndIdNot(name,id)!=null;
    }

    public void updateCategory(Long id,CategoryPostVm categoryPostVm){

        Category category = categoryRepository.findById(id)
                .orElseThrow(()->null);
        this.validateDuplicateName(categoryPostVm.name(),id);
        this.updateCategoryAttribute(category,categoryPostVm);

        categoryRepository.save(category);
    }

    private void updateCategoryAttribute(Category category,CategoryPostVm categoryPostVm){
        category.setName(categoryPostVm.name());
        category.setSlug(categoryPostVm.slug());
        category.setDescription(categoryPostVm.description());
        category.setMetaDescription(categoryPostVm.metaDescription());
        category.setDisplayIndex(categoryPostVm.displayIndex());
        category.setMetaKeywords(categoryPostVm.metaKeywords());
        category.setPublic(categoryPostVm.isPublic());
        category.setImageId(categoryPostVm.imageId());
        if(categoryPostVm.parentId()!=null){
            Category parent = categoryRepository.findById(categoryPostVm.parentId())
                    .orElseThrow(()->null);
            if(this.checkIsCircularParent(category.getId(),parent)){
                // throw exception
            }
            category.setParent(parent);
        }

    }

    private boolean checkIsCircularParent(Long id,Category parent){
        if(id.equals(parent.getId())){
            return true;
        }
        if(parent.getParent()!=null){
            return checkIsCircularParent(id,parent.getParent());
        }
        return false;
    }

    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->null);
        categoryRepository.deleteById(id);
    }





}
