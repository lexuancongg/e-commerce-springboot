package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.*;
import com.lexuancong.product.model.attribute.ProductAttributeGroup;
import com.lexuancong.product.repository.*;
import com.lexuancong.product.service.internal.ImageClient;
import com.lexuancong.product.dto.image.ImagePreviewResponse;
import com.lexuancong.product.dto.product.*;
import com.lexuancong.product.dto.product.databinding.BaseProductPropertiesRequire;
import com.lexuancong.product.dto.product.producforwarehouse.ProductInfoGetResponse;
import com.lexuancong.product.dto.productoptionvalue.ProductOptionValueCreateRequest;
import com.lexuancong.product.dto.product.variants.ProductVariantGetResponse;
import com.lexuancong.product.dto.product.variants.ProductVariationCreateRequest;
import com.lexuancong.product.dto.productattribute.AttributeGroupValueGetResponse;
import com.lexuancong.product.dto.productattribute.AttributeValueGetResponse;
import com.lexuancong.share.exception.BadRequestException;
import com.lexuancong.share.exception.DuplicatedException;
import com.lexuancong.share.exception.NotFoundException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductOptionValueRepository productOptionValueRepository;
    private final SpecificProductVariantRepository specificProductVariantRepository;
    private final ImageClient imageClient;

    public ProductSummaryResponse createProduct(ProductParentCreateRequest productCreateRequest) {

        this.validateProduct(productCreateRequest, null);
        Product product = productCreateRequest.toModel();

        this.setBrandForProduct(product, productCreateRequest.brandId());

        Product parentProduct = this.productRepository.save(product);


        this.syncProductCategories(parentProduct, productCreateRequest.categoryIds());

        this.syncProductImages(parentProduct, productCreateRequest.imageIds());


        if (CollectionUtils.isEmpty(productCreateRequest.variations()) || CollectionUtils.isEmpty(productCreateRequest.productOptionValues())) {
            return ProductSummaryResponse.fromProduct(product);
        }

        // xử lý các biến thể
        List<Product> variants = this.createVariants(productCreateRequest.variations(), parentProduct);


        List<ProductOption> productOptions = this.getProductOption(productCreateRequest.productOptionValues());

        Map<Long, ProductOption> mapOptionById = productOptions.stream()
                .collect(Collectors.toMap(ProductOption::getId, Function.identity()));

        List<ProductOptionValue> productOptionValues =
                this.createProductOptionValue(productCreateRequest, parentProduct, mapOptionById);

        this.createSpecificVariant(variants, productOptionValues, productCreateRequest.variations(), mapOptionById);
        return ProductSummaryResponse.fromProduct(parentProduct);
    }

    // cần có product và ProductOption
    private void createSpecificVariant(List<Product> variationsSaved,
                                       List<ProductOptionValue> productOptionValues,
                                       List<ProductVariationCreateRequest> variantCreates,
                                       Map<Long, ProductOption> mapOptionById) {
        List<SpecificProductVariant> specificProductVariants = new ArrayList<>();
        // variationSave rồi nhưng chưa bt nó ứng với productOption Id nào và value gì => dua vao variationVm
        // variantSaved và variantVm có cùng slug => dựa vào slug để lấy
        Map<String, Product> mapProductBySlug = variationsSaved.stream()
                .collect(Collectors.toMap(Product::getSlug, Function.identity()));

        for (ProductVariationCreateRequest variantCreate : variantCreates) {
            String slugVariant = variantCreate.slug().toLowerCase();
            Product variantSaved = mapProductBySlug.get(slugVariant);
            if (variantSaved == null) {
                throw new RuntimeException();
            }
            // lặp qua từng option
            variantCreate.valueOfOptionByOptionId().forEach((optionId, optionValue) -> {
                ProductOption productOption = mapOptionById.get(optionId);
                // check xem giá trị của từng option có nằm trong productOptionValues khong
                boolean isExitOptionValue = productOptionValues.stream()
                        .anyMatch(productOptionValue -> productOptionValue.getProductOption()
                                .getId().equals(optionId) && productOptionValue.getValue().equals(optionValue));
                if (!isExitOptionValue) {
                    throw new BadRequestException(Constants.ErrorKey.PRODUCT_OPTION_VALUE_IS_NOT_FOUND);
                }
                SpecificProductVariant specificProductVariant = SpecificProductVariant.builder()
                        .productOption(productOption)
                        .product(variantSaved)
                        .value(optionValue)
                        .build();
                specificProductVariants.add(specificProductVariant);
            });


        }
        this.specificProductVariantRepository.saveAll(specificProductVariants);
    }


    private List<ProductOptionValue> createProductOptionValue(ProductParentCreateRequest productCreateRequest,
                                                              Product productParent, Map<Long, ProductOption> mapProductOption) {
        List<ProductOptionValue> productOptionValues = new ArrayList<>();
        productCreateRequest.productOptionValues().forEach(optionValue -> {
            ProductOption productOption = mapProductOption.get(optionValue.productOptionId());
            optionValue.values().forEach(value -> {
                ProductOptionValue productOptionValue = ProductOptionValue.builder()
                        .productOption(productOption)
                        .product(productParent)
                        .value(value)
                        .build();
                productOptionValues.add(productOptionValue);
            });

        });
        this.productOptionValueRepository.saveAll(productOptionValues);
        return productOptionValues;
    }

    private List<ProductOption> getProductOption(List<ProductOptionValueCreateRequest> productOptionValueRequest) {
        List<Long> optionIds = productOptionValueRequest.stream()
                .map(ProductOptionValueCreateRequest::productOptionId)
                .toList();
        if (CollectionUtils.isEmpty(optionIds)) {
            return Collections.emptyList();
        }
        return this.productOptionRepository.findAllById(optionIds);

    }


    private List<Product> createVariants(List<ProductVariationCreateRequest> variantCreateRequests,
                                         Product productParent) {

        List<Product> variants = variantCreateRequests.stream()
                .map(req -> buildVariant(req, productParent, new Product()))
                .toList();

        this.productRepository.saveAll(variants);

        List<ProductImage> allImages = IntStream.range(0, variants.size())
                .boxed()
                .filter(i -> org.apache.commons.collections4.CollectionUtils.isNotEmpty(variantCreateRequests.get(i).imageIds()))
                .flatMap(i -> variantCreateRequests.get(i).imageIds().stream()
                        .map(imageId -> ProductImage.builder()
                                .imageId(imageId)
                                .product(variants.get(i))
                                .build()))
                .toList();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(allImages)) {
            this.productImageRepository.saveAll(allImages);
        }

        return variants;

    }



    private void syncProductImages(Product product, List<Long> imageIds) {
        if (CollectionUtils.isEmpty(imageIds)) {
            this.productImageRepository.deleteByProductId(product.getId());

        }
        else if (product.getProductImages().isEmpty()) {
            List<ProductImage> productImages = imageIds.stream().map(imageId ->
                    ProductImage.builder().imageId(imageId).product(product).build()
            ).toList();
            this.productImageRepository.saveAll(productImages);

        } else {
            List<Long> imageIdsOld = product.getProductImages().stream()
                    .map(ProductImage::getImageId)
                    .sorted()
                    .toList();
            if (!org.apache.commons.collections4.CollectionUtils.isEqualCollection(imageIds, imageIdsOld)) {
                List<Long> imageIdsNew = imageIds.stream()
                        .filter(imageId -> !imageIdsOld.contains(imageId))
                        .toList();
                List<Long> imageIdsToDelete = imageIdsOld.stream()
                        .filter(imageId -> !imageIds.contains(imageId))
                        .toList();
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(imageIdsToDelete)) {
                    this.productImageRepository.deleteByImageIdInAndProductId(imageIdsToDelete, product.getId());

                }
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(imageIdsNew)) {
                    List<ProductImage> productImages =
                            imageIdsNew.stream()
                                    .map(newImageId -> ProductImage.builder().imageId(newImageId).product(product).build())
                                    .toList();
                    this.productImageRepository.saveAll(productImages);
                }
            }

        }
    }




    private void syncProductCategories(Product product, List<Long> categoryIds) {
        List<ProductCategory> productCategories = this.buildProductCategories(product, categoryIds);

        Map<Long, ProductCategory> mapProductCategory = productCategories.stream()
                .collect(Collectors.toMap(
                        productCategory -> productCategory.getCategory().getId(),
                        Function.identity()

                ));


        if (productCategories.isEmpty()) {
            if (categoryIds.isEmpty()) {
                this.productCategoryRepository.deleteByProductId(product.getId());
            }
        } else {
            List<Long> categoryIdsOld = product.getProductCategories().stream()
                    .map(productCategory -> productCategory.getCategory().getId())
                    .toList();
            List<Long> categoryIdsToDelete = categoryIdsOld.stream()
                    .filter(categoryId -> !categoryIds.contains(categoryId))
                    .toList();
            List<Long> categoIdsNew = categoryIds.stream()
                    .filter(categoryId -> !categoryIdsOld.contains(categoryId))
                    .toList();

            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(categoryIdsToDelete)) {
                this.productCategoryRepository.deleteByCategoryIdInAndProductId(categoryIdsToDelete, product.getId());
            }
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(categoIdsNew)) {
                List<ProductCategory> productCategoriesNew =  categoIdsNew.stream()
                        .map(
                                mapProductCategory::get
                        ).toList();
                this.productCategoryRepository.saveAll(productCategoriesNew);

            }
        }


    }


    // cho  cả update và create dùng chung
    private List<ProductCategory> buildProductCategories(Product product, List<Long> categoryIds) {
        if (CollectionUtils.isEmpty(categoryIds)) {
            return Collections.emptyList();
        }
        List<ProductCategory> productCategoryList = new ArrayList<>();

        // product có thể là cái mới save , có thể là cái lấy trong db ra nếu update
        List<Long> categoryIdsInDb = product.getProductCategories().stream()
                .map(productCategory -> productCategory.getCategory().getId())
                .sorted()
                .toList();
        // nếu có thay đổi thì mới thực hiện
        if (!org.apache.commons.collections4.CollectionUtils.isEqualCollection(categoryIdsInDb, categoryIds)) {
            List<Category> categories = categoryRepository.findAllById(categoryIds);
            if (categories.isEmpty()) {
                throw new BadRequestException(Constants.ErrorKey.CATEGORY_NOT_FOUND, categoryIds);
            } else if (categoryIds.size() > categories.size()) {
                List<Long> categoryIdsValid = categories.stream().map(Category::getId).toList();
                categoryIds.removeAll(categoryIdsValid);
                throw new BadRequestException(Constants.ErrorKey.CATEGORY_NOT_FOUND, categoryIds);
            } else {
                for (Category category : categories) {
                    productCategoryList.add(ProductCategory.builder()
                            .product(product)
                            .category(category).build());
                }
            }
        }

        return productCategoryList;


    }


    private void setBrandForProduct(Product product, Long brandId) {
        if (brandId != null) {
            Brand brand = this.brandRepository.findById(brandId)
                    .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.BRAND_NOT_FOUND));
            product.setBrand(brand);
        }
    }


    private void validateProduct(ProductParentCreateRequest productCreateRequest, Product productExist) {
        this.validateLengthMustGreaterThanWidth(productCreateRequest);
        this.validateUniqueProductProperties(productCreateRequest, productExist);
        this.validateDuplicateProductPropertiesBetweenVariations(productCreateRequest);

        List<Long> variationIds = productCreateRequest.variations().stream()
                .map(ProductVariationCreateRequest::id)
                .filter(Objects::nonNull)
                .toList();

        Map<Long, Product> mapVariant = this.productRepository.findAllById(variationIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity(), (product1, product2) -> new Product()));

        for (ProductVariationCreateRequest variation : productCreateRequest.variations()) {
            Product variantInDb = mapVariant.get(variation.id());
            this.validateUniqueProductProperties(variation, variantInDb);
        }

    }


    private void validateLengthMustGreaterThanWidth(ProductParentCreateRequest productVmToSave) {
        if (productVmToSave.length() < productVmToSave.width()) {
            throw new BadRequestException(Constants.ErrorKey.LENGTH_MUST_EXCEED_WIDTH);
        }
    }

    private void validateUniqueProductProperties(BaseProductPropertiesRequire baseProductProperties, Product productExist) {
        this.ensurePropertyNotExists(baseProductProperties.slug().toLowerCase(), this.productRepository::findBySlug,
                productExist, Constants.ErrorKey.SLUG_ALREADY_EXISTED);

        this.ensurePropertyNotExists(baseProductProperties.sku(), this.productRepository::findBySku,
                productExist, Constants.ErrorKey.SKU_ALREADY_EXISTED);

    }


    private void ensurePropertyNotExists(String propertyValue, Function<String, Optional<Product>> finder, Product productExist, String errorKey) {
        finder.apply(propertyValue).ifPresent(product -> {
            if (productExist == null || !product.getId().equals(productExist.getId())) {
                throw new DuplicatedException(errorKey);
            }
        });
    }


    private void validateDuplicateProductPropertiesBetweenVariations(ProductParentCreateRequest productCreateRequest) {
        Set<String> setSkus = new HashSet<>(Collections.singletonList(productCreateRequest.sku()));
        Set<String> setSlugs = new HashSet<>(Collections.singletonList(productCreateRequest.slug()));
        for (ProductVariationCreateRequest variation : productCreateRequest.variations()) {
            if (!setSkus.add(variation.sku().toLowerCase())) {
                throw new DuplicatedException(Constants.ErrorKey.SKU_ALREADY_EXISTED);
            }
            if (!setSlugs.add(variation.slug().toLowerCase())) {
                throw new DuplicatedException(Constants.ErrorKey.SLUG_ALREADY_EXISTED);
            }

        }
    }


    public void updateProduct(Long id, ProductParentCreateRequest productCreateRequest) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND, id));

        this.validateProduct(productCreateRequest, product);
        this.setBrandForProduct(product, productCreateRequest.brandId());
        this.updatePropertiesProductFromVm(product, productCreateRequest);

        // chỉnh sửa lại productCategory
