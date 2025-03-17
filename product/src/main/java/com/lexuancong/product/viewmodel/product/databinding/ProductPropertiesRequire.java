package com.lexuancong.product.viewmodel.product.databinding;

import java.util.List;

// không sử dụng interface trư tiếp mà dùng T extent giúp mở rộng cho sau này lỡ có một vài sp có thuộc tính đặc biệt ,
public interface ProductPropertiesRequire<T extends ProductVariationPropertiesRequire> extends BaseProductPropertiesRequire {
    // T ở đây là các biến thể con kèm theo như màu sắc...
    List<T> variations();
    boolean isPublic();
    // mặc định id post lên là null
    @Override
    default Long id(){ return null; }


    // ba thuộc tính cơ bản nayf sau này để tính tiền vaanjj chuyển
    Double length();
    Double width();
    Double height();
    Double weight();
}
