package com.project.shopApp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("quantity")
    private Integer quantity;
}
