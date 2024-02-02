package io.mosip.totpbinderservice.exception;

import io.mosip.totpbinderservice.util.ErrorConstants;

public class BindingException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private String errorCode;

    public BindingException() {
        super(ErrorConstants.UNKNOWN_ERROR);
        this.errorCode = ErrorConstants.UNKNOWN_ERROR;
    }

    public BindingException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
