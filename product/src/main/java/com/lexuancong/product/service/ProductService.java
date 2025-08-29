package com.lexuancong.product.service;

import com.lexuancong.product.constant.Constants;
import com.lexuancong.product.model.*;
import com.lexuancong.product.model.attribute.ProductAttributeGroup;
import com.lexuancong.product.repository.*;
import com.lexuancong.product.service.internal.ImageService;
import com.lexuancong.product.viewmodel.image.ImageVm;
import com.lexuancong.product.viewmodel.product.*;
import com.lexuancong.product.viewmodel.product.databinding.BaseProductPropertiesRequire;
import com.lexuancong.product.viewmodel.product.productoptions.ProductOptionValuePostVm;
import com.lexuancong.product.viewmodel.product.variants.ProductVariantVm;
import com.lexuancong.product.viewmodel.product.variants.ProductVariationPostVm;
import com.lexuancong.product.viewmodel.productattribute.AttributeGroupValueVm;
import com.lexuancong.product.viewmodel.productattribute.AttributeValueVm;
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
    private final ImageService imageService;
    public ProductSummaryVm createProduct(ProductPostVm productPostVm){

        this.validateProduct(productPostVm,null);
        Product product = productPostVm.toModel();

        this.setBrandForProduct(product, productPostVm.brandId());

        Product parentProductSaved =  this.productRepository.save(product);

        this.syncProductCategories(parentProductSaved,productPostVm.categoryIds());
        this.syncProductImages(parentProductSaved,productPostVm.imageIds());



        if(CollectionUtils.isEmpty(productPostVm.variations()) || CollectionUtils.isEmpty(productPostVm.productOptionValues())){
            return ProductSummaryVm.fromModel(product);
        }

        // xử lý các biến thể
        List<Product> variationSaved = this.createVariationsFromVm(productPostVm.variations(),parentProductSaved);


        List<ProductOption> productOptions = this.getProductOption(productPostVm.productOptionValues());

        Map<Long,ProductOption> mapOptionById =  productOptions.stream()
                .collect(Collectors.toMap(ProductOption::getId, Function.identity()));
        List<ProductOptionValue> productOptionValues =
                this.createProductOptionValue(productPostVm,parentProductSaved,mapOptionById);
        this.createProductOptionCombination(variationSaved,productOptionValues,productPostVm.variations(),mapOptionById);
        return ProductSummaryVm.fromModel(parentProductSaved);
    }

    // cần có product và ProductOption
    private void createProductOptionCombination(List<Product> variationsSaved,
                                                List<ProductOptionValue> productOptionValues,
                                                List<ProductVariationPostVm> variationVms,
                                                Map<Long,ProductOption> mapOptionById){
        List<SpecificProductVariant> specificProductVariants = new ArrayList<>();
        // variationSave rồi nhưng chưa bt nó ứng với productOption Id nào và value gì => dua vao variationVm
        // variantSaved và variantVm có cùng slug => dựa vào slug để lấy
        Map<String,Product> mapProductBySlug = variationsSaved.stream()
                .collect(Collectors.toMap(Product::getSlug,Function.identity()));
        for(ProductVariationPostVm variationVm : variationVms){
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
                boolean isExitOptionValue = productOptionValues.stream()
                        .anyMatch(productOptionValue -> productOptionValue.getProductOption()
                                .getId().equals(optionId) && productOptionValue.getValue().equals(optionValue));
                if(!isExitOptionValue){
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
        this.productOptionCombinationRepository.saveAll(specificProductVariants);
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

    private List<ProductOption> getProductOption(List<ProductOptionValuePostVm> productOptionValueVms){
        List<Long> optionIds  = productOptionValueVms.stream()
                .map(ProductOptionValuePostVm::productOptionId)
                .toList();
        if(CollectionUtils.isEmpty(optionIds)) return Collections.emptyList();
        List<ProductOption> productOptions = this.productOptionRepository.findAllById(optionIds);
        return productOptions;

    }




    private List<Product> createVariationsFromVm(List<ProductVariationPostVm> variationPostVmList,
                                                 Product mainProduct) {

        // đầu tiên sử lý hình ảnh
        List<ProductImage> allProductImages = new ArrayList<>();
        List<Product> variations = variationPostVmList.stream()
                .map(variationVm ->{
                    Product productVariation  = this.buildProductVariationFormVm(variationVm, mainProduct ,new Product());
                    // quay lại lưu hình ảnh như sản phẩm chính
                    List<ProductImage> variationImages =
                            this.updateProductImagesForSave(productVariation,variationVm.imageIds());

                    allProductImages.addAll(variationImages);

                    return productVariation;

                }  )
                .toList();

//        allProductImages cùng tham chiếu tới các product trong list product variations
        List<Product> variationsSaved = this.productRepository.saveAll(variations);

        // lưu cho bảng productImage
        this.productImageRepository.saveAll(allProductImages);
        return variationsSaved;


    }


    private void  syncProductImages(Product product, List<Long> imageIds){
        List<ProductImage> productImageNew = this.updateProductImagesForSave(product,imageIds);
        this.productImageRepository.saveAll(productImageNew);
    }

    // cũng dần cho cả create cả update
    private List<ProductImage>  updateProductImagesForSave(Product product , List<Long> imageIds){
        List<ProductImage> productImages = new ArrayList<>();

        if(CollectionUtils.isEmpty(imageIds)){
            // xóa các cái cũ đi
            this.productImageRepository.deleteByProductId(product.getId());

        }
        // trường hợp product chưa có category => chỉ cần thêm vào để lưu vào
        else if(product.getProductImages().isEmpty()){
            productImages.addAll(
                    imageIds.stream().map(imageId ->
                            ProductImage.builder().imageId(imageId).product(product).build()
                    ).toList()
            );

            // nếu đã có trc , => xóa cái cũ, thêm cái mới
        }else {
            List<Long> imageIdsOfProductInDb = product.getProductImages().stream()
                    .map(ProductImage::getImageId)
                    .sorted()
                    .toList();
            if(!org.apache.commons.collections4.CollectionUtils.isEqualCollection(imageIds, imageIdsOfProductInDb)){
                List<Long> newImageIds = imageIds.stream()
                        .filter(imageId->  !imageIdsOfProductInDb.contains(imageId))
                        .toList();
                List<Long> imageIdsToDelete = imageIdsOfProductInDb.stream()
                        .filter( imageId->  !imageIds.contains(imageId))
                        .toList();
                if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(imageIdsToDelete)){
                    this.productImageRepository.deleteByImageIdInAndProductId(imageIdsToDelete, product.getId());

                }
                if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(newImageIds)){
                    productImages.addAll(
                            newImageIds.stream()
                                    .map(newImageId ->ProductImage.builder().imageId(newImageId).product(product).build())
                                    .toList()
                    );
                }
            }

        }
        return productImages;

    }


    private void syncProductCategories(Product product ,List<Long> categoryIdsVm ){
        List<ProductCategory> productCategoryList = this.buildProductCategories(product,categoryIdsVm);
        List<ProductCategory> productCategories = new ArrayList<>();

        if(productCategoryList.isEmpty()){
            if(categoryIdsVm.isEmpty()){
               this.productCategoryRepository.deleteByProductId(product.getId());
            }
        }else{
            List<Long> categoryIdsOfProductInDb = product.getProductCategories().stream()
                    .map(productCategory -> productCategory.getCategory().getId())
                    .toList();
            List<Long> categoryIdsToDelete =  categoryIdsOfProductInDb.stream()
                    .filter(categoryId-> !categoryIdsVm.contains(categoryId))
                    .toList();
            List<Long> newCategoryIds = categoryIdsVm.stream()
                    .filter(categoryId-> !categoryIdsOfProductInDb.contains(categoryId))
                    .toList();

            if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(categoryIdsToDelete)){
                this.productCategoryRepository.deleteByCategoryIdInAndProductId(categoryIdsToDelete ,product.getId());
            }
            if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(newCategoryIds)){
                productCategories.addAll(
                        newCategoryIds.stream().map(categoryId -> ProductCategory
                                .builder()
                                .category(productCategoryList.stream()
                                        .filter(productCategory -> productCategory.getCategory().getId().equals(categoryId))
                                        .findFirst()
                                        .get()
                                        .getCategory()
                                )
                                .product(product)
                                .build()
                        ).toList()
                );
            }
        }
        this.productCategoryRepository.saveAll(productCategories);


    }


    // cho  cả update và create dùng chung
    private List<ProductCategory> buildProductCategories(Product product, List<Long> categoryIdsVm){
        if(CollectionUtils.isEmpty(categoryIdsVm)){
            return Collections.emptyList();
        }
        List<ProductCategory> productCategoryList = new ArrayList<>();

        // product có thể là cái mới save , có thể là cái lấy trong db ra nếu update
        List<Long> categoryIds = product.getProductCategories().stream()
                .map(productCategory -> productCategory.getCategory().getId())
                .sorted()
                .toList();
        // nếu có thay đổi thì mới thực hiện
        if(!org.apache.commons.collections4.CollectionUtils.isEqualCollection(categoryIds,categoryIdsVm)){
            List<Category> categories = categoryRepository.findAllById(categoryIdsVm);
            if(categories.isEmpty()){
                throw new BadRequestException(Constants.ErrorKey.CATEGORY_NOT_FOUND,categoryIdsVm);
            }else if (categoryIdsVm.size() > categories.size()){
                List<Long> categoryIdsValid = categories.stream().map(Category::getId).toList();
                categoryIdsVm.removeAll(categoryIdsValid);
                throw new BadRequestException(Constants.ErrorKey.CATEGORY_NOT_FOUND, categoryIdsVm);
            }else {
                //  hợp lệ
                for (Category category : categories) {
                    productCategoryList.add(ProductCategory.builder()
                            .product(product)
                            .category(category).build());
                }
            }
        }

        return productCategoryList;


    }






    private void setBrandForProduct(Product product , Long brandId){
        if(brandId != null){
            Brand brand = this.brandRepository.findById(brandId)
                    .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.BRAND_NOT_FOUND));
            product.setBrand(brand);
        }
    }






    //  cả update và create và update đều validate => gộp chung hàm
    private  void validateProduct(ProductPostVm productVmToSave, Product existingProduct){
        this.validateLengthMustGreaterThanWidth(productVmToSave);
        this.validateUniqueProductProperties(productVmToSave,existingProduct);
        this.validateDuplicateProductPropertiesBetweenVariations(productVmToSave);


        // valid datete thuộc tính của varian cos bị trùng lặp trong db không
        List<Long> variationIds = productVmToSave.variations().stream()
                .map(ProductVariationPostVm::id)
                .filter(Objects::nonNull)
                .toList();

        // variant tuong ung trong db vs tung id
        Map<Long,Product> mapVariationsSaved = this.productRepository.findAllById(variationIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity(),(product1, product2) -> new Product() ));

        for(ProductVariationPostVm variation : productVmToSave.variations()){
            Product variantExitedInDb = mapVariationsSaved.get(variation.id());
            this.validateUniqueProductProperties(variation,variantExitedInDb);
        }

    }


    // check xem chieeuf dài và chiều rộng đươc nhập hhowpjp lệ không => tính toán tiền vận chuyển sau nay
    private void validateLengthMustGreaterThanWidth(ProductPostVm productVmToSave){
        if(productVmToSave.length() < productVmToSave.width()) {
            throw  new BadRequestException(Constants.ErrorKey.LENGTH_MUST_EXCEED_WIDTH);
        }
    }

    // check xem các thuộc tinhs của sp có bị trùng lặp trong db không
    private void validateUniqueProductProperties(BaseProductPropertiesRequire baseProductProperties, Product existingProduct){
        this.ensurePropertyNotExists(baseProductProperties.slug().toLowerCase(),this.productRepository::findBySlug,
                existingProduct,Constants.ErrorKey.SLUG_ALREADY_EXISTED);
        if (StringUtils.isNotEmpty(baseProductProperties.gtin())){
            this.ensurePropertyNotExists(baseProductProperties.gtin(),this.productRepository::findByGtin,
                    existingProduct,Constants.ErrorKey.GTIN_ALREADY_EXISTED);
        }
        this.ensurePropertyNotExists(baseProductProperties.sku(),this.productRepository::findBySku,
                existingProduct,Constants.ErrorKey.SKU_ALREADY_EXISTED);

    }




    private void ensurePropertyNotExists(String propertyValue, Function<String, Optional<Product>> finder , Product existingProduct, String errorKey){
        finder.apply(propertyValue).ifPresent(product -> {
            if(existingProduct == null || !product.getId().equals(existingProduct.getId()) ){
                 throw new DuplicatedException(errorKey);
            }
        });
    }



    // chưa check trùng lặp các thuộc tính con trong db => làm sau
    private  void validateDuplicateProductPropertiesBetweenVariations(ProductPostVm productVmToSave){
        // check trùng lặp slug , sku, gtins cho các biến thể con
        Set<String> setSkus = new HashSet<>(Collections.singletonList(productVmToSave.sku()));
        Set<String> setGtins = new HashSet<>(Collections.singletonList(productVmToSave.gtin()));
        Set<String> setSlugs = new HashSet<>(Collections.singletonList(productVmToSave.slug()));
        for (ProductVariationPostVm variation : productVmToSave.variations()) {
            if(!setSkus.add(variation.sku().toLowerCase())){
                throw new DuplicatedException(Constants.ErrorKey.SKU_ALREADY_EXISTED);
            }
            if(!setSlugs.add(variation.slug().toLowerCase())){
                // throw exception
                throw new DuplicatedException(Constants.ErrorKey.SLUG_ALREADY_EXISTED);
            }
            if(StringUtils.isNotEmpty(variation.gtin()) && !setGtins.add(variation.gtin())){
                // throw exception
                throw new DuplicatedException(Constants.ErrorKey.GTIN_ALREADY_EXISTED);

            }
        }
    }



    public void updateProduct(Long id, ProductPostVm productPostVm){
        Product product = this.productRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Constants.ErrorKey.PRODUCT_NOT_FOUND,id));

        this.validateProduct(productPostVm,product);
        this.setBrandForProduct(product,productPostVm.brandId());
        this.updatePropertiesProductFromVm(product,productPostVm);

        // chỉnh sửa lại productCategory
