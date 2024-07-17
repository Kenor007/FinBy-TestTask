package by.fin.product_card.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class ProductRequestDto {
    @Schema(example = "Прекрасные наушники", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product name should not be blank")
    @Size(min = 1, message = "Product name should contain at least 1 character")
    private String name;

    @Schema(example = "Acme", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product brand should not be blank")
    @Size(min = 1, message = "Product brand should contain at least 1 character")
    private String brand;

    @Schema(example = "Model123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product model should not be blank")
    @Size(min = 1, message = "Product model should contain at least 1 character")
    private String model;

    @Schema(example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Product available quantity should not be empty")
    @Min(value = 0, message = "Product available quantity must not be less than zero")
    private Integer availableQuantity;

    @Schema(example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Product availability is required")
    private Boolean availability;

    @Schema(example = "200 г", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product weight should not be blank")
    @Size(min = 1, message = "Product weight should contain at least 1 character")
    private String weight;

    @Schema(example = "4.5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Product rating is required")
    @DecimalMin(value = "0.0", message = "Product rating cannot be less than 0.0")
    @DecimalMax(value = "10.0", message = "Product rating cannot be higher than 10.0")
    @Digits(integer = 2, fraction = 1, message = "Product rating must have an accuracy of 1 digits")
    private Double rating;

    @Schema(example = "Электроника", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product category should not be blank")
    @Size(min = 1, message = "Product category should contain at least 1 character")
    private String category;

    @Schema(example = "черный", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product color should not be blank")
    @Size(min = 1, message = "Product color should contain at least 1 character")
    private String color;

    @Schema(example = "49.99", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.01", message = "Product price cannot be less than 0.01")
    @Digits(integer = 10, fraction = 2, message = "Product price must have an accuracy of 2 digits")
    private BigDecimal price;

    @Schema(example = "headphones.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Photo name should not be blank")
    @Size(min = 1, message = "Photo name should contain at least 1 character")
    private String photoName;

    @Schema(example = "https://plus.unsplash.com/premium_photo-1678099940967", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].\\S*$", message = "Invalid image URL")
    private String imageUrl;

    @Schema(example = "2 года", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product warranty should not be blank")
    @Size(min = 1, message = "Product warranty should contain at least 1 character")
    private String warranty;

    @Schema(example = "[\"Активное шумоподавление\", \"Складной дизайн\", \"Встроенный микрофон\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Product features should not be empty")
    private Set<String> features;
}