//        this.updateProductCategories(product,productPostVm);
        this.syncProductCategories(product, productCreateRequest.categoryIds());


//        this.updateProductImage(product,productPostVm );
        this.syncProductImages(product, productCreateRequest.imageIds());

        List<Product> variationChillInDb = product.getChild();
        this.updateVariationInDb(productCreateRequest, variationChillInDb, product);
        // variationChillInDb được cập nhật do tham chiếu các phần tử
        List<Product> variantUpdated = this.productRepository.saveAll(variationChillInDb);


        // update laại product option
        List<ProductOption> productOptionsNew = this.getProductOption(productCreateRequest.productOptionValues());

        Map<Long, ProductOption> productOptionMapById = productOptionsNew.stream()
                .collect(Collectors.toMap(ProductOption::getId, Function.identity()));


        // cái thêm mới thì khoong có id
        List<ProductVariationCreateRequest> variationVmsNew = productCreateRequest.variations().stream()
                .filter(variationVm -> variationVm.id() == null)
                .toList();


        List<ProductOptionValue> productOptionValues = this.updateProductOptionValue(product, productOptionMapById, productCreateRequest);
        // cập nhật lại combination cho các bien the, trường hợp thay đổi giá trị hoặc thay đổi option
        this.updateProductOptionCombination(productCreateRequest.variations(), variationChillInDb,
                variantUpdated, productOptionValues, productOptionMapById);

        if (org.apache.commons.collections4.CollectionUtils.isEmpty(variationVmsNew)) {
            return;
        }
        //create variation và create combination cho variation mới => variationVm này đã qua được isvalid
        List<Product> variationSaved = this.createVariants(variationVmsNew, product);
        // create  combination

        this.createSpecificVariant(variationSaved, productOptionValues, variationVmsNew, productOptionMapById);


    }

    private void updateProductOptionCombination(List<ProductVariationCreateRequest> variationVms,
                                                List<Product> variationChill,
                                                List<Product> variantUpdated,
                                                List<ProductOptionValue> productOptionValues,
                                                Map<Long, ProductOption> productOptionMapById) {
        List<Long> variationIds = variationChill.stream().map(Product::getId).toList();
        this.specificProductVariantRepository.deleteAllByProductIdIn(variationIds);
        this.createSpecificVariant(variantUpdated, productOptionValues, variationVms, productOptionMapById);

    }

    private List<ProductOptionValue> updateProductOptionValue(Product product, Map<Long, ProductOption> productOptionMapById,
                                                              ProductParentCreateRequest productCreateRequest) {
        this.productOptionValueRepository.deleteAllByProductId(product.getId());
        return this.createProductOptionValue(productCreateRequest, product, productOptionMapById);


    }