//        this.updateProductCategories(product,productPostVm);
        this.syncProductCategories(product,productPostVm.categoryIds());


//        this.updateProductImage(product,productPostVm );
        this.syncProductImages(product,productPostVm.imageIds());

        List<Product> variationChillInDb = product.getChild();
        this.updateVariationInDb(productPostVm, variationChillInDb , product);
        // variationChillInDb được cập nhật do tham chiếu các phần tử
        List<Product> variantUpdated = this.productRepository.saveAll(variationChillInDb);


        // update laại product option
        List<ProductOption> productOptionsNew = this.getProductOption(productPostVm.productOptionValues());

        Map<Long,ProductOption> productOptionMapById = productOptionsNew.stream()
                .collect(Collectors.toMap(ProductOption::getId, Function.identity()));

        // cái thêm mới
        List<ProductVariationPostVm>  variationVmsNew = productPostVm.variations().stream()
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
    private void updateProductOptionCombination(List<ProductVariationPostVm> variationVms,
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

    private void updateProductImage(Product product , ProductPostVm productPostVm){
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
                .sorted()
                .toList();
        List<Long> categoryIdsNew = productPostVm.categoryIds().stream().sorted().toList();
        if(!org.apache.commons.collections4.CollectionUtils.isEqualCollection(categoryIdsOld,categoryIdsNew)){
            this.perFormUpdateProductCategories(product,categoryIdsNew,productCategoriesOld,categoryIdsOld);
        }


    }


    private Product buildProductVariationFormVm(ProductVariationPostVm productVariationPostVm,
                                                Product mainProduct , Product variationInit){
        variationInit.setName(productVariationPostVm.name());
        variationInit.setAvatarImageId(productVariationPostVm.avatarImageId());
        variationInit.setSlug(productVariationPostVm.slug());
        variationInit.setSku(productVariationPostVm.sku());
        variationInit.setGtin(productVariationPostVm.gtin());
        variationInit.setPrice(productVariationPostVm.price());
        variationInit.setPublic(mainProduct.isPublic());
        // khi create
        if(variationInit.getId() == null){
            variationInit.setParent(mainProduct);
        }
        return variationInit;



    }


    private void perFormUpdateValueForVariation(Product mainProduct, ProductVariationPostVm variationPutVm , Product variationInit ){
      this.buildProductVariationFormVm(variationPutVm,mainProduct,variationInit);

    }

    private  void updateVariationInDb(ProductPostVm productPostVm, List<Product> variationInDbs , Product mainProduct){
        Map<Long,Product> mapVariationSaved = variationInDbs.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        productPostVm.variations().forEach(variationVm->{
//            for(Product variationChill : variationChillInDbs){
//                if(variationChill.getId().equals(variationVm.id())){
//                    this.perFormUpdateValueForVariation(variationChill,variationVm);
//                    break;
//                }
//            }
            Product variation = mapVariationSaved.get(variationVm.id());
            if(variation!=null){
                this.perFormUpdateValueForVariation(mainProduct,variationVm,variation);
                this.syncProductImages(variation,variationVm.imageIds());
            }


        });

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
        List<ProductImage> productImages = this.syncProductImages(product,imageIdsToAdd);
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

        // tối ưu ở đây

        List<ProductCategory> productCategoriesNew = this.buildProductCategories(product,categoryIdsToAdd);
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
                    String avatarUrl =  this.imageService.getImageById(product.getAvatarImageId()).url();
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
        String avatarUrl =  this.imageService.getImageById(product.getAvatarImageId()).url();
        List<Long> imageIds = product.getProductImages().stream().map(ProductImage::getImageId)
                .toList();
        List<String> productImageUrls = this.imageService.getImageByIds(imageIds)
                .stream().map(ImageVm::url)
                .toList();

        List<ProductAttributeValue> productAttributeValues = product.getProductAttributeValues();
        List<AttributeGroupValueVm> attributeGroupValueVms = new ArrayList<>();
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(productAttributeValues)) {
            List<ProductAttributeGroup> productAttributeGroups = productAttributeValues.stream()
                    .map(productAttributeValue ->
                            productAttributeValue.getProductAttribute().getProductAttributeGroup())
                    .filter(Objects::nonNull)
                    // loại bỏ ptu trùng lặp dựa trên equal
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
            List<SpecificProductVariant> combinations = this.productOptionCombinationRepository
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
            String avatarUrl =  this.imageService.getImageById(product.getAvatarImageId()).url();
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
                            imageService.getImageById(product.getAvatarImageId()).url()
                    );
                })
                .toList();
    }


    public List<ProductPreviewVm>  getProductsByIds(List<Long> ids){
        List<Product> products = this.productRepository.findAllByIdIn(ids);
        return products.stream().map(product -> {
            String avatarUrl =  this.imageService.getImageById(product.getAvatarImageId()).url();
            // nếu avataurl = null mà không có parent nữa thì trả về null luôn
            if(StringUtils.isEmpty(avatarUrl) && Objects.nonNull(product.getParent()) ){
                    Optional<Product> parentProduct = this.productRepository.findById(product.getParent().getId());
                    avatarUrl = parentProduct.map(item->
                            this.imageService.getImageById(item.getAvatarImageId()).url())
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
        List<Product> products = productPage.getContent();
        List<ProductPreviewVm> contentPayload = products.stream()
                .map(product -> {
                    String avatarUrl =  this.imageService.getImageById(product.getAvatarImageId()).url();
                    return new ProductPreviewVm(
                            product.getId(),
                            product.getName(),
                            product.getSlug(),
                            product.getPrice(),
                            avatarUrl
                    );

                }).toList();

        return new ProductPagingVm(
                contentPayload,
                pageIndex,
                pageSize,
                (int) productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast()

        );

    }

    public List<ProductVariantVm> getProductVariationsByParentId(Long parentId){
        Product parentProduct = this.productRepository.findById(parentId).orElseThrow(()-> new RuntimeException());
//       // autoboxing : convert lên nếu là bolean
        if(Boolean.TRUE.equals(parentProduct.isHasOptions())){
            List<Product> productVariations  = parentProduct.getChild().stream()
                    .filter(Product::isPublic)
                    .toList();
            return productVariations.stream()
                    .map(variant -> {
                        List<SpecificProductVariant> specificProductVariants = this.productOptionCombinationRepository
                                .findAllByProduct(variant);
                        Map<Long,String> optionsValues = specificProductVariants.stream()
                                .collect(
                                        Collectors.toMap(
                                                specificProductVariant -> specificProductVariant.getProductOption().getId() ,
                                                SpecificProductVariant:: getValue
                                                )
                                );
                        String avatarUrl = null;
                        if(variant.getAvatarImageId()!= null){
                            avatarUrl = this.imageService.getImageById(variant.getAvatarImageId()).url();
                        }
                        List<Long> imageIds = variant.getProductImages().stream().map(ProductImage::getId).toList();
                        List<ImageVm> productImages =  this.imageService.getImageByIds(imageIds);
                        return new ProductVariantVm(
                                variant.getId(),
                                variant.getName(),
                                variant.getSlug(),
                                variant.getSku(),
                                variant.getGtin(),
                                variant.getPrice(),
                                avatarUrl,
                                productImages,
                                optionsValues
                        );


                    }).toList();

        }
        return Collections.emptyList();


    }


}
