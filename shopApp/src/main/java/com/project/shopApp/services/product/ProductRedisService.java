package com.project.shopApp.services.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopApp.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductRedisService implements IProductRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    //Add this line
    @Value("${spring.data.redis.use-redis-cache}")
    private boolean useRedisCache;

    private String getKeyFrom(String keyword,
                              Long categoryId,
                              PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = sort.getOrderFor("id")
                .getDirection() == Sort.Direction.ASC ? "asc": "desc";
        return String.format("all_products:%d:%d:%s", pageNumber, pageSize, sortDirection);
        /*
        {
            "all_products:1:10:asc": "list of products object"
        }
        * */
    }
    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();

    }

    @Override
    public List<ProductResponse> getAllProducts(String keyword,
                                                Long categoryId,
                                                PageRequest pageRequest) throws JsonProcessingException {
//        if(!useRedisCache) {
//            return null;
//        }
        String key = this.getKeyFrom(keyword, categoryId, pageRequest);
        String json = (String) redisTemplate.opsForValue().get(key);
        List<ProductResponse> productResponses =
                json != null ?
                        redisObjectMapper.readValue(json, new TypeReference<List<ProductResponse>>() {})
                        : null;
        return productResponses;


    }

    @Override
    public void saveAllProducts(List<ProductResponse> productResponses,
                                String keyword,
                                Long categoryId,
                                PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword, categoryId, pageRequest);
        String json = redisObjectMapper.writeValueAsString(productResponses);
        redisTemplate.opsForValue().set(key, json);

    }
}
