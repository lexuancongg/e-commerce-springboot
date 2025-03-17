package com.lexuancong.product.viewmodel.product.databinding;

import java.util.Map;

// chứa những thông tin thuojc tính mà các biến thể có
public interface ProductVariationPropertiesRequire extends BaseProductPropertiesRequire {
    // chứa thông tin màu sacws theo kiểu map với id thuộc tính màu sắc làm key
    Map<Long,String> valueOfOptionByOptionId();

}
