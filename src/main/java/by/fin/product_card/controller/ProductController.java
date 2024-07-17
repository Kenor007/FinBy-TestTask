package by.fin.product_card.controller;

import by.fin.product_card.dto.ProductRequestDto;
import by.fin.product_card.dto.ProductResponseDto;
import by.fin.product_card.error_handling.exception.ExceptionResponse;
import by.fin.product_card.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static by.fin.product_card.error_handling.constant.ExceptionAnswer.POSITIVE_ID;

@Tag(name = "Product Controller", description = "API for working with products")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Create a product by params",
            description = "Returns information about the product, if the product has been created")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - The product already exists", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product to add", content =
            @Content(schema = @Schema(implementation = ProductRequestDto.class)))
            @Valid @RequestBody ProductRequestDto productRequestDto) {
        log.debug("Creating product: {}", productRequestDto);
        return productService.createProduct(productRequestDto);
    }

    @Operation(summary = "Get information about the product by id",
            description = "Returns an information about the product as per the id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved", content =
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found - The product was not found", content =
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("/{id}")
    public ProductResponseDto findProductById(
            @PathVariable("id") @Positive(message = POSITIVE_ID)
            @Parameter(name = "id", description = "Product id", example = "1") Long id) {
        log.debug("Getting product by id: {}", id);
        return productService.findProductById(id);
    }

    @Operation(summary = "Get a list of all products with their information",
            description = "Returns a list of all products with their information")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "The list has been successfully retrieved", content =
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))))
            }
    )
    @GetMapping()
    public List<ProductResponseDto> findAllProducts() {
        log.debug("Getting all products");
        return productService.findAllProducts();
    }

    @Operation(summary = "Update product by id",
            description = "Returns an information about the updated product as per the id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated", content =
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found - The product for updating was not found", content =
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PutMapping("/{id}")
    public ProductResponseDto updateProductById(
            @PathVariable("id") @Positive(message = POSITIVE_ID)
            @Parameter(name = "id", description = "Product id", example = "1") Long id,
            @Valid @RequestBody ProductRequestDto productRequestDto) {
        log.debug("Updating product {} by id {}", productRequestDto, id);
        return productService.updateProductById(id, productRequestDto);
    }

    @Operation(summary = "Delete product by id",
            description = "Returns NO CONTENT if product has been deleted by id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Not found - The product for deleting was not found", content =
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(
            @PathVariable("id") @Positive(message = POSITIVE_ID)
            @Parameter(name = "id", description = "Product id", example = "1") Long id) {
        log.debug("Deleting product by id {}", id);
        productService.deleteProductById(id);
    }

    @Operation(summary = "Get product information by the specified parameter",
            description = "Returns the information of product by specified parameter: highest rating, expensive or cheapest")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "The product has been successfully retrieved", content =
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "The product was not found by the specified parameter", content =
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("/search/{param}")
    public ProductResponseDto findProductByParam(
            @PathVariable("param") @NotBlank(message = "param should not be blank")
            @Parameter(name = "param", description = "Search param", example = "highest rating, expensive or cheapest") String param) {
        log.debug("Getting product by param: {}", param);
        return productService.findProductByParam(param);
    }
}