//    private void updateProductImage(Product product , ProductPostVm productPostVm){
//        List<ProductImage> productImages = product.getProductImages();
//        List<Long> imageIdsOld = productImages.stream()
//                .map(ProductImage::getImageId)
//                .toList();
//        List<Long> imageIdsNew = productPostVm.imageIds();
//        if(!org.apache.commons.collections4.CollectionUtils.isEqualCollection(imageIdsOld,imageIdsNew)){
//            this.perFormUpdateProductImage(product,imageIdsNew,imageIdsOld,productImages);
//        }
//
//    }


//
//    private void updateProductCategories(Product product , ProductPostVm productPostVm){
//        List<ProductCategory> productCategoriesOld = product.getProductCategories();
//        List<Long> categoryIdsOld = productCategoriesOld.stream()
//                .map(productCategoryOld ->productCategoryOld.getCategory().getId() )
//                .sorted()
//                .toList();
//        List<Long> categoryIdsNew = productPostVm.categoryIds().stream().sorted().toList();
//        if(!org.apache.commons.collections4.CollectionUtils.isEqualCollection(categoryIdsOld,categoryIdsNew)){
//            this.perFormUpdateProductCategories(product,categoryIdsNew,productCategoriesOld,categoryIdsOld);
//        }
//
//
//    }


    private Product buildVariant(ProductVariationCreateRequest variationCreateRequest,
                                 Product productParent, Product variationInit) {
        variationInit.setName(variationCreateRequest.name());
        variationInit.setAvatarImageId(variationCreateRequest.avatarImageId());
        variationInit.setSlug(variationCreateRequest.slug());
        variationInit.setSku(variationCreateRequest.sku());
        variationInit.setPrice(variationCreateRequest.price());
        variationInit.setPublic(productParent.isPublic());
        // khi create
        if (variationInit.getId() == null) {
            variationInit.setParent(productParent);
        }
        return variationInit;


    }


    private void perFormUpdateValueForVariation(Product mainProduct, ProductVariationCreateRequest variationPutVm, Product variationInit) {
        this.buildVariant(variationPutVm, mainProduct, variationInit);

    }

    private void updateVariationInDb(ProductParentCreateRequest productCreateRequest, List<Product> variationInDbs, Product mainProduct) {
        Map<Long, Product> mapVariationSaved = variationInDbs.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        productCreateRequest.variations().forEach(variationVm -> {
//            for(Product variationChill : variationChillInDbs){
//                if(variationChill.getId().equals(variationVm.id())){
//                    this.perFormUpdateValueForVariation(variationChill,variationVm);
//                    break;
//                }
//            }
            Product variation = mapVariationSaved.get(variationVm.id());
            if (variation != null) {
                this.perFormUpdateValueForVariation(mainProduct, variationVm, variation);
                this.syncProductImages(variation, variationVm.imageIds());
            }


        });

    }


