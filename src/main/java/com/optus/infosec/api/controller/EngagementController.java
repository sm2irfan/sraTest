package com.optus.infosec.api.controller;

import com.optus.infosec.api.dto.EngagementDTO;
import com.optus.infosec.api.dto.EngagementDtoWrapper;
import com.optus.infosec.api.dto.EngagementsByTemporal;
import com.optus.infosec.api.exception.ServiceException;
import com.optus.infosec.api.service.EngagementService;
import com.optus.infosec.api.service.LocalFileService;
import com.optus.infosec.api.service.repository.EngagementRepositoryService;
import com.optus.infosec.domain.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author SM
 * <p>
 * Controller for Engagements
 */
@RestController
@RequestMapping("/engagements")
public class EngagementController {

    private static final Logger LOG = LoggerFactory.getLogger(EngagementController.class);

    @Autowired
    private EngagementService engagementService;

    @Autowired
    private EngagementRepositoryService engagementRepositoryService;


    @Autowired
    LocalFileService localFileService;

    /**
     * Get Operation for Engagements
     *
     * @param engagementId
     * @return ResponseEntity<EngagementEntity>
     */
    @GetMapping(value = "/{engagementId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EngagementDTO> getEngagement(@PathVariable(required = true) Long engagementId) {

        LOG.debug("Engagement Id : {}", engagementId);
        EngagementDTO engagementDTO = engagementService.getEngagement(engagementId);
        return new ResponseEntity<>(engagementDTO, HttpStatus.OK);
    }

    /**
     * Get Operation for Engagements
     *
     * @return ResponseEntity<EngagementEntity>
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EngagementDTO>> getEngagements(@RequestParam(required = false) String requestedBy,
                                                              @RequestParam(required = false) Long engagementId,
                                                              @RequestParam(required = false) String projectName,
                                                              @RequestParam(required = false) String infosecResource,
                                                              @RequestParam(required = false) Status engagementStatus,
                                                              @RequestParam(required = false) String emailId) {

        LOG.debug("Get All Engagements: Requested By : {}", requestedBy);
        List<EngagementDTO> engagementDTOList = engagementService.getEngagements(requestedBy,
                engagementId,
                projectName,
                infosecResource,
                engagementStatus,
                emailId);
        return new ResponseEntity<>(engagementDTOList, HttpStatus.OK);
    }

    /**
     * Post Operation for Engagements
     *
     * @param engagementDTO
     * @return ResponseEntity<Void>
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> postEngagement(@RequestBody EngagementDTO engagementDTO) {

        LOG.debug("Engagement DTO : {}", engagementDTO);
        EngagementDTO createdEngagementDTO = engagementService.createEngagement(engagementDTO);
        return new ResponseEntity<>(createdEngagementDTO.getEngagementId(), HttpStatus.CREATED);
    }

    /**
     * Put Operation for Engagements
     *
     * @param engagementDTO
     * @return ResponseEntity<Void>
     */
    @PutMapping(value = "/{engagementId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putEngagement(@PathVariable(required = true) Long engagementId,
                                              @RequestBody EngagementDTO engagementDTO) throws Exception {

        LOG.debug("Engagement Id : {}", engagementId);
        LOG.debug("Engagement DTO : {}", engagementDTO);
        LOG.debug(engagementDTO.toString());
        engagementService.updateEngagement(engagementId, engagementDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * @param engagementId
     * @return
     */
    @PostMapping(value = "/{engagementId}/certificate")
    public ResponseEntity<String> generateEngagementCertificate(@PathVariable(required = true) Long engagementId) {
        LOG.debug("Generating Certificate For Engagement Id : {}", engagementId);
        try {
            engagementService.generateAndSendEngagementCertificate(engagementId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param engagementId
     * @param projectName
     * @param requestedBy
     * @param engagementStatus
     * @param requestedDatetime
     * @param completedDatetime
     * @param assignedTo
     */
    @GetMapping("/searchEngagements")
    public ResponseEntity<EngagementDtoWrapper> searchEngagements
    (@RequestParam(required = false) Long engagementId,
     @RequestParam(required = false) String projectName,
     @RequestParam(required = false) String requestedBy,
     @RequestParam(required = false) Status engagementStatus,
     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestedDatetime,
     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate completedDatetime,
     @RequestParam(required = false) String assignedTo,
     @RequestParam(required = false) Integer numberOfRisks,
     @RequestParam(required = false) Integer page,
     @RequestParam(required = false) Integer size) {
        return new ResponseEntity<>(this.engagementRepositoryService.searchEngagements(engagementId, projectName, requestedBy, engagementStatus,
                requestedDatetime,
                completedDatetime, assignedTo, numberOfRisks, page, size),
                HttpStatus.OK);
    }

    @GetMapping("getEngagementsByMonthlyBasis")
    public List<EngagementsByTemporal> getEngagementsByMonthlyBasis() {
        return this.engagementService.getEngagementsByMonthlyBasis();
    }

    @GetMapping("getEngagementsByWeeklyBasis")
    public List<EngagementsByTemporal> getEngagementsByWeeklyBasis() {
        return this.engagementService.getEngagementsByWeeklyBasis();
    }

    @GetMapping("getEngagementCount")
    public ResponseEntity<Map<String, Integer>> getEngagementCount() {
        return this.engagementService.countEngagements();
    }

    @GetMapping("listFiles/{engagementId}")
    ResponseEntity<List<String>> listFiles(@PathVariable Long engagementId) {
        return this.localFileService.listFiles(engagementId);
    }

    @GetMapping(value = "/{engagementId}/file")
    public ResponseEntity<Void> downloadEngagementFilesAsZipFromLocalDisk(@PathVariable()
                                                                                  Long engagementId,
                                                                          HttpServletResponse response) {
        response.setContentType("application/zip");
        response.setHeader("Content-disposition", "attachment; filename=" + engagementId + ".zip");

        byte[] zipFileByteArray = localFileService.getZipFileByteArrayForEngagement(engagementId);
        try {
            response.getOutputStream().write(zipFileByteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
