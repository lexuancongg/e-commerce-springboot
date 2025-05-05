package com.lexuancong.search.model;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "product")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
}
