package com.optus.infosec.api.service;


import au.com.optus.portal.sdk.util.Dates;
import au.com.optus.portal.sdk.util.Strings;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.optus.infosec.api.dto.*;
import com.optus.infosec.api.exception.ServiceException;
import com.optus.infosec.api.service.repository.EngagementRepositoryService;
import com.optus.infosec.api.service.wrapper.FileService;
import com.optus.infosec.domain.entity.EmployeeEntity;
import com.optus.infosec.domain.entity.EngagementEntity;
import com.optus.infosec.domain.enums.EngagementForm;
import com.optus.infosec.domain.enums.Status;
import com.optus.infosec.repositories.EngagementEntityRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author SM
 * <p>
 * Service Class for Engagement
 */
@Service
public class EngagementService {

    private static final Logger LOG = LoggerFactory.getLogger(EngagementService.class);

    @Autowired
    private EngagementRepositoryService engagementRepositoryService;

    @Autowired
    private EngagementFormService engagementFormService;

    @Autowired
    private RiskService riskService;

    @Autowired
    private StorageService storageService;

    @Value("${engagements.file.location}")
    private String engagementsFileLocation;

    @Value("${new.engagement.subject}")
    private String newEngagementSubject;

    @Value("${update.engagement.subject}")
    private String updatedEngagementSubject;

    @Value("${new.engagement.body}")
    private String newEngagementBody;

    @Value("${update.engagement.body}")
    private String updatedEngagementBody;

    @Value("${closed.engagement.body}")
    private String closedEngagementBody;

    @Value("${closed.engagement.subject}")
    private String closedEngagementSubject;

    @Value("${from.email}")
    private String fromEmail;

    @Value("${cancel.engagement.subject}")
    private String cancelledEngagementSubject;

    @Value("${cancel.engagement.body}")
    private String cancelledEngagementBody;

    @Value("${assigned.engagement.subject}")
    private String assignedEngagementSubject;

    @Value("${assigned.engagement.body}")
    private String assignedEngagementBody;


    @Value("${closed.engagement.project.url}")
    private String closedEngagementProjectUrl;

    @Value("${closed.engagement.project.subject}")
    private String closedEngagementProjectSubject;

    @Autowired
    @Qualifier("infosecReadinessApprovalCertificate")
    private ClassPathResource infosecReadinessApprovalCertificateTemplate;

    @Autowired
    private FileService fileService;

    @Autowired
    private EmailService emailService;

    @Value("${infosec.dropbox}")
    private String infosecDropbox;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EngagementEntityRepository repository;

    @Autowired
    private TokenDetailService tokenDetailService;

    private String riskRowTemplate = "<TR>\n" +
            "\t\t\t<TD class=\"tr0 td0\"></TD>\n" +
            "\t\t\t<TD class=\"tr0 td39\"><P class=\"p35 ft1\">{riskName}</P></TD>\n" +
            "\t\t\t<TD class=\"tr0 td40\"><P class=\"p30 ft1\">{riskStatus}</P></TD>\n" +
            "\t\t\t<TD class=\"tr0 td40\"><P class=\"p30 ft1\">{mitigationDate}</P></TD>\n" +
            "\t\t</TR>";

    /**
     * Creates a new engagement
     *
     * @param engagementDTO
     */
    public EngagementDTO createEngagement(EngagementDTO engagementDTO) {
        return engagementRepositoryService.createEngagement(engagementDTO);
    }

    /**
     * Get Engagement
     *
     * @param engagementId
     * @return EngagementEntity
     */
    public EngagementDTO getEngagement(Long engagementId) {
        return engagementRepositoryService.getEngagement(engagementId);
    }

    /**
     * Get Engagement
     *
     * @return EngagementEntity
     */
    public List<EngagementDTO> getEngagements(String requestedBy, Long engagementId, String projectName, String infosecResource, Status engagementStatus, String emailId) {

        if (null == requestedBy && null == engagementId && null == projectName && null == infosecResource && null == engagementStatus && null == emailId) {
            return engagementRepositoryService.getEngagements();
        }
        // this is the case for the business owner
        if (null != emailId) {
            return engagementRepositoryService.getEngagementsWithRiskOwnerEmailId(emailId);
        }
        // other cases
        return engagementRepositoryService.getEngagementsWithParams(requestedBy,
                engagementId,
                projectName,
                infosecResource,
                engagementStatus
        );
    }

