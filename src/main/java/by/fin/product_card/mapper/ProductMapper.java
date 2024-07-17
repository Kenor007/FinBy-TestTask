package by.fin.product_card.mapper;

import by.fin.product_card.dto.ProductRequestDto;
import by.fin.product_card.dto.ProductResponseDto;
import by.fin.product_card.model.Product;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    @Mapping(target = "description", expression = "java(generateDescription(productRequestDto))")
    Product productRequestDtoToProduct(ProductRequestDto productRequestDto);

    @Mapping(target = "description", expression = "java(generateDescription(productRequestDto))")
    Product productRequestDtoToUpdatedProduct(ProductRequestDto productRequestDto, @MappingTarget Product product);

    ProductResponseDto productToProductResponseDto(Product product);

    default String generateDescription(ProductRequestDto productRequestDto) {
        return String.format("%s %s, цвет: %s. %s",
                productRequestDto.getName(),
                productRequestDto.getBrand(),
                productRequestDto.getColor(),
                String.join(", ", productRequestDto.getFeatures()));
    }
}