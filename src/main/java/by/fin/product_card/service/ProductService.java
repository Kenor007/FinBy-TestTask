package by.fin.product_card.service;

import by.fin.product_card.dto.ProductRequestDto;
import by.fin.product_card.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto productRequestDto);

    ProductResponseDto findProductById(Long productId);

    List<ProductResponseDto> findAllProducts();

    ProductResponseDto updateProductById(Long productId, ProductRequestDto productRequestDto);

    void deleteProductById(Long productId);

    ProductResponseDto findProductByParam(String param);
}