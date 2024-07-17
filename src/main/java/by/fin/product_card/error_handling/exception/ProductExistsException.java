package by.fin.product_card.error_handling.exception;

public class ProductExistsException extends BadRequestException {
    public ProductExistsException(String message) {
        super(message);
    }
}