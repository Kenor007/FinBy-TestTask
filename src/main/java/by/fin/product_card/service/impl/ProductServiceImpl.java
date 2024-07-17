package by.fin.product_card.service.impl;

import by.fin.product_card.dto.ProductRequestDto;
import by.fin.product_card.dto.ProductResponseDto;
import by.fin.product_card.error_handling.exception.ProductExistsException;
import by.fin.product_card.error_handling.exception.ProductNotFoundException;
import by.fin.product_card.mapper.ProductMapper;
import by.fin.product_card.model.Product;
import by.fin.product_card.repository.ProductRepository;
import by.fin.product_card.service.PhotoService;
import by.fin.product_card.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static by.fin.product_card.error_handling.constant.ExceptionAnswer.PRODUCT_ALREADY_EXISTS;
import static by.fin.product_card.error_handling.constant.ExceptionAnswer.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final PhotoService photoService;

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = productMapper.productRequestDtoToProduct(productRequestDto);
        validateProduct(product);
        product.setPhoto(photoService.processPhoto(productRequestDto.getImageUrl(), productRequestDto.getPhotoName()));
        Product savedProduct = productRepository.save(product);
        log.debug("Product with id {} is saved", savedProduct.getId());
        return productMapper.productToProductResponseDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto findProductById(Long productId) {
        Product product = findProductByIdOrThrow(productId);
        log.debug("Product with id {} is found", product.getId());
        return productMapper.productToProductResponseDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> findAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            log.debug("List of products is empty");
        }
        log.debug("List of products has been found");
        return products.stream().map(productMapper::productToProductResponseDto).toList();
    }

    @Override
    @Transactional
    public ProductResponseDto updateProductById(Long productId, ProductRequestDto productRequestDto) {
        Product product = findProductByIdOrThrow(productId);
        log.debug("Updating product {}", product);
        product = productMapper.productRequestDtoToUpdatedProduct(productRequestDto, product);
        product.setPhoto(photoService.processPhoto(productRequestDto.getImageUrl(), productRequestDto.getPhotoName()));
        product = productRepository.save(product);
        log.debug("Product with id {} is updated", product.getId());
        return productMapper.productToProductResponseDto(product);
    }

    @Override
    @Transactional
    public void deleteProductById(Long productId) {
        if (productRepository.existsById(productId)) {
            Product deletedProduct = findProductByIdOrThrow(productId);
            productRepository.deleteById(productId);
            log.debug("Product with id {} is deleted", productId);
        } else {
            log.error("Product with id {} not found", productId);
            throw new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, productId));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto findProductByParam(String param) {
        switch (param) {
            case "highest rating" -> {
                Product product = productRepository.findAll().stream()
                        .max(Comparator.comparing(Product::getRating))
                        .orElseThrow(() -> new ProductNotFoundException("No product with the highest rating found"));
                return productMapper.productToProductResponseDto(product);
            }
            case "expensive" -> {
                Product product = productRepository.findAll().stream()
                        .max(Comparator.comparing(Product::getPrice))
                        .orElseThrow(() -> new ProductNotFoundException("No product with the highest price found"));
                return productMapper.productToProductResponseDto(product);
            }
            case "cheapest" -> {
                Product product = productRepository.findAll().stream()
                        .min(Comparator.comparing(Product::getPrice))
                        .orElseThrow(() -> new ProductNotFoundException("No product with the lowest price found"));
                return productMapper.productToProductResponseDto(product);
            }
            default -> {
                log.error("Unknown parameter: {}", param);
                throw new IllegalArgumentException("Unknown parameter: " + param);
            }
        }
    }

    private void validateProduct(Product product) {
        log.debug("Checking product {}", product);
        if (productRepository.findProduct(product).isPresent()) {
            log.error("Product already exists");
            throw new ProductExistsException(PRODUCT_ALREADY_EXISTS);
        }
    }

    private Product findProductByIdOrThrow(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> {
            log.error("Product with id {} not found", productId);
            return new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND, productId));
        });
    }
}