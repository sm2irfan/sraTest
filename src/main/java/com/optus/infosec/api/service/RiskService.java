package com.optus.infosec.api.service;

import com.optus.infosec.api.dto.EngagementDTO;
import com.optus.infosec.api.dto.RiskDTO;
import com.optus.infosec.api.dto.RisksByTemporal;
import com.optus.infosec.api.service.repository.EngagementRepositoryService;
import com.optus.infosec.api.service.repository.RiskRepositoryService;
import com.optus.infosec.domain.entity.RiskEntity;
import com.optus.infosec.domain.enums.Status;
import com.optus.infosec.repositories.RiskEntityRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author SM
 * <p>
 * Risk Service
 */
@Service
public class RiskService {

    private static final Logger LOG = LoggerFactory.getLogger(RiskService.class);

    @Autowired
    private RiskRepositoryService riskRepositoryService;

    @Autowired
    private EngagementRepositoryService engagementRepositoryService;

    @Autowired
    private RiskEntityRepository repository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmployeeService employeeService;

    @Value("${new.risk.subject}")
    private String newRiskSubject;

    @Value("${update.risk.subject}")
    private String updatedRiskSubject;

    @Value("${new.risk.body}")
    private String newRiskBody;

    @Value("${update.risk.body}")
    private String updatedRiskBody;

    @Value("${from.email}")
    private String fromEmail;

    /**
     * Create Risk
     *
     * @param engagementId
     * @param riskDTO
     * @return
     */
    public RiskDTO createRisk(Long engagementId, RiskDTO riskDTO) {
        return riskRepositoryService.createRisk(engagementId, riskDTO);
    }

    /**
     * Create a List of Risks
     *
     * @param engagementId
     * @param riskDTOList
     * @return
     */
    public List<RiskDTO> createRiskList(Long engagementId, List<RiskDTO> riskDTOList) {
        return riskRepositoryService.createRiskList(engagementId, riskDTOList);
    }

    /**
     * Update Risk
     *
     * @param engagementId
     * @param riskId
     * @param riskDTO
     * @return
     */
    public RiskDTO updateRisk(Long engagementId, Long riskId, RiskDTO riskDTO) {
        return riskRepositoryService.updateRisk(engagementId, riskId, riskDTO);
    }


    public List<RiskDTO> updateRiskList(Long engagementId, List<RiskDTO> riskDTOList) {
        return riskRepositoryService.updateRiskList(engagementId, riskDTOList);
    }

    /**
     * Get Risk
     *
     * @param engagementId
     * @param riskId
     * @return
     */
    public RiskDTO getRisk(Long engagementId, Long riskId) {
        return riskRepositoryService.getRisk(engagementId, riskId);
    }

    /**
     * Get Risk List
     *
     * @param engagementId
     * @return
     */
    public List<RiskDTO> getRiskList(Long engagementId) {

        if (engagementId != null) {
            return riskRepositoryService.getRisks(engagementId);
        }
        return getRiskList();
    }


    /**
     * Get Risk List
     *
     * @return
     */
    public List<RiskDTO> getRiskList() {
        return riskRepositoryService.getRisks();
    }


    /**
     * Get Risk List
     *
     * @return
     */
    public List<RiskDTO> getRiskList(Long engagementId, Long riskId, String projectName, String businessOwner, String infosecResource, Status riskStatus) {

        if (null == engagementId && null == riskId && null == projectName && null == businessOwner && null == infosecResource && null == riskStatus) {
            return getRiskList();
        }
        return riskRepositoryService.getRisks(engagementId, riskId, projectName, businessOwner, infosecResource, riskStatus);
    }


