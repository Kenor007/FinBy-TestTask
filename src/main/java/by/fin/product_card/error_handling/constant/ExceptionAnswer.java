package by.fin.product_card.error_handling.constant;

public final class ExceptionAnswer {
    public static final String POSITIVE_ID = "Id must not be less than one";
    public static final String PRODUCT_NOT_FOUND = "The product with id %s not found";
    public static final String PRODUCT_ALREADY_EXISTS = "The product already exists";
    public static final String ERROR_DOWNLOADING_IMAGE = "Error downloading image from URL: %s";
    public static final String ERROR_SAVING_IMAGE = "Error saving image to path: %s";
}