package com.lexuancong.search.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

// document ánh xạ duwx lệu được lưu trong elasticserach
@Document(indexName = "product")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private Long id;

    // type Text- : bị tokenize  ( phân tachs thành các đoạn nhỏ khi lưu chứ không lưu nguyên vẹn)
    // analyzer = "",  : chiến lược khi thêm dữ liệu vào (indexing)
    // searchAnalyzer =  : chiêến lược khi tìm kiếm dl
    // => quan trong trong search full text : hai giá trị này có thể custom bằng file config thông quqa @Setting , filter
    @Field(name = "name", type = FieldType.Text ,  analyzer = "standard", searchAnalyzer = "standard")
    private String name;
    private String slug;
    @Field(name = "price", type = FieldType.Double)
    private Double price;
    private boolean isFeature;
    private Long avatarImageId;
    private boolean isShownSeparately;
    @Field(type = FieldType.Text, fielddata = true)
    private String brand;
    @Field(type = FieldType.Keyword)
    private List<String> categories;






}
