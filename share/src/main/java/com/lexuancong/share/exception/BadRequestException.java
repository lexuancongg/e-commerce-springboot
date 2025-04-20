package com.lexuancong.share.exception;

import com.lexuancong.share.utils.MessagesUtils;

public class BadRequestException extends RuntimeException

{
    private final String message;

    public BadRequestException(String errorKey ,Object... prams ) {
        this.message = MessagesUtils.getMessage(errorKey,prams);
    }
    @Override
    public String getMessage() {
        return this.message;
    }
}
