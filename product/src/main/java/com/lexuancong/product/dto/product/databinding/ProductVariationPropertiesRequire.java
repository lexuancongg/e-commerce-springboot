package com.lexuancong.product.dto.product.databinding;

import java.util.Map;

// chứa những thông tin thuojc tính mà các biến thể có
public interface ProductVariationPropertiesRequire extends BaseProductPropertiesRequire {
    Map<Long,String> valueOfOptionByOptionId();

}
