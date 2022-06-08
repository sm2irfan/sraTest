package com.optus.infosec.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author SM
 *
 * Service to manage Client and Server Errors
 */
@Component
public class ErrorDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorDetailService.class);

    @Value("${error.details.file}")
    private String errorDetailsFile;

    @Autowired
    private DefaultResourceLoader defaultResourceLoader;

    @Autowired
    private ObjectMapper objectMapper;
    /**
     * Reads list of errors from an external file.
     * Results are cached.
     *
     * @return List<ErrorDetail>
     */

    public List<ErrorDetail> getErrorDetails(){

        final Resource resource = defaultResourceLoader.getResource(errorDetailsFile);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(resource.getInputStream(), List.class);
        } catch (IOException e) {
            LOGGER.error("Error In Loading Error Codes File");
        }
        return new ArrayList<>();
    }

    /**
     * Get ErrorDetail
     *
     * @param errorName
     * @return ErrorDetail
     */
    public ErrorDetail getErrorDetail(ErrorName errorName){

        List<ErrorDetail> errorDetails = getErrorDetails();
        // find error detail from the list using ErrorName
        Optional<ErrorDetail> errorDetailOptional = errorDetails.stream().filter(errorDetail -> errorDetail.getErrorName().equals(errorName)).findFirst();
        if(errorDetailOptional.isPresent()){
            return errorDetailOptional.get();
        }
        // return default error code if not found
        return errorDetails.stream().filter(errorDetail -> errorDetail.getErrorName().equals(ErrorName.DEFAULT)).findFirst().get();
    }
}