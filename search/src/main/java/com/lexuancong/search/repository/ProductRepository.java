package com.lexuancong.search.repository;

import com.lexuancong.search.model.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductRepository extends ElasticsearchRepository<Product, Long> {
}
