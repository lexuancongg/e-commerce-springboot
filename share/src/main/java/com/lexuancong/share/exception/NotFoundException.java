package com.lexuancong.share.exception;

import com.lexuancong.share.utils.MessagesUtils;

// khi không tìm thấy
public class NotFoundException extends RuntimeException{
    private final String message;

    public NotFoundException(String errorKey ,Object... prams ) {
        this.message = MessagesUtils.getMessage(errorKey,prams);
    }
    @Override
    public String getMessage() {
        return this.message;
    }

}
