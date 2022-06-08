package com.optus.infosec.api.service;

import com.optus.infosec.api.dto.EngagementFormDTO;
import com.optus.infosec.api.service.repository.EngagementFormRepositoryService;
import com.optus.infosec.api.service.repository.EngagementRepositoryService;
import com.optus.infosec.api.service.wrapper.FileService;
import com.optus.infosec.domain.enums.EngagementForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;

/**
 * @author SM
 * <p>
 * Service Class for Engagement Forms
 */
@Service
public class EngagementFormService {

    private static final Logger LOG = LoggerFactory.getLogger(EngagementRepositoryService.class);

    @Autowired
    private EngagementFormRepositoryService engagementFormRepositoryService;

    @Value("${engagements.file.location}")
    private String engagementsFileLocation;

    @Autowired
    private FileService fileService;

    @Autowired
    private LocalFileService localFileService;

    /**
     * Creates a new engagement form
     *
     * @param engagementFormDTO
     */
    public void createEngagementForm(Long engagementId, EngagementFormDTO engagementFormDTO) {
        engagementFormRepositoryService.createEngagementForm(engagementId, engagementFormDTO);
    }

    /**
     * Create default engagement forms for the list of engagement form types passed
     *
     * @param engagementId
     * @param engagementFormList
     */
    public void createDefaultEngagementForms(Long engagementId, List<EngagementForm> engagementFormList) {
        engagementFormRepositoryService.createDefaultEngagementForms(engagementId, engagementFormList);
    }

    /**
     * Get Engagement Form
     *
     * @param engagementId
     * @return EngagementEntity
     */
    public EngagementFormDTO getEngagementForm(Long engagementId, EngagementForm engagementFormType) {
        return engagementFormRepositoryService.getEngagementForm(engagementId, engagementFormType);
    }

    /**
     * Get Engagement
     *
     * @param engagementId
     * @return EngagementEntity
     */
    public void updateEngagementForm(Long engagementId, EngagementForm engagementFormType, EngagementFormDTO engagementFormDTO) {
        engagementFormRepositoryService.updateEngagementForm(engagementId, engagementFormType, engagementFormDTO);
    }

    /**
     * Upload file for engagement form
     *
     * @param engagementId
     * @param engagementFormType
     * @param file
     */
    public ResponseEntity<Object> handleEngagementFormFileUpload(Long engagementId, EngagementForm engagementFormType, MultipartFile file) {
       return localFileService.upload(engagementId, engagementFormType.name(), file);
    }

    /**
     * Get file list for engagement form
     *
     * @param engagementId
     * @param engagementFormType
     * @return List<String>
     */
    public List<String> getFileListForEngagementForm(Long engagementId, EngagementForm engagementFormType) {
        return fileService.list(Paths.get(engagementsFileLocation, String.valueOf(engagementId), engagementFormType.name()));
    }

    /**
     * Download file for Engagement Form
     *
     * @param engagementId
     * @param engagementFormType
     * @param fileName
     * @return Resource File
     */
    public Resource downloadEngagementFormFile(Long engagementId, EngagementForm engagementFormType, String fileName) {
        return fileService.download(Paths.get(engagementsFileLocation, String.valueOf(engagementId), engagementFormType.name()), fileName);
    }
}
