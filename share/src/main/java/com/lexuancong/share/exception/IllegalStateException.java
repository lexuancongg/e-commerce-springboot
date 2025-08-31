package com.lexuancong.share.exception;

import com.lexuancong.share.utils.MessagesUtils;

public class IllegalStateException extends RuntimeException{
    private final String message;
    public IllegalStateException(String errorKey ,Object... prams ) {
        this.message = MessagesUtils.getMessage(errorKey,prams);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
