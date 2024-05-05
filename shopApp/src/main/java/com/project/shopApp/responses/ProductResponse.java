package com.project.shopApp.responses;


import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.shopApp.models.BaseEntity;
import com.project.shopApp.models.Product;
import com.project.shopApp.models.ProductImage;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProductResponse extends BaseEntity {
    private Long id;

    private String name;



    private Float price;
    private String thumbnail;
    private String description;
    // Thêm trường totalPages
    private int totalPages;

    @JsonProperty("category_id")
    private Long categoryId;
    @JsonProperty("product_images")
    private List<ProductImage> productImageList=new ArrayList<>();

    public static ProductResponse fromProduct(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .productImageList(product.getProductImages())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
