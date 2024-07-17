package by.fin.product_card.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ProductResponseDto {
    @Schema(example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(example = "Прекрасные наушники", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(example = "Acme", requiredMode = Schema.RequiredMode.REQUIRED)
    private String brand;

    @Schema(example = "Model123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String model;

    @Schema(example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    private int availableQuantity;

    @Schema(example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean availability;

    @Schema(example = "200 г", requiredMode = Schema.RequiredMode.REQUIRED)
    private String weight;

    @Schema(example = "4.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private double rating;

    @Schema(example = "Электроника", requiredMode = Schema.RequiredMode.REQUIRED)
    private String category;

    @Schema(example = "черный", requiredMode = Schema.RequiredMode.REQUIRED)
    private String color;

    @Schema(example = "49.99", requiredMode = Schema.RequiredMode.REQUIRED)
    private double price;

    @Schema(example = "headphones.jpg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String photoName;

    @Schema(example = "https://plus.unsplash.com/premium_photo-1678099940967", requiredMode = Schema.RequiredMode.REQUIRED)
    private String imageUrl;

    @Schema(example = "2 года", requiredMode = Schema.RequiredMode.REQUIRED)
    private String warranty;

    @Schema(example = "Прекрасные наушники Acme, цвет: черный. Активное шумоподавление, Складной дизайн, Встроенный микрофон", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Schema(example = "[\"Активное шумоподавление\", \"Складной дизайн\", \"Встроенный микрофон\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<String> features;
}