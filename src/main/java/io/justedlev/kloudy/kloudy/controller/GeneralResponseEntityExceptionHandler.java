package io.justedlev.kloudy.kloudy.controller;

import io.justedlev.kloudy.kloudy.model.ErrorDetails;
import io.justedlev.kloudy.kloudy.model.ValidationErrorResponse;
import io.justedlev.kloudy.kloudy.model.ViolationResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;

@Slf4j
@ControllerAdvice
public class GeneralResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {
            EntityNotFoundException.class,
            FileNotFoundException.class,
    })
    public ResponseEntity<Object> handleNotFountException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        var errorDetailsResponse = ErrorDetails.builder()
                .details(request.getDescription(false))
                .message(ex.getMessage())
                .build();
        var problemDetails = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        var res = ErrorResponse.builder(ex, problemDetails)
                .detail(ex.getMessage())
                .build();

        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object>
    handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {

        log.error(ex.getMessage(), ex);
        var violations = ex.getConstraintViolations()
                .stream()
                .map(current -> ViolationResponse.builder()
                        .fieldName(current.getPropertyPath().toString())
                        .message(current.getMessage())
                        .build())
                .toList();
        var response = ValidationErrorResponse.builder()
                .details(request.getDescription(false))
                .violations(violations)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
//                                                                  @NonNull HttpHeaders headers,
//                                                                  @NonNull HttpStatusCode status,
//                                                                  @NonNull WebRequest request) {
//
//        return super.handleMethodArgumentNotValid(ex, headers, status, request);
//        log.error(ex.getMessage(), ex);
//        var violations = ex.getBindingResult().getFieldErrors()
//                .stream()
//                .map(current -> ViolationResponse.builder()
//                        .fieldName(current.getField())
//                        .message(current.getDefaultMessage())
//                        .build())
//                .toList();
//        var error = ValidationErrorResponse.builder()
//                .details(request.getDescription(false))
//                .violations(violations)
//                .build();
//
//        return new ResponseEntity<>(error, status);
//    }
}