//
//    private void perFormUpdateProductImage(Product product,List<Long> imageIdsNew,
//                                    List<Long> imageIdsOld,List<ProductImage> productImagesOld){
//        Set<Long> setImageIdsOld = new HashSet<>(imageIdsOld);
//        Set<Long> setImageIdsNew = new HashSet<>(imageIdsNew);
//        // tìm cái cần xóa => có trong cũ không có trong mới
//        List<ProductImage> productImagesToRemove = productImagesOld.stream()
//                .filter(productImageOld -> !setImageIdsNew.contains(productImageOld.getImageId()))
//                .toList();
//        // tìm cái có trong cái mới mà cái cũ không cs
//        List<Long> imageIdsToAdd =  imageIdsNew.stream()
//                .filter(imageId -> !setImageIdsOld.contains(imageId))
//                .toList();
//        List<ProductImage> productImages = this.syncProductImages(product,imageIdsToAdd);
//        this.productImageRepository.deleteAllInBatch(productImagesToRemove);
//        this.productImageRepository.saveAll(productImages);
//
//    }


    private void updatePropertiesProductFromVm(Product product, ProductParentCreateRequest productCreateRequest) {
        product.setName(productCreateRequest.name());
        product.setSlug(productCreateRequest.slug());
        product.setAvatarImageId(productCreateRequest.avatarImageId());
        product.setDescription(productCreateRequest.description());
        product.setShortDescription(productCreateRequest.shortDescription());
        product.setSpecifications(productCreateRequest.specification());
        product.setSku(productCreateRequest.sku());
        product.setDescription(productCreateRequest.description());
        product.setPrice(productCreateRequest.price());
        product.setFeature(productCreateRequest.isFeature());
        // lombok tự động đổi tên setter và getter
        product.setOrderEnable(productCreateRequest.isOrderEnable());
        product.setPublic(productCreateRequest.isPublic());
        product.setShownSeparately(productCreateRequest.isShownSeparately());
        product.setInventoryTracked(productCreateRequest.isInventoryTracked());
        product.setWidth(productCreateRequest.width());
        product.setHeight(productCreateRequest.height());
        product.setLength(productCreateRequest.length());
        product.setWeight(productCreateRequest.weight());

    }


    // update trường hợp có id danh mục cũ và id danh mục mới trun nhau nhiều để cải thện hiệu xuất sau
    private void perFormUpdateProductCategories(Product product, List<Long> categoryIdsNew,
                                                List<ProductCategory> productCategoriesOld,
                                                List<Long> categoryIdsOld) {
        Set<Long> setCategoryIdsOld = new HashSet<>(categoryIdsOld);
        Set<Long> setCategoryIdsNew = new HashSet<>(categoryIdsNew);
        // ttìm cái cần thêm
        List<Long> categoryIdsToAdd = categoryIdsNew.stream().filter(categoryId -> !setCategoryIdsOld.contains(categoryId))
                .toList();
        // tìm cái cần xóa
        List<ProductCategory> productCategoriesToRemove = productCategoriesOld.stream()
                .filter(productCategory -> !setCategoryIdsNew.contains(productCategory.getCategory().getId()))
                .toList();

        // tối ưu ở đây

        List<ProductCategory> productCategoriesNew = this.buildProductCategories(product, categoryIdsToAdd);
        this.productCategoryRepository.deleteAllInBatch(productCategoriesToRemove);
        this.productCategoryRepository.saveAll(productCategoriesNew);


    }


    public ProductPreviewPagingGetResponse getFeaturedProductsPaging(int pageIndex, int pageSize) {

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Product> productPage = this.productRepository.findAllByFeatureIsTrueAndShownSeparatelyIsTrueAndPublicIsTrueOrderByIdAsc(pageable);
        List<Product> productsContent = productPage.getContent();
        List<ProductPreviewGetResponse> productPreviewPayload = productsContent.stream()
                .map(product -> {
                    String avatarUrl = this.imageClient.getImageById(product.getAvatarImageId()).url();
                    return new ProductPreviewGetResponse(product.getId(), product.getName(), product.getSlug(), product.getPrice(), avatarUrl);
                }).toList();

        return new ProductPreviewPagingGetResponse(
                productPreviewPayload, pageIndex, pageSize,
                (int) productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()
        );

    }


    // lấy thông tin chi tiết về sản phâ
    public ProductDetailGetResponse getProductDetailBySlug(String slug) {
        Product product = this.productRepository.findBySlugAndPublicIsTrue(slug)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND, slug));
        String avatarUrl = this.imageClient.getImageById(product.getAvatarImageId())
                .url();
        List<Long> imageIds = product.getProductImages().stream().map(ProductImage::getImageId)
                .toList();
        List<String> productImageUrls = this.imageClient.getImageByIds(imageIds)
                .stream().map(ImagePreviewResponse::url)
                .toList();

        List<ProductAttributeValue> productAttributeValues = product.getProductAttributeValues();
        List<AttributeGroupValueGetResponse> attributeGroupValueGetResponses = new ArrayList<>();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(productAttributeValues)) {
            List<ProductAttributeGroup> productAttributeGroups = productAttributeValues.stream()
                    .map(productAttributeValue ->
                            productAttributeValue.getProductAttribute().getGroup())
                    .filter(Objects::nonNull)
                    // loại bỏ ptu trùng lặp dựa trên equal
                    .distinct()
                    .toList();

            productAttributeGroups.forEach(productAttributeGroup -> {
                List<AttributeValueGetResponse> attributeValueGetResponses = new ArrayList<>();
                productAttributeValues.forEach(productAttributeValue -> {
                    if (productAttributeValue.getProductAttribute().getGroup().getId().equals(productAttributeGroup.getId())) {
                        String attributeName = productAttributeValue.getProductAttribute().getName();
                        String value = productAttributeValue.getValue();
                        AttributeValueGetResponse attributeValueGetResponse = new AttributeValueGetResponse(attributeName, value);
                        attributeValueGetResponses.add(attributeValueGetResponse);
                    }
                });
                String attributeGroupName = productAttributeGroup.getName();
                AttributeGroupValueGetResponse attributeGroupValueGetResponse = new AttributeGroupValueGetResponse(attributeGroupName, attributeValueGetResponses);
                attributeGroupValueGetResponses.add(attributeGroupValueGetResponse);
            });

        }
        return new ProductDetailGetResponse(
                product.getId(),
                product.getName(),
                product.getBrand().getName(),
                product.getProductCategories().stream().map(productCategory -> productCategory.getCategory().getName())
                        .toList(),
                attributeGroupValueGetResponses,
                product.getShortDescription(),
                product.getDescription(),
                product.getSpecifications(),
                product.getPrice(),
                product.isHasOptions(),
                avatarUrl,
                product.isFeature(),
                productImageUrls,
                product.isOrderEnable(),
                product.isPublic()
        );

    }


    // xóa mềm tránh maats dữ liệu cho các bảng khác
    public void deleteProduct(Long id) {
        Product product = this.productRepository.findById(id).orElseThrow(() -> new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND, id));
        product.setPublic(false);
        // check xem có phải là biến thể không
        if (!Objects.isNull(product.getParent())) {
            // xóa các combination của biến thể này
            List<SpecificProductVariant> combinations = this.specificProductVariantRepository
                    .findAllByProduct(product);
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(combinations)) {
                this.specificProductVariantRepository.deleteAll(combinations);
            }
        }
        this.productRepository.save(product);


    }



    public ProductPagingGetResponse getProductsByCategorySlug(int pageIndex, int pageSize, String categorySlug) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Category category = this.categoryRepository.findBySlug(categorySlug)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.CATEGORY_NOT_FOUND, categorySlug));


        Page<ProductCategory> productCategoryPage = this.productCategoryRepository.findAllByCategory(category, pageable);
        List<Product> productContents = productCategoryPage.getContent().stream()
                .map(ProductCategory::getProduct)
                .toList();

        List<ProductPreviewGetResponse> productPreviewGetResponses = productContents.stream()
                .map(product -> {
                    String avatarUrl = this.imageClient.getImageById(product.getAvatarImageId()).url();
                    return new ProductPreviewGetResponse(
                            product.getId(), product.getName(), product.getSlug(), product.getPrice(), avatarUrl
                    );
                }).toList();
        return new ProductPagingGetResponse(
                productPreviewGetResponses,
                pageIndex,
                pageSize,
                (int) productCategoryPage.getTotalElements(),
                productCategoryPage.getTotalPages(),
                productCategoryPage.isLast()
        );


    }


    // fix sau: hiệu năng không tốt vì phải lấy ht toa bộ sp db
    public List<ProductPreviewGetResponse> getProductFeaturedMakeSlide() {
        List<Product> productsFeatured = this.productRepository.findAllByFeatureIsTrue();
        Collections.shuffle(productsFeatured);
        return productsFeatured.stream().limit(10)
                .map(product -> {
                    return new ProductPreviewGetResponse(
                            product.getId(), product.getName(), product.getSlug(), product.getPrice(),
                            imageClient.getImageById(product.getAvatarImageId()).url()
                    );
                })
                .toList();
    }


    public List<ProductPreviewGetResponse> getProductsByIds(List<Long> ids) {
        List<Product> products = this.productRepository.findAllByIdIn(ids);
        return products.stream().map(product -> {
            String avatarUrl = this.imageClient.getImageById(product.getAvatarImageId()).url();
            // nếu avataurl = null mà không có parent nữa thì trả về null luôn
            if (StringUtils.isEmpty(avatarUrl) && Objects.nonNull(product.getParent())) {
                Optional<Product> parentProduct = this.productRepository.findById(product.getParent().getId());
                avatarUrl = parentProduct.map(item ->
                                this.imageClient.getImageById(item.getAvatarImageId()).url())
                        .orElse("");

            }
            return new ProductPreviewGetResponse(
                    product.getId(),
                    product.getName(),
                    product.getSlug(),
                    product.getPrice(),
                    avatarUrl

            );

        }).toList();
    }

    public ProductPagingGetResponse getProductByMultiParams(int pageIndex, int pageSize, String productName, String categorySlug, Double startPrice, Double endPrice) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Product> productPage = this.productRepository.findByProductNameAndCategorySlugAndPriceBetween(
                productName.trim().toLowerCase(),
                categorySlug.trim(), startPrice, endPrice, pageable
        );
        List<Product> products = productPage.getContent();
        List<ProductPreviewGetResponse> contentPayload = products.stream()
                .map(product -> {
                    String avatarUrl = this.imageClient.getImageById(product.getAvatarImageId()).url();
                    return new ProductPreviewGetResponse(
                            product.getId(),
                            product.getName(),
                            product.getSlug(),
                            product.getPrice(),
                            avatarUrl
                    );

                }).toList();

        return new ProductPagingGetResponse(
                contentPayload,
                pageIndex,
                pageSize,
                (int) productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()

        );

    }

    public List<ProductVariantGetResponse> getProductVariationsByParentId(Long parentId) {
        Product parentProduct = this.productRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND,parentId));
