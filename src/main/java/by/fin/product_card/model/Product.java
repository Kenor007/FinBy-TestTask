package by.fin.product_card.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;

    @Column(name = "availability", nullable = false)
    private boolean availability;

    @Column(name = "weight", nullable = false)
    private String weight;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "photo_name", nullable = false)
    private String photoName;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "photo", nullable = false)
    private byte[] photo;

    @Column(name = "warranty", nullable = false)
    private String warranty;

    @Column(name = "description", nullable = false)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_features", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "feature")
    private Set<String> features;
}