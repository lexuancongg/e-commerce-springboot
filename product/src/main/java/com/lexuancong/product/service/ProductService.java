package com.lexuancong.product.service;

import com.lexuancong.product.model.*;
import com.lexuancong.product.model.attribute.ProductAttributeGroup;
import com.lexuancong.product.repository.*;
import com.lexuancong.product.service.internal.MediaService;
import com.lexuancong.product.viewmodel.image.ImageVm;
import com.lexuancong.product.viewmodel.product.*;
import com.lexuancong.product.viewmodel.product.databinding.BaseProductPropertiesRequire;
import com.lexuancong.product.viewmodel.product.databinding.ProductOptionPropertyRequire;
import com.lexuancong.product.viewmodel.product.databinding.ProductPropertiesRequire;
import com.lexuancong.product.viewmodel.product.databinding.ProductVariationPropertiesRequire;
import com.lexuancong.product.viewmodel.productattribute.AttributeGroupValueVm;
import com.lexuancong.product.viewmodel.productattribute.AttributeValueVm;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private final ProductOptionCombinationRepository productOptionCombinationRepository;
    private final MediaService mediaService;

    public ProductSummaryVm createProduct(ProductPostVm productPostVm){
        this.validateProduct(productPostVm);
        Product product = productPostVm.toModel();
        this.setBrandForProduct(product, productPostVm.brandId());
        Product productSaved =  this.productRepository.save(product);

        List<ProductCategory> productCategoryList = this.setProductCategories(productSaved,productPostVm.categoryIds());
        List<ProductImage> productImageList = this.setProductImages(productSaved,productPostVm.imageIds());

        this.productCategoryRepository.saveAll(productCategoryList);
        this.productImageRepository.saveAll(productImageList);


        if(CollectionUtils.isEmpty(productPostVm.variations()) || CollectionUtils.isEmpty(productPostVm.productOptionValues())){
            return ProductSummaryVm.fromModel(product);
        }

        // xử lý các biến thể
        List<Product> variationSaved = this.createVariationsFromVm(productPostVm.variations(),productSaved);

        // lưu dữ liệu bảng product optionValue cho  product => cần lấy đuược product otion tương ứng từng id => Map
        List<ProductOption> productOptions = this.getProductOption(productPostVm.productOptionValues());
        Map<Long,ProductOption> mapOptionById =  productOptions.stream()
                .collect(Collectors.toMap(ProductOption::getId, Function.identity()));
        List<ProductOptionValue> productOptionValues =
                this.createProductOptionValue(productPostVm,productSaved,mapOptionById);
        this.createProductOptionCombination(variationSaved,productOptionValues,productPostVm.variations(),mapOptionById);
        return ProductSummaryVm.fromModel(productSaved);


    }

    // cần có product và ProductOption
    private void createProductOptionCombination(List<Product> variationsSaved,
                                                List<ProductOptionValue> productOptionValues,
                                                List<? extends ProductVariationPropertiesRequire> variationVms,
                                                Map<Long,ProductOption> mapOptionById){
        List<ProductOptionCombination> productOptionCombinations = new ArrayList<>();
        // variationSave rồi nhưng chưa bt nó ứng với productOption Id nào và value gì => dua vao variationVm
        // variantSaved và variantVm có cùng slug => dựa vào slug để lấy
        Map<String,Product> mapProductBySlug = variationsSaved.stream()
                .collect(Collectors.toMap(Product::getSlug,Function.identity()));
        for(ProductVariationPropertiesRequire variationVm : variationVms){
            String slugVariant = variationVm.slug().toLowerCase();
            Product variantSaved = mapProductBySlug.get(slugVariant);
            if(variantSaved == null){
                // chứng tỏ lưu lần trước bị lỗi
                throw new RuntimeException();
            }
            // lặp qua từng option
            variationVm.valueOfOptionByOptionId().forEach((optionId,optionValue)->{
                ProductOption productOption = mapOptionById.get(optionId);
                // check xem giá trị của từng option có nằm trong productOptionValues khong
                boolean  isExitOptionValue = productOptionValues.stream()
                        .anyMatch(productOptionValue -> productOptionValue.getProductOption()
                                .getId().equals(optionId) && productOptionValue.getValue().equals(optionValue));
                if(!isExitOptionValue){
                    throw new RuntimeException();
                }
                ProductOptionCombination productOptionCombination = ProductOptionCombination.builder()
                        .productOption(productOption)
                        .product(variantSaved)
                        .value(optionValue)
                        .build();
                productOptionCombinations.add(productOptionCombination);
            });


        }
        this.productOptionCombinationRepository.saveAll(productOptionCombinations);
    }
    private List<ProductOptionValue> createProductOptionValue(ProductPostVm productPostVm ,
                                                              Product mainProduct ,Map<Long,ProductOption> productOptionMapById){
        List<ProductOptionValue> productOptionValues = new ArrayList<>();
        productPostVm.productOptionValues().forEach(optionValue ->{
            ProductOption productOption = productOptionMapById.get(optionValue.productOptionId());
            optionValue.values().forEach(value -> {
                ProductOptionValue productOptionValue = ProductOptionValue.builder()
                        .productOption(productOption)
                        .product(mainProduct)
                        .value(value)
                        .build();
                productOptionValues.add(productOptionValue);
            });

        });
        this.productOptionValueRepository.saveAll(productOptionValues);
        return productOptionValues;


    }

    private List<ProductOption> getProductOption(List<? extends ProductOptionPropertyRequire> productOptionValueVms){
        List<Long> optionIds  = productOptionValueVms.stream()
                .map(ProductOptionPropertyRequire::productOptionId)
                .toList();
        if(CollectionUtils.isEmpty(optionIds)) return Collections.emptyList();
        List<ProductOption> productOptions = this.productOptionRepository.findAllById(optionIds);
        return productOptions;

    }




    private List<Product> createVariationsFromVm(List<? extends ProductVariationPropertiesRequire> variationPostVmList,
                                                 Product mainProduct) {

        // đầu tiên sử lý hình ảnh
        List<ProductImage> allProductImages = new ArrayList<>();
        List<Product> variations = variationPostVmList.stream()
                .map(variationVm ->{
                    Product productVariation  = this.buildProductVariationFormVm(variationVm, mainProduct);
                    // quay lại lưu hình ảnh như sản phẩm chính
                    List<ProductImage> variationImages =
                            this.setProductImages(productVariation,variationVm.imageIds());
                    allProductImages.addAll(variationImages);

                    return productVariation;

                }  )
                .toList();

        // lưu lại các bieens thể sp
        List<Product> variationsSaved = this.productRepository.saveAll(variations);

        // lưu cho bảng productImage
        this.productImageRepository.saveAll(allProductImages);
        return variationsSaved;


    }


    private Product buildProductVariationFormVm(ProductVariationPropertiesRequire variationPropertiesVm,
                                                Product mainProduct){
        return Product.builder()
                .name(variationPropertiesVm.name())
                .avatarImageId(variationPropertiesVm.avatarImageId())
                .slug(variationPropertiesVm.slug().toLowerCase())
                .sku(variationPropertiesVm.sku())
                .gtin(variationPropertiesVm.gtin())
                .price(variationPropertiesVm.price())
                .isPublic(mainProduct.isPublic())
                .parent(mainProduct)
                .build();

    }

    // ds productImage lưu lại khi theem sp
    private List<ProductImage> setProductImages(Product product , List<Long> imageIds){
        List<ProductImage> productImages = new ArrayList<>();
        if(CollectionUtils.isEmpty(imageIds)){
            return productImages;
        }
        productImages = imageIds.stream()
                .map(imageId -> ProductImage.builder().imageId(imageId).product(product)
                        .build())
                .toList();
        return productImages;
    }

    private List<ProductCategory> setProductCategories(Product product,List<Long> categoryIds){
        if(CollectionUtils.isEmpty(categoryIds)){
            return Collections.emptyList();
        }
        List<ProductCategory> productCategoryList = new ArrayList<>();
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        if(categories.isEmpty()){
            // throw exception
        }else {
            for (Category category : categories){
                productCategoryList.add(
                        ProductCategory.builder().category(category)
                                .product(product)
                                .build()
                );

            }
        }
        return productCategoryList;


    }


    private void setBrandForProduct(Product product , Long brandId){
        if(brandId != null){
            Brand brand = this.brandRepository.findById(brandId)
                    .orElseThrow(()-> new RuntimeException());
            product.setBrand(brand);
        }
    }




    // dành cho create
    private <T extends ProductVariationPropertiesRequire > void validateProduct(ProductPropertiesRequire<T> productVmToSave){
        this.validateProduct(productVmToSave,null);
    }


    //  cả update và create và update đều validate => gộp chung hàm
    private <T extends ProductVariationPropertiesRequire> void validateProduct(ProductPropertiesRequire<T> productVmToSave, Product existingProduct){
        this.validateLengthMustGreaterThanWidth(productVmToSave);
        this.validateExistingProductProperties(productVmToSave,existingProduct);
        this.validateDuplicateProductPropertiesBetweenVariations(productVmToSave);


        // valid datete thuộc tính của varian cos bị trùng lặp trong db không
        List<Long> variationIds = productVmToSave.variations().stream()
                .map(BaseProductPropertiesRequire::id)
                .filter(Objects::nonNull)
                .toList();

        // variant tuong ung trong db vs tung id
        Map<Long,Product> mapVariationsSaved = this.productRepository.findAllById(variationIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity(),(product1, product2) -> new Product() ));

        for(BaseProductPropertiesRequire variation : productVmToSave.variations()){
            Product variantExitedInDb = mapVariationsSaved.get(variation.id());
            this.validateExistingProductProperties(variation,variantExitedInDb);
        }

    }


    // check xem chieeuf dài và chiều rộng đươc nhập hhowpjp lệ không => tính toán tiền vận chuyển sau nay
    private <T extends ProductVariationPropertiesRequire> void validateLengthMustGreaterThanWidth(ProductPropertiesRequire<T> productVmToSave){
        if(productVmToSave.length() < productVmToSave.width()) {
            // bắn ra ngoại lệ
        }
    }

    // check xem các thuộc tinhs của sp có bị trùng lặp trong db không
    private void validateExistingProductProperties(BaseProductPropertiesRequire baseProductProperties,Product existingProduct){
        this.checkPropertyExisted(baseProductProperties.slug().toLowerCase(),this.productRepository::findBySlug,existingProduct);
        if (StringUtils.isNotEmpty(baseProductProperties.gtin())){
            this.checkPropertyExisted(baseProductProperties.gtin(),this.productRepository::findByGtin,existingProduct);
        }
        this.checkPropertyExisted(baseProductProperties.sku(),this.productRepository::findBySku,existingProduct);
    }

    // cách thức check các thuộc tính tuương tự nhau (cần tên thuộc tính , đưa method từ jpa) => hàm
    private void checkPropertyExisted(String propertyValue, Function<String, Optional<Product>> finder ,Product existingProduct){
        finder.apply(propertyValue).ifPresent(product -> {
            if(existingProduct == null || !product.getId().equals(existingProduct.getId()) ){
                // throw exception
            }
        });
    }



    // chưa check trùng lặp các thuộc tính con trong db => làm sau
    private <T extends ProductVariationPropertiesRequire> void validateDuplicateProductPropertiesBetweenVariations(ProductPropertiesRequire<T> productVmToSave){
        // check trùng lặp slug , sku, gtins cho các biến thể con
        Set<String> setSkus = new HashSet<>(Collections.singletonList(productVmToSave.sku()));
        Set<String> setGtins = new HashSet<>(Collections.singletonList(productVmToSave.gtin()));
        Set<String> setSlugs = new HashSet<>(Collections.singletonList(productVmToSave.slug()));
        for (BaseProductPropertiesRequire variation : productVmToSave.variations()) {
            if(!setSkus.add(variation.sku().toLowerCase())){
                // throw exception
            }
            if(!setSlugs.add(variation.slug().toLowerCase())){
                // throw exception
            }
            if(StringUtils.isNotEmpty(variation.gtin()) && !setGtins.add(variation.gtin())){
                // throw exception

            }
        }


    }

    public void updateProduct(Long id, ProductPostVm productPostVm){
        Product product = this.productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException());
        this.validateProduct(productPostVm,product);
        this.setBrandForProduct(product,productPostVm.brandId());
        // chỉnh sửa lại productCategory
        this.updateProductCategories(product,productPostVm);

        this.updatePropertiesProductFromVm(product,productPostVm);

        this.updateProductImage(product,productPostVm );
        List<Product> variationChillInDb = product.getChild();
        this.updateValueVariationChillInDb(productPostVm, variationChillInDb);
        List<Product> variantUpdated = this.productRepository.saveAll(variationChillInDb);

        // tiếp theo sử lý phần productOptionValues cho ProductOptionValue
        List<ProductOption> productOptions = this.getProductOption(productPostVm.productOptionValues());
        Map<Long,ProductOption> productOptionMapById = productOptions.stream()
                .collect(Collectors.toMap(ProductOption::getId, Function.identity()));

        // cái thêm mới
        List<? extends ProductVariationPropertiesRequire>  variationVmsNew = productPostVm.variations().stream()
                .filter(variationVm -> variationVm.id() == null )
                .toList();

        productPostVm.variations().removeAll(variationVmsNew);

        List<ProductOptionValue> productOptionValues = this.updateProductOptionValue(product,productOptionMapById,productPostVm);
        // cập nhật lại combination cho các bien the
        this.updateProductOptionCombination(productPostVm.variations(),variationChillInDb,
                variantUpdated,productOptionValues,productOptionMapById);

        if(org.apache.commons.collections4.CollectionUtils.isEmpty(variationVmsNew)){
            return;
        }
        //create variation và create combination cho variation mới => variationVm này đã qua được isvalid
        List<Product> variationSaved = this.createVariationsFromVm(variationVmsNew,product);
        // create  combination

        this.createProductOptionCombination(variationSaved,productOptionValues,variationVmsNew,productOptionMapById);


    }
    private void updateProductOptionCombination(List<? extends ProductVariationPropertiesRequire> variationVms,
                                                List<Product> variationChill,
                                                List<Product> variantUpdated,
                                                List<ProductOptionValue> productOptionValues,
                                                Map<Long,ProductOption> productOptionMapById) {
        List<Long> variationIds = variationChill.stream().map(Product::getId).toList();
        this.productOptionCombinationRepository.deleteAllByProductIdIn(variationIds);
        this.createProductOptionCombination(variantUpdated,productOptionValues,variationVms,productOptionMapById);

    }
    private List<ProductOptionValue> updateProductOptionValue(Product product , Map<Long,ProductOption> productOptionMapById,
                                          ProductPostVm productPostVm){
         this.productOptionValueRepository.deleteAllByProductId(product.getId());
         return this.createProductOptionValue(productPostVm,product,productOptionMapById);



    }

    private void updateProductImage(Product product , BaseProductPropertiesRequire productPostVm){
        List<ProductImage> productImages = product.getProductImages();
        List<Long> imageIdsOld = productImages.stream()
                .map(ProductImage::getImageId)
                .toList();
        List<Long> imageIdsNew = productPostVm.imageIds();
        if(!org.apache.commons.collections4.CollectionUtils.isEqualCollection(imageIdsOld,imageIdsNew)){
            this.perFormUpdateProductImage(product,imageIdsNew,imageIdsOld,productImages);
        }

    }

    private void updateProductCategories(Product product , ProductPostVm productPostVm){
        List<ProductCategory> productCategoriesOld = product.getProductCategories();
        List<Long> categoryIdsOld = productCategoriesOld.stream()
                .map(productCategoryOld ->productCategoryOld.getCategory().getId() )
                .toList();
        List<Long> categoryIdsNew = productPostVm.categoryIds();
        if(!org.apache.commons.collections4.CollectionUtils.isEqualCollection(categoryIdsOld,categoryIdsNew)){
            this.perFormUpdateProductCategories(product,categoryIdsNew,productCategoriesOld,categoryIdsOld);
        }


    }

    private  void updateValueVariationChillInDb(ProductPostVm productPostVm,  List<Product> variationChillInDbs ){
        productPostVm.variations().forEach(variationVm->{
            for(Product variationChill : variationChillInDbs){
                if(variationChill.getId().equals(variationVm.id())){
                    this.perFormUpdateValueForVariation(variationChill,variationVm);
                    break;
                }
            }
        });

    }

    private void perFormUpdateValueForVariation(Product variation, ProductVariationPropertiesRequire variationPutVm ){
        variation.setName(variationPutVm.name());
        variation.setSlug(variationPutVm.slug());
        variation.setSku(variationPutVm.sku());
        variation.setGtin(variationPutVm.gtin());
        variation.setPrice(variationPutVm.price());
        variation.setAvatarImageId(variationPutVm.avatarImageId());
        // nếu mà insert vào mỗi for thì hiệu xuất giảm
        this.updateProductImage(variation,variationPutVm);






    }


    private void perFormUpdateProductImage(Product product,List<Long> imageIdsNew,
                                    List<Long> imageIdsOld,List<ProductImage> productImagesOld){
        Set<Long> setImageIdsOld = new HashSet<>(imageIdsOld);
        Set<Long> setImageIdsNew = new HashSet<>(imageIdsNew);
        // tìm cái cần xóa => có trong cũ không có trong mới
        List<ProductImage> productImagesToRemove = productImagesOld.stream()
                .filter(productImageOld -> !setImageIdsNew.contains(productImageOld.getImageId()))
                .toList();
        // tìm cái có trong cái mới mà cái cũ không cs
        List<Long> imageIdsToAdd =  imageIdsNew.stream()
                .filter(imageId -> !setImageIdsOld.contains(imageId))
                .toList();
        List<ProductImage> productImages = this.setProductImages(product,imageIdsToAdd);
        this.productImageRepository.deleteAllInBatch(productImagesToRemove);
        this.productImageRepository.saveAll(productImages);

    }


    private void updatePropertiesProductFromVm(Product product,ProductPostVm productPostVm){
        product.setName(productPostVm.name());
        product.setSlug(productPostVm.slug());
        product.setAvatarImageId(productPostVm.avatarImageId());
        product.setDescription(productPostVm.description());
        product.setShortDescription(productPostVm.shortDescription());
        product.setSpecifications(productPostVm.specification());
        product.setSku(productPostVm.sku());
        product.setDescription(productPostVm.description());
        product.setGtin(productPostVm.gtin());
        product.setPrice(productPostVm.price());
        product.setFeature(productPostVm.isFeature());
        // lombok tự động đổi tên setter và getter
        product.setOrderEnable(productPostVm.isOrderEnable());
        product.setPublic(productPostVm.isPublic());
        product.setShownSeparately(productPostVm.isShownSeparately());
        product.setInventoryTracked(productPostVm.isInventoryTracked());
        product.setWidth(productPostVm.width());
        product.setHeight(productPostVm.height());
        product.setLength(productPostVm.length());
        product.setWeight(productPostVm.weight());

    }


    // update trường hợp có id danh mục cũ và id danh mục mới trun nhau nhiều để cải thện hiệu xuất sau
    private void perFormUpdateProductCategories(Product product , List<Long> categoryIdsNew,
                                         List<ProductCategory> productCategoriesOld,
                                         List<Long> categoryIdsOld){
        Set<Long> setCategoryIdsOld = new HashSet<>(categoryIdsOld);
        Set<Long> setCategoryIdsNew = new HashSet<>(categoryIdsNew);
        // ttìm cái cần thêm
        List<Long> categoryIdsToAdd = categoryIdsNew.stream().filter(categoryId -> !setCategoryIdsOld.contains(categoryId))
                .toList();
        // tìm cái cần xóa
        List<ProductCategory> productCategoriesToRemove  = productCategoriesOld.stream()
                .filter(productCategory -> !setCategoryIdsNew.contains(productCategory.getCategory().getId()))
                .toList();

        List<ProductCategory> productCategoriesNew = this.setProductCategories(product,categoryIdsToAdd);
        this.productCategoryRepository.deleteAllInBatch(productCategoriesToRemove);
        this.productCategoryRepository.saveAll(productCategoriesNew);



    }


    public ProductFeaturePagingVm getFeaturedProductsPaging(int pageIndex,int pageSize){
        // interface phn trang và sx khi truy vấn db
        Pageable pageable = PageRequest.of(pageIndex,pageSize);
        Page<Product> productPage = this.productRepository.findAllByFeatureIsTrueAndShownSeparatelyIsTrueAndPublicIsTrueOrderByIdAsc(pageable);
        List<Product> productsContent = productPage.getContent();
        List<ProductPreviewVm> productPreviewPayload = productsContent.stream()
                .map(product -> {
                    String avatarUrl =  this.mediaService.getImageById(product.getAvatarImageId()).url();
                    return new ProductPreviewVm(product.getId(),product.getName(),product.getSlug(),product.getPrice(),avatarUrl);
                }).toList();
        return new ProductFeaturePagingVm(
                productPreviewPayload,pageIndex,pageSize,
                (int) productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()
        );

    }




    // lấy thông tin chi tiết về sản phâ
    public ProductDetailVm getProductDetailBySlug(String slug){
        Product product = this.productRepository.findBySlugAndPublicIsTrue(slug)
                .orElseThrow(()-> new RuntimeException());
        String avatarUrl =  this.mediaService.getImageById(product.getAvatarImageId()).url();
        List<Long> imageIds = product.getProductImages().stream().map(ProductImage::getImageId)
                .toList();
        List<String> productImageUrls = this.mediaService.getImageByIds(imageIds)
                .stream().map(ImageVm::url)
                .toList();

        List<ProductAttributeValue> productAttributeValues = product.getProductAttributeValues();
        List<AttributeGroupValueVm> attributeGroupValueVms = new ArrayList<>();
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(productAttributeValues)) {
            List<ProductAttributeGroup> productAttributeGroups = productAttributeValues.stream()
                    .map(productAttributeValue ->
                            productAttributeValue.getProductAttribute().getProductAttributeGroup())
                    .filter(Objects::nonNull)
                    // loại bỏ ptu trùng lặp dựa trên equa
                    .distinct()
                    .toList();

            productAttributeGroups.forEach(productAttributeGroup -> {
                List<AttributeValueVm> attributeValueVms = new ArrayList<>();
                productAttributeValues.forEach(productAttributeValue -> {
                    if(productAttributeValue.getProductAttribute().getProductAttributeGroup().getId().equals(productAttributeGroup.getId())) {
                        String attributeName = productAttributeValue.getProductAttribute().getName();
                        String value = productAttributeValue.getValue();
                        AttributeValueVm attributeValueVm = new AttributeValueVm(attributeName,value);
                        attributeValueVms.add(attributeValueVm);
                    }
                });
                String attributeGroupName = productAttributeGroup.getName();
                AttributeGroupValueVm attributeGroupValueVm = new AttributeGroupValueVm(attributeGroupName,attributeValueVms);
                attributeGroupValueVms.add(attributeGroupValueVm);
            });

        }
        return new ProductDetailVm(
                product.getId(),
                product.getName(),
                product.getBrand().getName(),
                product.getProductCategories().stream().map(productCategory -> productCategory.getCategory().getName())
                        .toList(),
                attributeGroupValueVms,
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
    public void deleteProduct(Long id){
        Product product = this.productRepository.findById(id).orElseThrow(()-> new RuntimeException());
        product.setPublic(false);
        // check xem có phải là biến thể không
        if(!Objects.isNull(product.getParent())){
            // xóa các combination của biến thể này
            List<ProductOptionCombination> combinations = this.productOptionCombinationRepository
                    .findAllByProduct(product);
            if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(combinations)) {
                this.productOptionCombinationRepository.deleteAll(combinations);
            }
        }
        this.productRepository.save(product);


    }

    // lấy sp
    public ProductPagingVm getProductsPaging(int pageIndex,int pageSize){
        Pageable pageable = PageRequest.of(pageIndex,pageSize);

    }



    public ProductPagingVm getProductsFromCategoryPaging(int pageIndex,int pageSize,Long categoryId){
        Pageable pageable = PageRequest.of(pageIndex,pageSize);
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(()-> new RuntimeException());
        Page<ProductCategory> productCategoryPage = this.productCategoryRepository.findAllByCategory(category,pageable);
        List<Product> productContents = productCategoryPage.getContent().stream()
                .map(ProductCategory::getProduct)
                .toList();

        List<ProductPreviewVm> productPreviewVms = productContents.stream().map(product -> {
            String avatarUrl =  this.mediaService.getImageById(product.getAvatarImageId()).url();
            return new ProductPreviewVm(
                    product.getId(),product.getName(),product.getSlug(),product.getPrice(),avatarUrl
            );
        }).toList();
        return new ProductPagingVm(
                productPreviewVms,
                pageIndex,
                pageSize,
                (int) productCategoryPage.getTotalElements(),
                productCategoryPage.getTotalPages(),
                productCategoryPage.isLast()
        );





    }




    // fix sau: hiệu năng không tốt vì phải lấy ht toa bộ sp db
    public List<ProductPreviewVm> getProductFeaturedMakeSlide(){
        List<Product> productsFeatured = this.productRepository.findAllByFeatureIsTrue();
        // sxep theo random
        Collections.shuffle(productsFeatured);
        return productsFeatured.stream().limit(10)
                .map(product -> {
                    return new ProductPreviewVm(
                            product.getId() , product.getName(),product.getSlug() , product.getPrice(),
                            mediaService.getImageById(product.getAvatarImageId()).url()
                    );
                })
                .toList();
    }


    public List<ProductPreviewVm>  getProductsByIds(List<Long> ids){
        List<Product> products = this.productRepository.findAllByIdIn(ids);
        return products.stream().map(product -> {
            String avatarUrl =  this.mediaService.getImageById(product.getAvatarImageId()).url();
            // nếu avataurl = null mà không có parent nữa thì trả về null luôn
            if(StringUtils.isEmpty(avatarUrl) && Objects.nonNull(product.getParent()) ){
                    Optional<Product> parentProduct = this.productRepository.findById(product.getParent().getId());
                    avatarUrl = parentProduct.map(item->
                            this.mediaService.getImageById(item.getAvatarImageId()).url())
                            .orElse("");

            }
            return new ProductPreviewVm(
                    product.getId(),
                    product.getName(),
                    product.getSlug(),
                    product.getPrice(),
                    avatarUrl

            );

        }).toList();
    }

    public ProductPagingVm getProductByMultiParams(int pageIndex,int pageSize,String productName,String categorySlug,Double startPrice , Double endPrice){
        Pageable pageable = PageRequest.of(pageIndex,pageSize);
        Page<Product> productPage = this.productRepository.findByProductNameAndCategorySlugAndPriceBetween(
                productName.trim().toLowerCase(),
                categorySlug.trim(), startPrice, endPrice, pageable
        );


    }


}