    /**
     * Get Engagement
     *
     * @param engagementId
     * @return EngagementEntity
     */
    public void updateEngagement(Long engagementId, EngagementDTO engagementDTO) throws Exception {

        // get a reference to the existing db object
        EngagementDTO retrievedEngagementDTO = engagementRepositoryService.getEngagement(engagementId);

        // email
        String emailSubject = null;
        String emailBody = null;
        boolean sendEmail = false;
        boolean isHtml = false;
        List<String> toEmailList = new ArrayList<>();
        if (!retrievedEngagementDTO.getEngagementStatus().equals(engagementDTO.getEngagementStatus())) {
            sendEmail = true;
            if (Status.NEW.equals(engagementDTO.getEngagementStatus())) {
                emailSubject = newEngagementSubject;
                emailBody = newEngagementBody;
                toEmailList.add(infosecDropbox);
            } else if (Status.CLOSED.equals(engagementDTO.getEngagementStatus())) {
                emailSubject = closedEngagementSubject;
                emailBody = closedEngagementBody;
                //generateAndSendEngagementCertificate(engagementId);
            } else if (Status.CANCELLED.equals(engagementDTO.getEngagementStatus())) {
                emailSubject = cancelledEngagementSubject;
                emailBody = cancelledEngagementBody;
                toEmailList.add(this.employeeService.getEmailFromEngagementDto(engagementDTO));
            } else if (Status.OPEN.equals(engagementDTO.getEngagementStatus())) {
                emailSubject = assignedEngagementSubject;
                emailBody = assignedEngagementBody;
                // assigned to is the employee ID

                EmployeeEntity employeeEntity = employeeService.getEmployeeEntityFromId(engagementDTO.getAssignedTo());
                String infosecResource = employeeEntity.getFirstName() + " " + employeeEntity.getLastName();
                emailBody = StringUtils.replace(emailBody, "{infosecResource}", infosecResource);
                toEmailList.add(this.employeeService.getEmailFromEngagementDto(engagementDTO));
            } else {
                emailSubject = updatedEngagementSubject;
                emailBody = updatedEngagementBody;
                if (engagementDTO.getAssignedTo() != null) {
                    toEmailList.add(this.employeeService.getEmailFromEngagementDto(engagementDTO));
                }
            }
            toEmailList.add(this.employeeService.getEmailFromEngagementDto(engagementDTO));
        }

        // update engagement
        engagementRepositoryService.updateEngagement(engagementId, engagementDTO);
        // after updating send email notification if the status of engagement changes
        if (sendEmail) {
            emailBody = StringUtils.replace(emailBody, "{engagementId}", String.valueOf(engagementId));
            emailSubject = StringUtils.replace(emailSubject, "{projectName}", engagementDTO.getProjectName());
            emailSubject = StringUtils.replace(emailSubject, "{engagementId}", String.valueOf(engagementId));
            EmployeeEntity employeeEntity = employeeService.getEmployeeEntityFromId(engagementDTO.getRequestedBy());
            String requesterName = employeeEntity.getFirstName() + " " + employeeEntity.getLastName();
            emailBody = StringUtils.replace(emailBody, "{requesterName}", requesterName);
            emailService.sendEmail(emailSubject, emailBody, fromEmail, toEmailList, Arrays.asList(), isHtml);
        }
    }

    /**
     * @param engagementId
     * @return
     */
    public byte[] downloadEngagementFilesAsZip(Long engagementId) {
        return fileService.downloadAsZip(Paths.get(engagementsFileLocation, String.valueOf(engagementId)));
    }

