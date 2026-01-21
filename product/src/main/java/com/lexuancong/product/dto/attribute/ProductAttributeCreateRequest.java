package com.lexuancong.product.dto.attribute;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
// cấu hình jackson serialize tu object sang json cho post api
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
public record ProductAttributeCreateRequest(@NotBlank String name, Long productAttributeGroupId) {

}
