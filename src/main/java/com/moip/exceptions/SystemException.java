package com.moip.exceptions;

/**
 * This exception represents the class of errors generated by the system (not
 * users fault). Usually this is the equivalent of the 5xx HTTP error classes
 * and the applications' exception mapper handles it returned the better 5xx
 * error to the user.
 */
public class SystemException extends ApplicationException {
    private static final long serialVersionUID = 5159913781729993872L;

    public SystemException(final SystemErrorCode code) {
        super(code);
    }

    public SystemException(final SystemErrorCode code, final Throwable cause) {
        super(code, cause);
    }
}