    /**
     * @return
     */
    public void generateAndSendEngagementCertificate(Long engagementId) throws ServiceException {

        String emailBody;
        JsonNode jsonNode;

        EngagementFormDTO engagementFormDTO = engagementFormService.getEngagementForm(engagementId, EngagementForm.SUMMARY);

        try {
            emailBody = FileUtils.readFileToString(infosecReadinessApprovalCertificateTemplate.getFile());
            jsonNode = new ObjectMapper().readTree(engagementFormDTO.getFormData());
        } catch (IOException e) {
            LOG.error(e.getMessage());
            throw new ServiceException("Error While Generating Certificate");
        }

        if (jsonNode == null) {
            LOG.error("Summary Engagement Form Data Is Null");
            throw new ServiceException("Error While Generating Certificate");
        }

        EngagementDTO engagementDTO = getEngagement(engagementId);

        // other info
        String s0Radio = jsonNode.get("S0Radio") != null ? jsonNode.get("S0Radio").textValue() : "";

        String projectName = engagementDTO.getProjectName();
        String engagementDate = engagementDTO.getRequestedDatetime() != null ? DateTimeFormatter.ofPattern("dd/MM/yyyy").format(engagementDTO.getRequestedDatetime()) : "";
        EmployeeEntity employeeEntity = engagementDTO.getRequestedBy() != null ? employeeService.getEmployeeEntityFromId(engagementDTO.getRequestedBy()) : null;
        String requesterName = employeeEntity != null ? employeeEntity.getFirstName() + " " + employeeEntity.getLastName() : "";
        EmployeeEntity infosecEmployeeEntity = engagementDTO.getAssignedTo() != null ? employeeService.getEmployeeEntityFromId(engagementDTO.getAssignedTo()) : null;
        String infoSecResource = infosecEmployeeEntity != null ? infosecEmployeeEntity.getFirstName() + " " + infosecEmployeeEntity.getLastName() : "";
        String lineOfBusiness = jsonNode.get("lineOfBusiness") != null ? jsonNode.get("lineOfBusiness").textValue() : "";
        String cRIMSClarityCode = jsonNode.get("code") != null ? jsonNode.get("code").textValue() : "";
        String sra = closedEngagementProjectUrl;
        String cyberSecurityCategory = jsonNode.get("securityCategory") != null ? jsonNode.get("securityCategory").textValue() : "";
        String rFSDate = jsonNode.get("rfsDate") != null ? jsonNode.get("rfsDate").textValue() : "";
        String description = jsonNode.get("endUserScopeOfSecurity") != null ? jsonNode.get("endUserScopeOfSecurity").textValue() : "";
        String businessCustodian = " ";
        String domainManager = " ";
        String portfolioManager = " ";
        String checkboxvaluea = "";
        String checkboxvalueb = "";
        String checkboxvaluec = "";
        String checkboxvalued = "";
        String checkboxvaluee = "";
        String infoSecResourceEmail = infosecEmployeeEntity != null ? infosecEmployeeEntity.getEmail() : "";
        String certificateDate = Dates.format(new Date(), "yyyy-MM-dd");

        // risk list
        List<RiskDTO> riskDTOList = riskService.getRiskList(engagementId);
        Map<Status, Long> statusCountMap = riskDTOList.stream().map(risk -> risk.getRiskStatus()).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));


        List<String> riskRowsTemplateList = new ArrayList<>();
        List<String> toEmailList = new ArrayList<>();
        riskDTOList.forEach(riskDTO -> {
            if (riskDTO.getRiskStatus() == Status.OPEN || riskDTO.getRiskStatus() == Status.DEVIATION) {
                String riskName = riskDTO.getRiskName();
                String riskStatus = riskDTO.getRiskStatus() != null ? riskDTO.getRiskStatus().toString() : "";
                String mitigationDate = riskDTO.getCompletedDatetime() != null ? Dates.format(Date.from(riskDTO.getCompletedDatetime().atZone(ZoneId.systemDefault()).toInstant()), "yyyy-MM-dd") : "";
                String riskRow = StringUtils.replace(riskRowTemplate, "{riskName}", riskName);
                riskRow = StringUtils.replace(riskRow, "{riskStatus}", riskStatus);
                riskRow = StringUtils.replace(riskRow, "{mitigationDate}", mitigationDate);
                riskRowsTemplateList.add(riskRow);
            }
        });

        if (riskRowsTemplateList.size() == 0) {
            String riskRow = StringUtils.replace(riskRowTemplate, "{riskName}", "");
            riskRow = StringUtils.replace(riskRow, "{riskStatus}", "");
            riskRow = StringUtils.replace(riskRow, "{mitigationDate}", "");
            riskRowsTemplateList.add(riskRow);
        }

        String riskRowsTemplate = Strings.buildString(false, "", riskRowsTemplateList.toArray(new String[riskRowsTemplateList.size()]));
        if ("NEW".equalsIgnoreCase(s0Radio) && (statusCountMap.get(Status.CLOSED) != null && statusCountMap.get(Status.CLOSED) == statusCountMap.size())) {
            checkboxvaluea = " checked=\"checked\"";
        } else if ("NEW".equalsIgnoreCase(s0Radio) && ((statusCountMap.get(Status.OPEN) != null && statusCountMap.get(Status.OPEN) > 0) || (statusCountMap.get(Status.DEVIATION) != null && statusCountMap.get(Status.DEVIATION) > 0))) {
            checkboxvalueb = " checked=\"checked\"";
        } else if ("Existing".equalsIgnoreCase(s0Radio) && (statusCountMap.get(Status.CLOSED) != null && statusCountMap.get(Status.CLOSED) == statusCountMap.size())) {
            checkboxvaluec = " checked=\"checked\"";
        } else if ("Existing".equalsIgnoreCase(s0Radio) && ((statusCountMap.get(Status.OPEN) != null && statusCountMap.get(Status.OPEN) > 0) || (statusCountMap.get(Status.DEVIATION) != null && statusCountMap.get(Status.DEVIATION) > 0))) {
            checkboxvalued = " checked=\"checked\"";
        } else {
            checkboxvaluee = " checked=\"checked\"";
        }

        emailBody = StringUtils.replace(emailBody, "{projectName}", projectName);
        emailBody = StringUtils.replace(emailBody, "{engagementDate}", engagementDate);
        emailBody = StringUtils.replace(emailBody, "{requesterName}", requesterName);
        emailBody = StringUtils.replace(emailBody, "{infoSecResource}", infoSecResource);
        emailBody = StringUtils.replace(emailBody, "{lineOfBusiness}", lineOfBusiness);
        emailBody = StringUtils.replace(emailBody, "{cRIMSClarityCode}", cRIMSClarityCode);
        emailBody = StringUtils.replace(emailBody, "{sra}", sra);
        emailBody = StringUtils.replace(emailBody, "{cyberSecurityCategory}", cyberSecurityCategory);
        emailBody = StringUtils.replace(emailBody, "{rFSDate}", rFSDate);
        emailBody = StringUtils.replace(emailBody, "{description}", description);
        emailBody = StringUtils.replace(emailBody, "{businessCustodian}", businessCustodian);
        emailBody = StringUtils.replace(emailBody, "{domainManager}", domainManager);
        emailBody = StringUtils.replace(emailBody, "{portfolioManager}", portfolioManager);
        emailBody = StringUtils.replace(emailBody, "{checkboxvaluea}", checkboxvaluea);
        emailBody = StringUtils.replace(emailBody, "{checkboxvalueb}", checkboxvalueb);
        emailBody = StringUtils.replace(emailBody, "{checkboxvaluec}", checkboxvaluec);
        emailBody = StringUtils.replace(emailBody, "{checkboxvalued}", checkboxvalued);
        emailBody = StringUtils.replace(emailBody, "{checkboxvaluee}", checkboxvaluee);

        emailBody = StringUtils.replace(emailBody, "{infoSecResourceEmail}", infoSecResourceEmail);
        emailBody = StringUtils.replace(emailBody, "{certificateDate}", certificateDate);

        emailBody = StringUtils.replace(emailBody, "{engagementId}", String.valueOf(engagementId));

        emailBody = StringUtils.replace(emailBody, "{riskRows}", riskRowsTemplate);
        String emailSubject = closedEngagementProjectSubject;
        emailSubject = StringUtils.replace(emailSubject, "{projectName}", engagementDTO.getProjectName());
        emailSubject = StringUtils.replace(emailSubject, "{engagementId}", String.valueOf(engagementId));

        toEmailList.add(employeeEntity.getEmail());

        List<String> ccEmailList = Arrays.asList(infosecDropbox);

        emailService.sendEmail(emailSubject, emailBody, fromEmail, toEmailList, ccEmailList, true);
        LOG.info("Infosec Certificate for Engagement : {} Sent", engagementId);
    }

    public ResponseEntity<Map<String, Integer>> countEngagements() {

        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>:  countEngagements() ");
        List<EngagementEntity> all = repository.findAll();
        Map<String, Integer> engagementStatusCountMap = new HashMap<>();
        engagementStatusCountMap.put("NEW", 0);
        engagementStatusCountMap.put("OPEN", 0);
        engagementStatusCountMap.put("CLOSED", 0);
        engagementStatusCountMap.put("CANCELLED", 0);
        engagementStatusCountMap.put("ON_HOLD", 0);
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>> countEngagements() >> number of engagements {}", all.size());

        getEngagementEntitiesBasedOnUser()
                .forEach(engagement -> {
                    String engagementStatus = engagement.getEngagementStatus().toString();
                    if (engagementStatusCountMap.containsKey(engagementStatus)) {
                        engagementStatusCountMap.replace(engagementStatus, engagementStatusCountMap.get(engagementStatus), engagementStatusCountMap.get(engagementStatus) + 1);
                    }
                });

        return new ResponseEntity<>(engagementStatusCountMap, HttpStatus.OK);
    }

    public List<EngagementsByTemporal> getEngagementsByMonthlyBasis() {
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getEngagementsByMonthlyBasis");

        List<EngagementsByTemporal> monthlyAnalysis = Arrays.asList(
                new EngagementsByTemporal("JANUARY", 0, 0, 0, 0, 0),
                new EngagementsByTemporal("FEBRUARY", 0, 0, 0, 0, 0),
                new EngagementsByTemporal("MARCH", 0, 0, 0, 0, 0),
                new EngagementsByTemporal("APRIL", 0, 0, 0, 0, 0),
                new EngagementsByTemporal("MAY", 0, 0, 0, 0, 0),
                new EngagementsByTemporal("JUNE", 0, 0, 0, 0, 0),
                new EngagementsByTemporal("JULY", 0, 0, 0, 0, 0),
                new EngagementsByTemporal("AUGUST", 0, 0, 0, 0, 0),
                new EngagementsByTemporal("SEPTEMBER", 0, 0, 0, 0, 0),
                new EngagementsByTemporal("OCTOBER", 0, 0, 0, 0, 0),
                new EngagementsByTemporal("NOVEMBER", 0, 0, 0, 0, 0),
                new EngagementsByTemporal("DECEMBER", 0, 0, 0, 0, 0));

        getEngagementEntitiesBasedOnUser().forEach(engagement -> {
            try {
                Month month = engagement.getCreatedDatetime().getMonth();
                LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getEngagementsByMonthlyBasis, Variable: month, value: {}", month);
                String riskStatus = engagement.getEngagementStatus().toString();
                LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getEngagementsByMonthlyBasis, Variable: riskStatus, value: {}", riskStatus);
                updateEngagementCountByMonth(month.toString(), riskStatus, monthlyAnalysis);
            } catch (Exception e) {
                LOG.error("error with data engagement >> {},", engagement);
                LOG.error("getEngagementsByMonthlyBasis() >> engagement  {}", e.getClass());
            }
        });
        return monthlyAnalysis;
    }

    public List<EngagementsByTemporal> getEngagementsByWeeklyBasis() {
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getEngagementsByWeeklyBasis");
        List<EngagementsByTemporal> weeklyAnalysis = new ArrayList<>(53);

        for (int i = 1; i < 54; i++) {
            weeklyAnalysis.add(new EngagementsByTemporal(String.valueOf(i), 0, 0, 0, 0, 0));
        }

        getEngagementEntitiesBasedOnUser().forEach(engagement -> {
            try {
                int weekOfYear = engagement.getCreatedDatetime().get(WeekFields.of(Locale.ROOT).weekOfYear());
                LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getEngagementsByWeeklyBasis, Variable: weekOfYear, value: {}", weekOfYear);
                String riskStatus = engagement.getEngagementStatus().toString();
                LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getEngagementsByWeeklyBasis, Variable: riskStatus, value: {}", riskStatus);
                updateEngagementCountByWeek(String.valueOf(weekOfYear), riskStatus, weeklyAnalysis);
            } catch (Exception e) {
                LOG.error(">>>>>>>>>>>>>>>>>>>>>>: method >>  getEngagementsByWeeklyBasis, Variable: engagement >>>> value : {}", engagement);
                LOG.error(">>>>>>>>>>>>>>>>>>>>>>: method >>  getEngagementsByWeeklyBasis, Exception:  {}", e.getMessage());
            }
        });
        return weeklyAnalysis;
    }

    void updateEngagementCountByWeek(String week, String engagementStatus, List<EngagementsByTemporal> weeklyAnalysis) {
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  updateEngagementCountByWeek, Variable: engagementStatus >>>> : {}", engagementStatus);
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  updateEngagementCountByWeek, Variable: weeklyAnalysis >>>> : {}", weeklyAnalysis);
        // -1 , because array lists index starts from 0 and week number starts from 1
        int weekNumber = Integer.parseInt(week) - 1;
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  updateEngagementCountByWeek, Variable: weekNumber >>>> : {}", weekNumber);
        updateEngagementCount(engagementStatus, weeklyAnalysis, weekNumber);
    }

    void updateEngagementCountByMonth(String month, String riskStatus, List<EngagementsByTemporal> monthlyAnalysis) {
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  updateEngagementCountByMonth, Variable: monthlyAnalysis >>>> : {}", monthlyAnalysis);
        int monthIndex = getMonthNumber(month);
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  updateEngagementCountByMonth, Variable: monthIndex >>>> : {}", monthIndex);
        updateEngagementCount(riskStatus, monthlyAnalysis, monthIndex);
    }

    private int getMonthNumber(String monthName) {
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getMonthNumber, Variable: monthName >>>> : {}", monthName);
        return Month.valueOf(monthName.toUpperCase()).getValue() - 1;
    }

    private void updateEngagementCount(String engagementStatus, List<EngagementsByTemporal> temporalAnalysis, int temporalNumber) {
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  updateEngagementCount, Variable: engagementStatus >>>> value : {}", engagementStatus);
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  updateEngagementCount, Variable: temporalAnalysis >>>> value : {}", temporalAnalysis);
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  updateEngagementCount, Variable: temporalNumber >>>> value : {}", temporalNumber);
        switch (engagementStatus) {
            case "NEW":
                temporalAnalysis.get(temporalNumber).setNewCount(temporalAnalysis.get(temporalNumber).getNewCount() + 1);
                break;
            case "OPEN":
                temporalAnalysis.get(temporalNumber).setOpenedCount(temporalAnalysis.get(temporalNumber).getOpenedCount() + 1);
                break;
            case "CLOSED":
                temporalAnalysis.get(temporalNumber).setCloseCount(temporalAnalysis.get(temporalNumber).getCloseCount() + 1);
                break;
            case "CANCELLED":
                temporalAnalysis.get(temporalNumber).setCanceledCount(temporalAnalysis.get(temporalNumber).getCanceledCount() + 1);
                break;
            case "ON_HOLD":
                temporalAnalysis.get(temporalNumber).setOnHoldCount(temporalAnalysis.get(temporalNumber).getOnHoldCount() + 1);
                break;
            default:
                break;
        }
    }

    /*
    if this is not an infosec user, we filter engagements from logged-in user's employee id to get engagements
    requested by him/her
     */
    List<EngagementEntity> getEngagementEntitiesBasedOnUser() {
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getEngagementEntitiesBasedOnUser");
        List<EngagementEntity> all = this.repository.findAll();
        EmployeeDTO employeeDTO = tokenDetailService.getEmployeeDtoFromToken();
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>: method >>  getEngagementEntitiesBasedOnUser, Variable: employeeDTO >>>> value : {}", employeeDTO);
        String isInfosecUser = employeeDTO.getIsInfosecUser();
        List<EngagementEntity> engagementEntities;
        if (isInfosecUser.equalsIgnoreCase("N")) {
            LOG.info(">>>> Not an infosec user");
            engagementEntities = all
                    .stream()
                    .filter(engagement -> engagement.getRequestedBy().equalsIgnoreCase(employeeDTO.getEmployeeId()))
                    .collect(Collectors.toList());
        } else {
            engagementEntities = new ArrayList<>(all);
        }
        return engagementEntities;
    }


}
