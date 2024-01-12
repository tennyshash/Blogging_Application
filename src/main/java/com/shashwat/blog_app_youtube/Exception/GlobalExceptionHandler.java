package com.shashwat.blog_app_youtube.Exception;

import com.shashwat.blog_app_youtube.Dtos_Payloads.ApiResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto> resourceNotFoundExceptionHandler(ResourceNotFoundException resourceNotFoundException){
        String message= resourceNotFoundException.getMessage();

        ApiResponseDto responseDto= new ApiResponseDto("FAILURE", message);

        return new ResponseEntity<ApiResponseDto>(responseDto, HttpStatus.NOT_FOUND);
    }



    /*
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
 as we have extended Class ResponseEntityExceptionHandler we have this method in that class   */

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponseDto> handleApiException(ApiException apiException){
        String message= apiException.getMessage();

        ApiResponseDto responseDto= new ApiResponseDto("FAILURE", message);

        return new ResponseEntity<ApiResponseDto>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>("Http Request Method in Valid , Pls Change" , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDto> handleUnAuthoriseException(AccessDeniedException accessDeniedException ){
        String message=accessDeniedException.getMessage();
        ApiResponseDto responseDto= new ApiResponseDto("FAILURE" , message);
        return new ResponseEntity<ApiResponseDto>(responseDto,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponseDto> handleIOException(IOException ioException){
        String message= ioException.getMessage();
        return new ResponseEntity<>(new ApiResponseDto("FAILURE" , message) , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity <ApiResponseDto> handleFileNotFoundException(FileNotFoundException fileNotFoundException){
        String message= fileNotFoundException.getMessage();
        return new ResponseEntity<>(new ApiResponseDto("FAILURE" , message) , HttpStatus.BAD_REQUEST);
    }
}
