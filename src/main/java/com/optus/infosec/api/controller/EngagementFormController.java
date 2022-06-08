package com.optus.infosec.api.controller;

import com.optus.infosec.api.dto.EngagementFormDTO;
import com.optus.infosec.api.service.EngagementFormService;
import com.optus.infosec.api.service.StorageService;
import com.optus.infosec.domain.enums.EngagementForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author SM
 * <p>
 * Controller for Engagement Forms
 */
@RestController
@RequestMapping("/engagements/{engagementId}")
public class EngagementFormController {

    private static final Logger LOG = LoggerFactory.getLogger(EngagementFormController.class);

    @Autowired
    private EngagementFormService engagementFormService;

    @Autowired
    private StorageService storageService;

    /**
     * Get Operation for Engagement Forms
     *
     * @param engagementId
     * @param engagementFormType
     * @return ResponseEntity<EngagementFormDTO>
     */
    @GetMapping(value = "/engagement-forms/{engagementFormType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EngagementFormDTO> getEngagementForm(@PathVariable(required = true) Long engagementId,
                                                               @PathVariable(required = true) EngagementForm engagementFormType) {

        LOG.debug("Engagement Id : {} \n Engagement Form Type : {}", engagementId, engagementFormType);
        EngagementFormDTO engagementFormDTO = engagementFormService.getEngagementForm(engagementId, engagementFormType);
        if (engagementFormDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(engagementFormDTO, HttpStatus.OK);
    }

    /**
     * Post Operation for Engagement Forms
     *
     * @param engagementId
     * @param engagementFormDTO
     * @return ResponseEntity<Void>
     */
    @PostMapping(value = "/engagement-forms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postEngagementForm(@PathVariable(required = true) Long engagementId, @RequestBody EngagementFormDTO engagementFormDTO) {

        LOG.debug("Engagement Id : {} \n Engagement Form DTO : {}", engagementId, engagementFormDTO);
        engagementFormService.createEngagementForm(engagementId, engagementFormDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * @param engagementId
     * @param engagementFormList
     * @return
     */
    @PostMapping(value = "/default-engagement-forms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postDefaultEngagementForms(@PathVariable(required = true) Long engagementId, @RequestBody List<EngagementForm> engagementFormList) {

        LOG.info("Engagement Id : {} \n Default Engagement Form List To Be Created : {}", engagementId, engagementFormList);
        engagementFormService.createDefaultEngagementForms(engagementId, engagementFormList);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Put Operation for Engagement Forms
     *
     * @param engagementId
     * @param engagementFormType
     * @param engagementFormDTO
     * @return ResponseEntity<Void>
     */
    @PutMapping(value = "/engagement-forms/{engagementFormType}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> putEngagementForm(@PathVariable(required = true) Long engagementId,
                                                  @PathVariable(required = true) EngagementForm engagementFormType,
                                                  @RequestBody EngagementFormDTO engagementFormDTO) {

        LOG.debug("Engagement Id : {} \n Engagement Form Type : {} \n Engagement Form DTO : {}", engagementId, engagementFormType, engagementFormDTO);
        engagementFormService.updateEngagementForm(engagementId, engagementFormType, engagementFormDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * File Upload for engagement form
     *
     * @param engagementFormType
     * @param engagementId
     * @param file
     * @return
     */
    @PostMapping(value = "/engagement-forms/{engagementFormType}/file")
    public ResponseEntity<Object> handleFileUpload(@PathVariable(required = true) Long engagementId,
                                                   @PathVariable(required = true) EngagementForm engagementFormType,
                                                   @RequestBody MultipartFile file) {
        LOG.debug("Uploading File \n For Engagement Id : {} \n Engagement Form Type : {}", engagementId, engagementFormType);
        //   storageService.init();
        return engagementFormService.handleEngagementFormFileUpload(engagementId, engagementFormType, file);
    }



    /**
     * Get list of files
     *
     * @param engagementId
     * @param engagementFormType
     * @return
     */
    @GetMapping(value = "/engagement-forms/{engagementFormType}/files")
    public ResponseEntity<List<String>> listUploadedFiles(@PathVariable(required = true) Long engagementId,
                                                          @PathVariable(required = true) EngagementForm engagementFormType) {

        List<String> fileList = engagementFormService.getFileListForEngagementForm(engagementId, engagementFormType);
        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }

    /**
     * Get file as a resource
     *
     * @param engagementId
     * @param engagementFormType
     * @param filename
     * @return
     */
    @GetMapping("/engagement-forms/{engagementFormType}/file/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable(required = true) Long engagementId,
                                              @PathVariable(required = true) EngagementForm engagementFormType,
                                              @PathVariable String filename) {

        Resource file = engagementFormService.downloadEngagementFormFile(engagementId, engagementFormType, filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"").body(file);
    }
}
