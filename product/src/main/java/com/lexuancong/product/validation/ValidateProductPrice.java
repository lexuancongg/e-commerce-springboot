package com.lexuancong.product.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

// chúng ta tạo custom animation để valid price cho sp theem vào

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        // class chứa logic để kiểm tra phần validate
        validatedBy = {ProductPriceValidator.class}
)
public @interface ValidateProductPrice {
    String message() default "Price must than 0";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
