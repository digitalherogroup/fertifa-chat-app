package com.ithd.chat.chatapp.exceptions;

public class IdNotFoundException extends RuntimeException {

    public IdNotFoundException(String id) {
        super(String.format("%s not found exception",id));
    }
}
