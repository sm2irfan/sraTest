package com.optus.infosec.api.exception;

/**
 * @author SM
 *
 * Class to capture all the details corresponding to an ErrorName
 */
public class ErrorDetail {

    private ErrorName errorName;
    private String errorCode;
    private String errorMessage;
    private int httpStatusCode;

    //Other details to be added as necessary

    public ErrorName getErrorName() {
        return errorName;
    }

    public void setErrorName(ErrorName errorName) {
        this.errorName = errorName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ErrorDetail that = (ErrorDetail) o;

        if (httpStatusCode != that.httpStatusCode) return false;
        if (errorName != that.errorName) return false;
        if (!errorCode.equals(that.errorCode)) return false;
        return errorMessage.equals(that.errorMessage);

    }

    @Override
    public int hashCode() {
        int result = errorName.hashCode();
        result = 31 * result + errorCode.hashCode();
        result = 31 * result + errorMessage.hashCode();
        result = 31 * result + httpStatusCode;
        return result;
    }

    @Override
    public String toString() {
        return "ErrorDetail{" +
                "errorName=" + errorName +
                ", errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", httpStatusCode=" + httpStatusCode +
                '}';
    }
}