    /**
     * Create or Updates Risks based on Id passed
     *
     * @param engagementId
     * @param riskDTOList
     * @return
     */
    public List<RiskDTO> createUpdateRiskList(Long engagementId, List<RiskDTO> riskDTOList) {

        // fetch already created risk from db
        List<RiskDTO> retrievedRiskList = getRiskList(engagementId);
        // first check if id exists or not - id exists needs to be updated else created
        List<RiskDTO> updateRiskDtoList = riskDTOList.stream().filter(riskDTO -> riskDTO.getRiskId() != null).collect(Collectors.toList());

        // check the status of updated risks to send emails.
        Set<String> toEmailsUpdateRisk = new HashSet<>();
        if (retrievedRiskList != null && updateRiskDtoList != null) {
            retrievedRiskList.forEach(retrievedRisk -> toEmailsUpdateRisk.addAll(updateRiskDtoList.stream()
                    .filter(riskDTO -> retrievedRisk.getRiskId().equals(riskDTO.getRiskId()))
                    .filter(riskDTO -> !retrievedRisk.getRiskStatus().equals(riskDTO.getRiskStatus()))
                    .map(RiskDTO::getRiskOwner)
                    .collect(Collectors.toList())
            ));

        }

        // perform the update
        List<RiskDTO> updatedRiskDtoList = updateRiskList(engagementId, updateRiskDtoList);
        // send emails for updated risks
        if (updateRiskDtoList != null && !updateRiskDtoList.isEmpty()) {
            final String infosecResourceEmail = employeeService.getEmployeeEntityFromId(updateRiskDtoList.get(0).getInfosecResource()).getEmail();
            String emailBody = StringUtils.replace(updatedRiskBody, "{engagementId}", String.valueOf(engagementId));
            toEmailsUpdateRisk.forEach(toEmail -> emailService.sendEmail(updatedRiskSubject, emailBody, fromEmail, Arrays.asList(toEmail, infosecResourceEmail), Arrays.asList(), false));
        }

        // create new risks - id doesn't exist
        List<RiskDTO> createRiskDtoList = riskDTOList.stream().filter(riskDTO -> riskDTO.getRiskId() == null).collect(Collectors.toList());
        List<RiskDTO> createdRiskDtoList = createRiskList(engagementId, createRiskDtoList);

        // send email for newly create risk
        Set<String> toEmailsNewRisk = new HashSet<>();
        if (createRiskDtoList != null && !createRiskDtoList.isEmpty()) {
            EngagementDTO engagementDTO = engagementRepositoryService.getEngagement(engagementId);
            final String requesterEmail = employeeService.getEmployeeEntityFromId(engagementDTO.getRequestedBy()).getEmail();
            createdRiskDtoList.forEach(riskDTO -> toEmailsNewRisk.add(riskDTO.getRiskOwner()));
            String emailBody = StringUtils.replace(newRiskBody, "{engagementId}", String.valueOf(engagementId));
            toEmailsNewRisk.forEach(toEmail -> emailService.sendEmail(newRiskSubject, emailBody, fromEmail, Arrays.asList(toEmail, requesterEmail), Arrays.asList(), false));
        }

        List<RiskDTO> createdUpdateList = new ArrayList<>();
        createdUpdateList.addAll(updatedRiskDtoList);
        createdUpdateList.addAll(createdRiskDtoList);

        return createdUpdateList;
    }

    public ResponseEntity<Map<String, Integer>> countRisks() {
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>:  countRisks() ");
        List<String> all = repository.getAll();
        Map<String, Integer> riskStatusCountMap = new HashMap<>();
        riskStatusCountMap.put("OPEN", 0);
        riskStatusCountMap.put("CLOSED", 0);
        riskStatusCountMap.put("MITIGATED", 0);
        riskStatusCountMap.put("DEVIATION", 0);
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>> countRisks() >> number of risks {}", all.size());
        all.forEach(riskStatus -> {
            if (riskStatusCountMap.containsKey(riskStatus)) {
                riskStatusCountMap.replace(riskStatus.trim(), riskStatusCountMap.get(riskStatus.trim()), riskStatusCountMap.get(riskStatus.trim()) + 1);
            }
        });
        return new ResponseEntity<>(riskStatusCountMap, HttpStatus.OK);
    }

    public List<RisksByTemporal> getRisksByMonthlyBasis() {
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getRisksByMonthlyBasis");
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getRisksByWeeklyBasis >> before getting 'risks' from database");
        List<RiskEntity> risks = this.repository.findAll();
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getRisksByWeeklyBasis >> after getting 'risks' from database");
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getRisksByWeeklyBasis >> variable: number of risks: >> {}", risks.size());
        List<RisksByTemporal> monthlyAnalysis = Arrays.asList(
                new RisksByTemporal("JANUARY", 0, 0, 0, 0),
                new RisksByTemporal("FEBRUARY", 0, 0, 0, 0),
                new RisksByTemporal("MARCH", 0, 0, 0, 0),
                new RisksByTemporal("APRIL", 0, 0, 0, 0),
                new RisksByTemporal("MAY", 0, 0, 0, 0),
                new RisksByTemporal("JUNE", 0, 0, 0, 0),
                new RisksByTemporal("JULY", 0, 0, 0, 0),
                new RisksByTemporal("AUGUST", 0, 0, 0, 0),
                new RisksByTemporal("SEPTEMBER", 0, 0, 0, 0),
                new RisksByTemporal("OCTOBER", 0, 0, 0, 0),
                new RisksByTemporal("NOVEMBER", 0, 0, 0, 0),
                new RisksByTemporal("DECEMBER", 0, 0, 0, 0));
        risks.forEach(risk -> {
            try {
                Month month = risk.getCompletedDatetime().getMonth();
                String riskStatus = risk.getRiskStatus().toString();
                updateRiskCountByMonth(month.toString(), riskStatus, monthlyAnalysis);
            } catch (Exception e) {
                LOG.error(">>>>>>>>>>>>>>>>>>>>>>: method >>  getRisksByMonthlyBasis >> inside 'risks.forEach' loop >> Exception: >> {}, Message: >> {}", e.getClass(), e.getMessage());
            }
        });
        return monthlyAnalysis;
    }

