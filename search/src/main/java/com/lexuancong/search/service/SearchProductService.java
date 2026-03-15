package com.lexuancong.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.lexuancong.search.constant.ProductField;
import com.lexuancong.search.model.Product;
import com.lexuancong.search.dto.ProductPreviewResponse;
import com.lexuancong.search.dto.ProductQueryParams;
import com.lexuancong.share.dto.paging.PagingResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchProductService {
    //  đối tượng tuong tac voi elasticsearch
    private final ElasticsearchOperations elasticsearchOperations;

    public SearchProductService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public PagingResponse<ProductPreviewResponse> findProductsByCriteria(ProductQueryParams productQueryParams) {
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder()
                // theem cacs điều kiện vào truy vaans
                .withQuery(queryBuilder -> queryBuilder
                        // truy vấn kết hợp nhiều dk
                        .bool(boolQueryBuilder -> boolQueryBuilder
                                // như or , khớp thì tốt -> tăng điểm cho docoment thỏa mản
                                .should(shouldQueryBuilder -> shouldQueryBuilder
                                        // tìm kiếm trên nhiều trường
                                        .multiMatch(multiMatchQueryBuilder -> multiMatchQueryBuilder
                                                .fields(ProductField.NAME, ProductField.BRAND, ProductField.CATEGORIES)
                                                .query(productQueryParams.keyword())
                                                // tìm kieems gần đúng
                                                .fuzziness(Fuzziness.ONE.toString())
                                        )
                                )
                        )
                )
                .withPageable(PageRequest.of(productQueryParams.pageIndex(), productQueryParams.pageSize()));




        // thêm bộ lọc vào truy vấn => không ảnh hưởng điểm , chỉ quyết định xem document đó có đc đưa vào kq không thôi => giống như where
        nativeQueryBuilder.withFilter(filterQueryBuilder -> filterQueryBuilder
                .bool(boolQueryBuilder -> {
                            this.applyTermsFilter(productQueryParams.brand(), ProductField.BRAND, boolQueryBuilder);
                            this.applyTermsFilter(productQueryParams.category(), ProductField.CATEGORIES, boolQueryBuilder);
                            this.applyRangeFilter(productQueryParams.minPrice(), productQueryParams.minPrice(), boolQueryBuilder);
                            boolQueryBuilder.must(mustQueryBuilder -> mustQueryBuilder
                                    .term(termQueryBuilder -> termQueryBuilder
                                            .field(ProductField.IS_PUBLISHED)
                                            .value(true)
                                    )
                            );
                            return boolQueryBuilder;
                        }
                )
        );

        org.springframework.data.elasticsearch.core.query.Query query = nativeQueryBuilder.build();
        // ds keets quả trả ve
        SearchHits<Product> searchHitsProduct = this.elasticsearchOperations.search(query, Product.class);
        // chuyển đổi ds kết quả thành  phân trang page
        SearchPage<Product> searchPageProduct = SearchHitSupport.searchPageFor(searchHitsProduct, nativeQueryBuilder.getPageable());
        List<ProductPreviewResponse> payload = searchHitsProduct.stream()
                .map(productSearchHit -> {
                    Product product = productSearchHit.getContent();
                    return ProductPreviewResponse.fromProduct(product);
                })
                .toList();
        return PagingResponse.<ProductPreviewResponse> builder()
                .last(searchPageProduct.isLast())
                .payload(payload)
                .totalPages(searchPageProduct.getTotalPages())
                .totalElements(searchPageProduct.getTotalElements())
                .build();
    }

    private void applyTermsFilter(String fieldValues, String fieldName, BoolQuery.Builder boolQueryBuilder) {
        if (fieldValues.isBlank()) return;
        String[] values = fieldValues.split(",");
        // phải ít nhất đúng một trong các value này theo fieldname
        boolQueryBuilder.must(mustQueryBuilder -> {

                    BoolQuery.Builder boolQuery = new BoolQuery.Builder();
                    for (String value : values) {
                        boolQuery.should(shouldQuery ->
                                shouldQuery
                                        .term(termQueryBuilder -> termQueryBuilder
                                                .field(fieldName)
                                                .value(value)
                                                .caseInsensitive(true)
                                        )
                        );

                    }
                    return new Query.Builder().bool(boolQuery.build());
                }

        );


    }

    private void applyRangeFilter(Double min, Double max, BoolQuery.Builder boolQueryBuilder) {
        if (min != null || max != null) {
            boolQueryBuilder.must(mustQueryBuilder -> mustQueryBuilder
                    .range(rangeQueryBuilder -> rangeQueryBuilder
                            .field(ProductField.PRICE)
                            .from(min != null ? min.toString() : null)
                            .to(max != null ? max.toString() : null)
                    )

            );
        }
    }

}

