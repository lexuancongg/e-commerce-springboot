package com.lexuancong.product.viewmodel.attribute;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
// cấu hình jackson serialize tu object sang json cho post api
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
public record ProductAttributePostVm(@NotBlank String name, Long productAttributeGroupId) {

}
