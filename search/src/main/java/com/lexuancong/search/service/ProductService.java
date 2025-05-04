package com.lexuancong.search.service;

import com.lexuancong.search.viewmodel.ProductPagingVm;
import com.lexuancong.search.viewmodel.ProductQueryParams;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    // tuong tac voi elasticsearch
    private final ElasticsearchOperations elasticsearchOperations;
    public ProductService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    private ProductPagingVm findProductsByCriteria(ProductQueryParams productQueryParams){
        NativeQueryBuilder  nativeQuery = NativeQuery.builder()
                .withAggregation()

    }
}

//// doc
//Query là đối tượng đại diện cho truy vấn thực tế trong Elasticsearch. Mỗi truy vấn trong Elasticsearch có thể là một loại truy vấn khác nhau như match, term, range, bool, v.v.
// Query bao gồm các thông tin về loại truy vấn và các điều kiện tìm kiếm.

// ví dụ
//Match Query: Tìm kiếm các tài liệu có chứa từ khóa trong một trường nhất định.
//Term Query: Tìm kiếm các tài liệu có giá trị chính xác cho một trường cụ thể.
//Range Query: Tìm kiếm các tài liệu có giá trị trong một khoảng.



//2. QueryBuilder
//QueryBuilder là lớp cơ sở để xây dựng các truy vấn trong Elasticsearch. Các lớp con của QueryBuilder như BoolQueryBuilder, TermQueryBuilder, MatchQueryBuilder, v.v.
// cho phép bạn dễ dàng xây dựng truy vấn Elasticsearch.
// Ví dụ,
//BoolQueryBuilder giúp bạn xây dựng một truy vấn bool với các điều kiện must, should, must_not, và filter.

//
//NativeQueryBuilder: Dùng trong Spring Data Elasticsearch để xây dựng các truy vấn tùy chỉnh.
//
//SearchRequest và SearchSourceBuilder: Dùng trong Elasticsearch Java Client để xây dựng và gửi các yêu cầu tìm kiếm.