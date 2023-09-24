package com.shashwat.blog_app_youtube.Exception;

public class ApiException extends RuntimeException{
    public ApiException(String failure, String m) {
    }

    public ApiException(String message) {
        super(message);
    }
}
