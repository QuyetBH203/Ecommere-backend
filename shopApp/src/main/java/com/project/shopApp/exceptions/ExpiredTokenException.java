package com.project.shopApp.exceptions;

public class ExpiredTokenException extends Exception{
    public ExpiredTokenException(String message){
        super(message);
    }
}
