package com.lexuancong.share.exception;

import com.lexuancong.share.utils.MessagesUtils;

// khi dữ liệu bị trùng lặp
public class DuplicatedException extends RuntimeException {
    private final String message;

    public DuplicatedException(String errorKey ,Object... prams ) {
        this.message = MessagesUtils.getMessage(errorKey,prams);
    }
    @Override
    public String getMessage() {
        return this.message;
    }
}
