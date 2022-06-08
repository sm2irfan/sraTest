package com.optus.infosec.api.controller;

import com.optus.infosec.api.dto.RiskDTO;
import com.optus.infosec.api.dto.RiskDtoWrapper;
import com.optus.infosec.api.dto.RisksByTemporal;
import com.optus.infosec.api.service.RiskService;
import com.optus.infosec.api.service.repository.RiskRepositoryService;
import com.optus.infosec.domain.enums.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.optus.infosec.domain.enums.Level;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author SM
 * <p>
 * Controller for Risks
 */
@RestController
public class RiskController {

    private static final Logger LOG = LoggerFactory.getLogger(RiskController.class);

    @Autowired
    private RiskService riskService;

    @Autowired
    private RiskRepositoryService riskRepositoryService;

    /**
     * Get Operation for Risk
     *
     * @param engagementId
     * @param riskId
     * @return ResponseEntity<RiskEntity>
     */
    @GetMapping(value = "/engagements/{engagementId}/risks/{riskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RiskDTO> getRisk(@PathVariable(required = true) Long engagementId, @PathVariable(required = true) Long riskId) {

        LOG.debug("Engagement Id : {} \n Risk Id : {}", engagementId, riskId);
        RiskDTO riskDTO = riskService.getRisk(engagementId, riskId);
        return new ResponseEntity<>(riskDTO, HttpStatus.OK);
    }

    /**
     * Get List of Risk
     *
     * @param engagementId
     * @return ResponseEntity<List < RiskDTO>>
     */
    @GetMapping(value = "/engagements/{engagementId}/risks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RiskDTO>> getRiskList(@PathVariable(required = false) Long engagementId) {

        LOG.debug("Get Risks for Engagement Id : {}", engagementId);
        List<RiskDTO> riskDTOList = riskService.getRiskList(engagementId);
        return new ResponseEntity<>(riskDTOList, HttpStatus.OK);
    }

    /**
     * Get List of Risk
     *
     * @return ResponseEntity<List < RiskDTO>>
     */
    @GetMapping(value = "/engagements/risks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RiskDTO>> getRiskList(@RequestParam(required = false) Long engagementId,
                                                     @RequestParam(required = false) Long riskId,
                                                     @RequestParam(required = false) String projectName,
                                                     @RequestParam(required = false) String businessOwner,
                                                     @RequestParam(required = false) String infosecResource,
                                                     @RequestParam(required = false) Status riskStatus) {

        LOG.debug("Get All Risks");
        List<RiskDTO> riskDTOList = riskService.getRiskList(engagementId, riskId, projectName, businessOwner, infosecResource, riskStatus);
        return new ResponseEntity<>(riskDTOList, HttpStatus.OK);
    }

    /*    *//**
     * Post Operation for Risk
     *
     * @param engagementId
     * @param riskDTO
     *
     * @return ResponseEntity<Void>
     *//*
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> postRisk (@PathVariable(required = true) Long engagementId, @RequestBody RiskDTO riskDTO) {

        LOG.debug("Engagement Id : {} \n Risk DTO : {}", engagementId, riskDTO);
        RiskDTO createdRiskDTO = riskService.createRisk(engagementId, riskDTO);
        return new ResponseEntity<>(createdRiskDTO.getRiskId(), HttpStatus.CREATED);
    }*/

    /**
     * Post Operation for Risk
     *
     * @param engagementId
     * @param riskDTOList
     * @return ResponseEntity<Void>
     */
    @PostMapping(value = "/engagements/{engagementId}/risks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Long>> postRiskList(@PathVariable(required = true) Long engagementId, @RequestBody List<RiskDTO> riskDTOList) {

        LOG.debug("Engagement Id : {} \n Risk DTO List : {}", engagementId, riskDTOList);
        List<RiskDTO> createdRiskDTO = riskService.createRiskList(engagementId, riskDTOList);
        return new ResponseEntity<>(createdRiskDTO.stream().map(s -> s.getRiskId()).collect(Collectors.toList()), HttpStatus.CREATED);
    }

    /**
     * Put Operation for Risk
     *
     * @param engagementId
     * @param riskId
     * @param riskDTO
     * @return ResponseEntity<Void>
     */
    @PutMapping(value = "/engagements/{engagementId}/risks/{riskId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> putRisk(@PathVariable(required = true) Long engagementId, @PathVariable(required = true) Long riskId, @RequestBody RiskDTO riskDTO) {

        LOG.debug("Engagement Id : {} \n Risk Id : {} \n Risk DTO : {}", engagementId, riskId, riskDTO);
        RiskDTO returnedRiskDTO = riskService.updateRisk(engagementId, riskId, riskDTO);
        return new ResponseEntity<>(returnedRiskDTO.getRiskId(), HttpStatus.NO_CONTENT);
    }

    /**
     * Updates/Created list of Risks
     *
     * @param engagementId
     * @param riskDTOList
     * @return
     */
    @PutMapping(value = "/engagements/{engagementId}/risks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Long>> putRisks(@PathVariable(required = true) Long engagementId, @RequestBody List<RiskDTO> riskDTOList) {

        LOG.debug("Engagement Id : {} \n Risk DTO List : {}", engagementId, riskDTOList);
        List<RiskDTO> returnedRiskDTOList = riskService.createUpdateRiskList(engagementId, riskDTOList);
        return new ResponseEntity<>(returnedRiskDTOList.stream().map(s -> s.getRiskId()).collect(Collectors.toList()), HttpStatus.OK);
    }


    @GetMapping(value = "/engagements/searchRisks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RiskDtoWrapper> searchRisks
            (@RequestParam(required = false) Long engagementId,
             @RequestParam(required = false) Level riskLevel,
             @RequestParam(required = false) String projectName,
             @RequestParam(required = false) Long riskId,
             @RequestParam(required = false) String riskName,
             @RequestParam(required = false) String riskOwner,
             @RequestParam(required = false) Status riskStatus,
             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate raisedDatetime,
             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate completedDatetime,
             @RequestParam(required = false) Integer page,
             @RequestParam(required = false) Integer size) {

        return new ResponseEntity<>(this.riskRepositoryService.searchRisks(engagementId, riskLevel, projectName, riskId, riskName, riskOwner, riskStatus,
                raisedDatetime,
                completedDatetime, page, size),
                HttpStatus.OK);
    }

    @GetMapping("getRisksCount")
    public ResponseEntity<Map<String, Integer>> getRisksCount() {
        return this.riskService.countRisks();
    }

    @GetMapping("getRisksByMonthlyBasis")
    public List<RisksByTemporal> getRisksByMonthlyBasis() {
        return this.riskService.getRisksByMonthlyBasis();
    }

    @GetMapping("getRisksByWeeklyBasis")
    public List<RisksByTemporal> getRisksByWeeklyBasis() {
        return this.riskService.getRisksByWeeklyBasis();
    }

}
