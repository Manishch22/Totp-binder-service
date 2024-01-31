package io.mosip.totpbinderservice.exception;

import io.mosip.totpbinderservice.helper.Constants;

public class BindingException extends RuntimeException{

    private String errorCode;

    public BindingException() {
        super(Constants.UNKNOWN_ERROR);
        this.errorCode = Constants.UNKNOWN_ERROR;
    }

    public BindingException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
