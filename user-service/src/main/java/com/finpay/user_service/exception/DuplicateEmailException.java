package com.finpay.user_service.exception;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(){
        super("This email is already registered");
    }
}
