package com.sethu.ems.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFundException extends RuntimeException {

    public ResourceNotFundException(String message) {
        super(message);
    }
}
