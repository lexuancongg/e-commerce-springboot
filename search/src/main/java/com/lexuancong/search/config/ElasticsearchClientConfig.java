package com.lexuancong.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
// class cấu hình làm cau noi giao tiep với Elasticsearch theo kieu repository  => cấu hình client connetc
@Configuration
// tựộng cấu hình  ElasticsearchOperations  và kích hoạt khả năng truy vấn dữ liệu từ Elasticsearch thông qua Spring Data.
@EnableElasticsearchRepositories
@RequiredArgsConstructor
public class ElasticsearchClientConfig  extends ElasticsearchConfiguration {
    private final ElasticsearchConfig elasticsearchConfig;


    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(this.elasticsearchConfig.getHostAndPort())
                .withBasicAuth(elasticsearchConfig.getUsername(), elasticsearchConfig.getPassword())
                .build();
    }




}


    // tím hiểu hai khaái niệm này
//    RestHighLevelClient , ElasticsearchOperations , ElasticsearchClient
