package com.lexuancong.product.service;

import com.lexuancong.product.model.*;
import com.lexuancong.product.repository.*;
import com.lexuancong.product.viewmodel.product.databinding.BaseProductPropertiesRequire;
import com.lexuancong.product.viewmodel.product.databinding.ProductPropertiesRequire;
import com.lexuancong.product.viewmodel.product.databinding.ProductVariationPropertiesRequire;
import com.lexuancong.product.viewmodel.product.post.ProductPostVm;
import com.lexuancong.product.viewmodel.product.post.ProductSummaryVm;
import com.lexuancong.product.viewmodel.product.post.ProductVariationPostVm;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
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

    public ProductSummaryVm createProduct(ProductPostVm productPostVm){
        this.validateProduct(productPostVm);
        Product product = productPostVm.toModel();
        this.setBrandForProduct(product, productPostVm.brandId());
        Product productSaved =  this.productRepository.save(product);

        List<ProductCategory> productCategoryList = this.setProductCategories(productSaved,productPostVm.categoryIds());
        List<ProductImage> productImageList = this.setProductImages(productSaved,productPostVm.categoryIds());

        this.productCategoryRepository.saveAll(productCategoryList);
        this.productImageRepository.saveAll(productImageList);


        if(CollectionUtils.isEmpty(productPostVm.variations())){
            return ProductSummaryVm.fromModel(product);
        }

        // xu ly cac bien the
        List<Product> variationSaved = this.createVariationsFromVm(productPostVm.variations(),productSaved);
        // xử lý l

    }



    private List<Product> createVariationsFromVm(List<? extends ProductVariationPropertiesRequire> variationPostVmList,
                                                 Product mainProduct) {

        // đầu tiên sử lý hình ảnh
        List<ProductImage> allProductImages = new ArrayList<>();
        List<Product> variations = variationPostVmList.stream()
                .map(variation ->{
                    Product productVariation  = this.buildProductVariationFormVm(variation, mainProduct);
                    // quay lại lưu hình ảnh như sản phẩm chính
                    List<ProductImage> variationImages =
                            this.setProductImages(productVariation,variation.productImageIds());
                    allProductImages.addAll(variationImages);

                    return productVariation;

                }  )
                .toList();
        List<Product> variationsSaved = this.productRepository.saveAll(variations);
        this.productImageRepository.saveAll(allProductImages);
        return variationsSaved;


    }

    // input : ds variationVm post , product cha -> để lưu tinh cha con
    private  List<Product> performCreateVariations(
            List<? extends ProductVariationPropertiesRequire> variationPropertiesVm,Product mainProduct){


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


    // gộp cho cả trường hợp create và update
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




}