    private int getMonthNumber(String monthName) {
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getMonthNumber, Variable: monthName >>>> : {}", monthName);
        return Month.valueOf(monthName.toUpperCase()).getValue() - 1;
    }

    public List<RisksByTemporal> getRisksByWeeklyBasis() {
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getRisksByWeeklyBasis >> before getting 'risks' from database");
        List<RiskEntity> risks = this.repository.findAll();
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getRisksByWeeklyBasis >> after getting 'risks' from database");
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getRisksByWeeklyBasis >> variable: number of risks: >> {}", risks.size());
        List<RisksByTemporal> weeklyAnalysis = new ArrayList<>(53);
        for (int i = 1; i < 54; i++) {
            weeklyAnalysis.add(new RisksByTemporal(String.valueOf(i), 0, 0, 0, 0));
        }
        risks.forEach(risk -> {
            try {
                int weekOfYear = risk.getCompletedDatetime().get(WeekFields.of(Locale.ROOT).weekOfYear());
                String riskStatus = risk.getRiskStatus().toString();
                updateRiskCountByWeek(String.valueOf(weekOfYear), riskStatus, weeklyAnalysis);
            } catch (Exception e) {
                LOG.error(">>>>>>>>>>>>>>>>>>>>>>: method >>  getRisksByWeeklyBasis >> inside 'risks.forEach' loop >> Exception: >> {}, Message: >> {}", e.getClass(), e.getMessage());
            }
        });
        return weeklyAnalysis;
    }

    void updateRiskCountByWeek(String week, String riskStatus, List<RisksByTemporal> weeklyAnalysis) {
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  updateRiskCountByWeek, Variable: riskStatus >>>> : {}", riskStatus);
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  updateRiskCountByWeek, Variable: weeklyAnalysis >>>> : {}", weeklyAnalysis);
        // -1 , because array lists index starts from 0 and week number starts from 1
        int weekNumber = Integer.parseInt(week) - 1;
        updateRiskCount(riskStatus, weeklyAnalysis, weekNumber);
    }

    void updateRiskCountByMonth(String month, String riskStatus, List<RisksByTemporal> monthlyAnalysis) {
        int monthIndex = getMonthNumber(month);
        updateRiskCount(riskStatus, monthlyAnalysis, monthIndex);
    }

    private void updateRiskCount(String riskStatus, List<RisksByTemporal> temporalAnalysis, int temporalNumber) {
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  updateRiskCount, Variable: riskStatus >>>> : {}", riskStatus);
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  updateRiskCount, Variable: temporalAnalysis >>>> : {}", temporalAnalysis);
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  updateRiskCount, Variable: temporalNumber >>>> : {}", temporalNumber);
        switch (riskStatus) {
            case "OPEN":
                temporalAnalysis.get(temporalNumber).setOpenCount(temporalAnalysis.get(temporalNumber).getOpenCount() + 1);
                break;
            case "CLOSED":
                temporalAnalysis.get(temporalNumber).setClosedAmount(temporalAnalysis.get(temporalNumber).getClosedAmount() + 1);
                break;
            case "MITIGATED":
                temporalAnalysis.get(temporalNumber).setMitigatedCount(temporalAnalysis.get(temporalNumber).getMitigatedCount() + 1);
                break;
            case "DEVIATION":
                temporalAnalysis.get(temporalNumber).setDeviationCount(temporalAnalysis.get(temporalNumber).getDeviationCount() + 1);
                break;
        }
    }


}
