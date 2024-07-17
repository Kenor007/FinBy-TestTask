package by.fin.product_card.repository;

import by.fin.product_card.model.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    default Optional<Product> findProduct(Product product) {
        Specification<Product> specification = Specification.where(nameIn(product.getName()))
                .and(brandIn(product.getBrand()))
                .and(modelIn(product.getModel()))
                .and(availableQuantityIn(product.getAvailableQuantity()))
                .and(availabilityIn(product.isAvailability()))
                .and(weightIn(product.getWeight()))
                .and(ratingIn(product.getRating()))
                .and(categoryIn(product.getCategory()))
                .and(colorIn(product.getColor()))
                .and(priceIn(product.getPrice()))
                .and(photoNameIn(product.getPhotoName()))
                .and(imageUrlIn(product.getImageUrl()))
                .and(warrantyIn(product.getWarranty()))
                .and(descriptionIn(product.getDescription()))
                .and(featuresIn(product.getFeatures()));
        return findOne(specification);
    }

    default Specification<Product> nameIn(String name) {
        return attributeEquals("name", name);
    }

    default Specification<Product> brandIn(String brand) {
        return attributeEquals("brand", brand);
    }

    default Specification<Product> modelIn(String model) {
        return attributeEquals("model", model);
    }

    default Specification<Product> availableQuantityIn(int availableQuantity) {
        return (product, cq, cb) -> cb.equal(product.get("availableQuantity"), availableQuantity);
    }

    default Specification<Product> availabilityIn(boolean availability) {
        return (product, cq, cb) -> cb.equal(product.get("availability"), availability);
    }

    default Specification<Product> weightIn(String weight) {
        return attributeEquals("weight", weight);
    }

    default Specification<Product> ratingIn(double rating) {
        return (product, cq, cb) -> cb.equal(product.get("rating"), rating);
    }

    default Specification<Product> categoryIn(String category) {
        return attributeEquals("category", category);
    }

    default Specification<Product> colorIn(String color) {
        return attributeEquals("color", color);
    }

    default Specification<Product> priceIn(BigDecimal price) {
        return (product, cq, cb) -> cb.equal(product.get("price"), price);
    }

    default Specification<Product> photoNameIn(String photoName) {
        return attributeEquals("photoName", photoName);
    }

    default Specification<Product> imageUrlIn(String imageUrl) {
        return attributeEquals("imageUrl", imageUrl);
    }

    default Specification<Product> warrantyIn(String warranty) {
        return attributeEquals("warranty", warranty);
    }

    default Specification<Product> descriptionIn(String description) {
        return attributeEquals("description", description);
    }

    default Specification<Product> featuresIn(Set<String> features) {
        return (product, cq, cb) -> {
            if (isNotEmpty(features)) {
                List<Predicate> predicates = new ArrayList<>();
                for (String feature : features) {
                    Subquery<Long> sq = cq.subquery(Long.class);
                    Root<Product> productRoot = sq.correlate(product);
                    Join<Product, String> specialFeaturesJoin = productRoot.join("features");
                    sq.select(productRoot.get("id"))
                            .where(cb.equal(cb.lower(specialFeaturesJoin), feature.toLowerCase()));
                    predicates.add(cb.exists(sq));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            } else {
                return cb.conjunction();
            }
        };
    }

    private static Specification<Product> attributeEquals(String attributeName, String value) {
        return (product, cq, cb) -> isNotNull(value)
                ? cb.equal(cb.lower(product.get(attributeName)), value.toLowerCase()) : cb.conjunction();
    }

    private static boolean isNotNull(String line) {
        return Objects.nonNull(line);
    }

    private static boolean isNotEmpty(Collection<?> collection) {
        return Objects.nonNull(collection) && !collection.isEmpty();
    }
}