//       // autoboxing : convert lên nếu là bolean
        if (Boolean.TRUE.equals(parentProduct.isHasOptions())) {
            List<Product> productVariations = parentProduct.getChild().stream()
                    .filter(Product::isPublic)
                    .toList();
            return productVariations.stream()
                    .map(variant -> {
                        List<SpecificProductVariant> specificProductVariants = this.specificProductVariantRepository
                                .findAllByProduct(variant);
                        Map<Long, String> optionsValues = specificProductVariants.stream()
                                .collect(
                                        Collectors.toMap(
                                                specificProductVariant -> specificProductVariant.getProductOption().getId(),
                                                SpecificProductVariant::getValue
                                        )
                                );
                        String avatarUrl = null;
                        if (variant.getAvatarImageId() != null) {
                            avatarUrl = this.imageClient.getImageById(variant.getAvatarImageId()).url();
                        }
                        List<Long> imageIds = variant.getProductImages().stream().map(ProductImage::getId).toList();
                        List<ImagePreviewResponse> productImages = this.imageClient.getImageByIds(imageIds);
                        return new ProductVariantGetResponse(
                                variant.getId(),
                                variant.getName(),
                                variant.getSlug(),
                                variant.getSku(),
                                variant.getPrice(),
                                avatarUrl,
                                productImages,
                                optionsValues
                        );


                    }).toList();

        }
        return Collections.emptyList();


    }

    public List<ProductInfoGetResponse> filterProductInProductIdsByNameOrSku(List<Long> productIds, String name, String sku){
        List<Product> products = this.productRepository.filterProductInProductIdsByNameOrSku(productIds,name,sku);
        return products.stream().map(ProductInfoGetResponse::fromProduct)
                .toList();
    }


    public List<ProductCheckoutPreviewVm> getProductCheckouts(List<Long> productIdsInCheckoutItems){
        List<Product> products = this.productRepository.findAllByIdInAndPublicIsTrue(productIdsInCheckoutItems);
        return products.stream()
                .map(ProductCheckoutPreviewVm::fromModel)
                .toList();

    }


    public void subtractProductQuantityAfterOder(List<ProductSubtractQuantityRequest> productSubtractQuantityRequests){
        List<Long> productIds = productSubtractQuantityRequests.stream()
                .map(ProductSubtractQuantityRequest::productId)
                .toList();
        List<Product> products = this.productRepository.findAllByIdInAndPublicIsTrue(productIds);

        Map<Long,Long> mapQuantitySubtractByProductId = productSubtractQuantityRequests.stream()
                .collect(
                        Collectors.toMap(
                                ProductSubtractQuantityRequest::productId ,
                                ProductSubtractQuantityRequest::quantity,
                                Long::sum
                        )
                );



        BiFunction<Long ,Long, Long> functionRecalculate = (inventoryQuantityCurrent, quantitySubtract)->{
            Long result = inventoryQuantityCurrent - quantitySubtract;
            return result <=0 ? 0 : result;
        };

        for (Product product : products) {
            if(product.isInventoryTracked()){
                Long inventoryQuantityNew  = this.getInventoryQuantityNewAfterCalculate(product,mapQuantitySubtractByProductId,functionRecalculate);
                product.setInventoryQuantity(inventoryQuantityNew);
            }
        }
        this.productRepository.saveAll(products);
    }

    public Long getInventoryQuantityNewAfterCalculate(Product product,Map<Long,Long> mapQuantitySubtractByProductId,
                                                      BiFunction<Long,Long,Long> functionRecalculate  ){
        Long quantitySubtract = mapQuantitySubtractByProductId.get(product.getId());
        Long inventoryQuantityCurrent =  product.getInventoryQuantity();
        return  functionRecalculate.apply(inventoryQuantityCurrent,quantitySubtract);


    }

}
