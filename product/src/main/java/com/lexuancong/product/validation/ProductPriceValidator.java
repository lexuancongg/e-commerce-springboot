package com.lexuancong.product.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ProductPriceValidator implements ConstraintValidator<ValidateProductPrice, Double> {
    @Override
    public void initialize(ValidateProductPrice constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Double productPrice, ConstraintValidatorContext constraintValidatorContext) {
        return productPrice >= 0;
    }
}
