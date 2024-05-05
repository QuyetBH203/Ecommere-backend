package com.project.shopApp.services.product.image;

import com.project.shopApp.exceptions.DataNotFoundException;
import com.project.shopApp.models.ProductImage;
import com.project.shopApp.repositories.ProductImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductImageService implements IProductImageService {
    private final ProductImageRepository productImageRepository;
    @Override
    @Transactional
    public ProductImage deleteProductImage(Long id) throws Exception {
        Optional<ProductImage> productImage = productImageRepository.findById(id);
        if(productImage.isEmpty()) {
            throw new DataNotFoundException(
                    String.format("Cannot find product image with id: %d", id)
            );
        }
        productImageRepository.deleteById(id);
        return productImage.get();
    }
}