/// / doc
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


//🧱 1. Các đối tượng cốt lõi
//Đối tượng	Ý nghĩa
//Query	Đại diện cho điều kiện tìm kiếm
//NativeQueryBuilder	Dùng để build truy vấn Query tùy chỉnh
//BoolQuery	Dùng để kết hợp nhiều điều kiện (must, should, must_not, filter)
//Aggregation	Dùng để phân nhóm, đếm, tính toán...
//SearchHits	Kết quả tìm kiếm trả về
//SearchHit<T>	Một item trong danh sách kết quả
//ElasticsearchOperations	Cung cấp method để thao tác với Elasticsearch
//
//🔍 2. Các method hay dùng
//Method	Ý nghĩa
//match(field, value)	Tìm kiếm tương đối (phân tích từ)
//term(field, value)	Tìm kiếm chính xác
//terms(field, List<value>)	Tìm theo nhiều giá trị
//range(...)	Tìm trong khoảng (>, <, >=, <=)
//bool(...)	Kết hợp nhiều điều kiện
//withQuery(...)	Truyền điều kiện vào truy vấn
//withAggregation(...)	Truyền điều kiện phân nhóm
//withSort(...)	Thêm sắp xếp
//withPageable(...)	Thêm phân trang
//
//📖 3. Các từ khóa điều kiện
//Từ khóa	Ý nghĩa
//must	Bắt buộc phải thỏa điều kiện
//should	Có thì tốt, không có cũng được
//must_not	Không được thỏa điều kiện
//filter	Giống must nhưng không tính điểm relevance (nhanh hơn)
//
//🧪 4. Ví dụ đơn giản
//java
//        Copy
//Edit
//Query query = QueryBuilders.bool()
//        .must(QueryBuilders.match("name", "iphone"))
//        .filter(QueryBuilders.term("categoryId", "123"));
//
//NativeQuery searchQuery = NativeQuery.builder()
//        .withQuery(query)
//        .build();
//
//SearchHits<Product> hits = elasticsearchOperations.search(searchQuery, Product.class);
//
//


//
//1. Truy vấn cơ bản:
//term
//
//        match
//
//multi_match
//
//        range
//
//wildcard
//
//        fuzzy
//
//prefix
//
//        query_string
//
//simple_query_string
//
//2. Truy vấn kết hợp (Boolean Queries):
//bool
//
//        must
//
//must_not
//
//        should
//
//filter
//
//3. Truy vấn nâng cao:
//boosting
//
//        constant_score
//
//dis_max
//
//        span_near
//
//span_term
//
//        span_first
//
//4. Truy vấn cho dữ liệu lồng nhau (Nested Queries):
//nested
//
//        has_child
//
//has_parent
//
//5. Truy vấn cho vị trí địa lý:
//geo_distance
//
//        geo_bounding_box
//
//geo_polygon
//
//        geo_shape
//
//6. Truy vấn cho phân tích dữ liệu (Aggregations & Other):
//aggregations
//
//        top_hits
//
//more_like_this
//
//7. Truy vấn kiểm tra sự tồn tại:
//exists
//
//8. Truy vấn theo ID:
//ids
//
//9. Truy vấn cho sự liên quan (Similarity & Scoring):
//boosting
