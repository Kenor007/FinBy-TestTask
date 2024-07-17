package by.fin.product_card.error_handling.advice;

import by.fin.product_card.error_handling.exception.BadRequestException;
import by.fin.product_card.error_handling.exception.ExceptionResponse;
import by.fin.product_card.error_handling.exception.ProductNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {ProductNotFoundException.class})
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex, WebRequest webRequest) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage(),
                notFound,
                webRequest.getDescription(false),
                ZonedDateTime.now(ZoneId.systemDefault())
        );
        return new ResponseEntity<>(exceptionResponse, notFound);
    }

    @ExceptionHandler(value = {BadRequestException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleBadRequestException(RuntimeException ex, WebRequest webRequest) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage(),
                badRequest,
                webRequest.getDescription(false),
                ZonedDateTime.now(ZoneId.systemDefault())
        );
        return new ResponseEntity<>(exceptionResponse, badRequest);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                        WebRequest webRequest) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        for (FieldError error : errors) {
            errorMessage.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append(";");
        }
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                errorMessage.toString(),
                badRequest,
                webRequest.getDescription(false),
                ZonedDateTime.now(ZoneId.systemDefault())
        );
        return new ResponseEntity<>(exceptionResponse, badRequest);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
                                                                     WebRequest webRequest) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getConstraintViolations()
                        .stream().map(violation -> String.format("%s value '%s' %s",
                                StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                                        .reduce((first, second) -> second).orElse(null),
                                violation.getInvalidValue(), violation.getMessage())).toList().toString(),
                badRequest,
                webRequest.getDescription(false),
                ZonedDateTime.now(ZoneId.systemDefault())
        );
        return new ResponseEntity<>(exceptionResponse, badRequest);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
                                                                            WebRequest webRequest) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                "Invalid format of " + ex.getName(),
                badRequest,
                webRequest.getDescription(false),
                ZonedDateTime.now(ZoneId.systemDefault())
        );
        return new ResponseEntity<>(exceptionResponse, badRequest);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleJsonParseError(HttpMessageNotReadableException ex, WebRequest webRequest) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                "Malformed JSON request",
                badRequest,
                webRequest.getDescription(false),
                ZonedDateTime.now(ZoneId.systemDefault())
        );
        return new ResponseEntity<>(exceptionResponse, badRequest);
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest webRequest) {
        HttpStatus methodNotAllowed = HttpStatus.METHOD_NOT_ALLOWED;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage(),
                methodNotAllowed,
                webRequest.getDescription(false),
                ZonedDateTime.now(ZoneId.systemDefault())
        );
        return new ResponseEntity<>(exceptionResponse, methodNotAllowed);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest webRequest) {
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                internalServerError.getReasonPhrase(),
                internalServerError,
                webRequest.getDescription(false),
                ZonedDateTime.now(ZoneId.systemDefault())
        );
        return new ResponseEntity<>(exceptionResponse, internalServerError);
    }
}