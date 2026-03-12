package com.lexuancong.share.exception;


import com.lexuancong.share.utils.MessagesUtils;

public class ResourceInUseException extends RuntimeException{
    private final String message;
    public ResourceInUseException(String message, Object ...params) {
        this.message = MessagesUtils.getMessage(message, params);
    }

    @Override
    public String getMessage() {
        return message;
    }
}

