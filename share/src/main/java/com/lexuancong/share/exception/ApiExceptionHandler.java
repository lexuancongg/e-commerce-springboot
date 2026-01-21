package com.lexuancong.share.exception;

import com.lexuancong.share.dto.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {
    private  final String INVALID_REQUEST_INFORMATION_MESSAGE = "Request information is not valid";
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        String message = exception.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value() , httpStatus.getReasonPhrase(), message , new ArrayList<>());
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }


    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedException(DuplicatedException exception, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        String message = exception.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value() , httpStatus.getReasonPhrase(), message , new ArrayList<>());
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }



    // khi không pass qua được @Valid trong controller
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        // đối tượng chua thong tin lỗi
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> errors =  fieldErrors.stream()
                .map(fieldError -> fieldError.getField() + fieldError.getDefaultMessage())
                .toList();

        ErrorResponse errorResponse =
                new ErrorResponse(httpStatus.value(),httpStatus.getReasonPhrase(),this.INVALID_REQUEST_INFORMATION_MESSAGE,errors);
        return ResponseEntity.status(httpStatus).body(errorResponse);

    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String message = exception.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value() , httpStatus.getReasonPhrase(), message , new ArrayList<>());
        return ResponseEntity.status(httpStatus).body(errorResponse);

    }




}
