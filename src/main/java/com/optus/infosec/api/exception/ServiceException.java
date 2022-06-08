package com.optus.infosec.api.exception;

/**
 * @author SM
 *
 * Custome Class to throw Infosec Portal Exceptions
 *
 */
public class ServiceException extends Exception {

    private String errorCode;
    private String errorMessage;
    private int httpStatusCode;

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ServiceException(String errorCode, String errorMessage, int httpStatusCode) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String toString() {
        return new StringBuilder().append("[ServiceException: {")
                .append(" errorCode: ").append(errorCode)
                .append(" errorMessage: ").append(errorMessage)
                .append(" httpStatusCode: ").append(httpStatusCode)
                .append("}]").toString();
    }
}
