package com.optus.infosec.api.exception;

/**
 * @author SM
 *
 * Utilit class for Exceptions
 */
public class ExceptionsUtil {

    private ExceptionsUtil(){
        // private constructor
    }

    /**
     * Get Service Exception
     *
     * @param errorDetail
     * @return ServiceException
     */
    public static ServiceException getServiceException(ErrorDetail errorDetail){
        return new ServiceException(
                errorDetail.getErrorCode(),
                errorDetail.getErrorMessage(),
                errorDetail.getHttpStatusCode()
        );
    }
}
