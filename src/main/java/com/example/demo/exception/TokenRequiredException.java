package com.example.demo.exception;

public class TokenRequiredException extends RuntimeException{

    public static String message = "";

    public TokenRequiredException(String message){
        super(message);
    }

//    @Override
//    public static String getMessage() {
//        return message;
//    }

    public static void setMessage(String message) {
        TokenRequiredException.message = message;
    }
}
