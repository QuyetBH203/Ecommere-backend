package com.project.shopApp.services.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.shopApp.responses.ProductResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductRedisService {
    void clear();//clear cache
    List<ProductResponse> getAllProducts(
            String keyword,
            Long categoryId, PageRequest pageRequest) throws JsonProcessingException;
    void saveAllProducts(List<ProductResponse> productResponses,
                         String keyword,
                         Long categoryId,
                         PageRequest pageRequest) throws JsonProcessingException;
}
