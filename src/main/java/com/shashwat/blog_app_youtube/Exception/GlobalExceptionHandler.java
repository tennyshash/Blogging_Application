package com.shashwat.blog_app_youtube.Exception;

import com.shashwat.blog_app_youtube.Dtos_Payloads.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto> resourceNotFoundExceptionHandler(ResourceNotFoundException resourceNotFoundException){
        String message= resourceNotFoundException.getMessage();

        ApiResponseDto responseDto= new ApiResponseDto("FAILURE", message);

        return new ResponseEntity<ApiResponseDto>(responseDto, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> methodArgumentValidatorException(MethodArgumentNotValidException exception){

        Map<String,String> responseError= new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach( (objectError) -> {
            var fieldName=((FieldError)objectError).getField();
            var message=objectError.getDefaultMessage();
            responseError.put(fieldName,message);
        });

        return new ResponseEntity<Map<String,String>>(responseError,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponseDto> handleApiException(ApiException apiException){
        String message= apiException.getMessage();

        ApiResponseDto responseDto= new ApiResponseDto("FAILURE", message);

        return new ResponseEntity<ApiResponseDto>(responseDto, HttpStatus.BAD_REQUEST);
    }
}
