package com.optus.infosec.configurations;

import com.optus.infosec.api.converter.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.optus.infosec.api.util.ApplicationConstants.DEV;


/**
 * @author SM
 * <p>
 * Bean Configurations
 */
@Configuration
public class BeanConfig {


    @Value("${email.smtp.host.name}")
    private String emailHostName;

    @Value("${spring.profiles.active:}")
    private List<String> profiles;

    /**
     * Create java mail sender bean
     *
     * @return JavaMailSender
     */
    @Bean
    public JavaMailSender javaMailSender() {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(emailHostName);
        if (isDevProfile()) {
            javaMailSender.getJavaMailProperties().setProperty("mail.debug", Boolean.TRUE.toString());
        }
        return javaMailSender;
    }

    /**
     * Checks if it is dev profile
     *
     * @return boolean - is Dev profile
     */
    private boolean isDevProfile() {
        return profiles.contains(DEV);
    }

    /**
     * Factory of all the converters
     *
     * @return ConversionServiceFactory
     */
    @Bean
    public ConversionServiceFactory conversionServiceFactoryBean() {

        Set<Converter> converterSet = new HashSet<>();

        converterSet.add(engagementDtoToEngagementEntity());
        converterSet.add(engagementFormDtoToEngagementEntity());
        converterSet.add(engagementFormCommentsDtoToEngagementFormCommentsEntity());
        converterSet.add(engagementFormTrackingDtoToEngagementFormTrackingEntity());
        converterSet.add(riskDtoToRiskEntity());
        converterSet.add(engagementEntityToEngagementDto());
        converterSet.add(engagementFormEntityToEngagementFormDto());
        converterSet.add(engagementFormCommentsEntityToEngagementFormCommentsDto());
        converterSet.add(engagementFormTrackingEntityToEngagementFormTrackingDto());
        converterSet.add(riskEntityToRiskDto());
        converterSet.add(engagementFormTemplateDtoToEngagementFormTemplateEntity());
        converterSet.add(engagementFormTemplateEntityToEngagementFormTemplateDto());
        converterSet.add(employeeEntityToEmployeeDto());

        ConversionServiceFactory conversionServiceFactory = new ConversionServiceFactory();
        conversionServiceFactory.setConverters(converterSet);

        return conversionServiceFactory;
    }

    /**
     * EngagementDtoToEngagementEntity Bean
     *
     * @return EngagementDtoToEngagementEntity
     */
    @Bean
    public EngagementDtoToEngagementEntity engagementDtoToEngagementEntity() {
        return new EngagementDtoToEngagementEntity();
    }

    /**
     * EngagementFormDtoToEngagementFormEntity Bean
     *
     * @return EngagementFormDtoToEngagementFormEntity
     */
    @Bean
    public EngagementFormDtoToEngagementFormEntity engagementFormDtoToEngagementEntity() {
        return new EngagementFormDtoToEngagementFormEntity();
    }

    /**
     * EngagementFormCommentsDtoToEngagementFormCommentsEntity Bean
     *
     * @return EngagementFormCommentsDtoToEngagementFormCommentsEntity
     */
    @Bean
    public EngagementFormCommentsDtoToEngagementFormCommentsEntity engagementFormCommentsDtoToEngagementFormCommentsEntity() {
        return new EngagementFormCommentsDtoToEngagementFormCommentsEntity();
    }

    /**
     * EngagementFormTrackingDtoToEngagementFormTrackingEntity Bean
     *
     * @return EngagementFormTrackingDtoToEngagementFormTrackingEntity
     */
    @Bean
    public EngagementFormTrackingDtoToEngagementFormTrackingEntity engagementFormTrackingDtoToEngagementFormTrackingEntity() {
        return new EngagementFormTrackingDtoToEngagementFormTrackingEntity();
    }

    /**
     * RiskDtoToRiskEntity Bean
     *
     * @return RiskDtoToRiskEntity
     */
    @Bean
    public RiskDtoToRiskEntity riskDtoToRiskEntity() {
        return new RiskDtoToRiskEntity();
    }


    /**
     * RiskEntityToRiskDto Bean
     *
     * @return RiskEntityToRiskDto
     */
    @Bean
    public RiskEntityToRiskDto riskEntityToRiskDto() {
        return new RiskEntityToRiskDto();
    }

    /**
     * EngagementEntityToEngagementDto Bean
     *
     * @return EngagementEntityToEngagementDto
     */
    @Bean
    public EngagementEntityToEngagementDto engagementEntityToEngagementDto() {
        return new EngagementEntityToEngagementDto();
    }

    /**
     * EngagementFormEntityToEngagementFormDto Bean
     *
     * @return EngagementFormEntityToEngagementFormDto
     */
    @Bean
    public EngagementFormEntityToEngagementFormDto engagementFormEntityToEngagementFormDto() {
        return new EngagementFormEntityToEngagementFormDto();
    }

    /**
     * EngagementFormCommentsEntityToEngagementFormCommentsDto Bean
     *
     * @return EngagementFormCommentsEntityToEngagementFormCommentsDto
     */
    @Bean
    public EngagementFormCommentsEntityToEngagementFormCommentsDto engagementFormCommentsEntityToEngagementFormCommentsDto() {
        return new EngagementFormCommentsEntityToEngagementFormCommentsDto();
    }

    /**
     * EngagementFormTrackingEntityToEngagementFormTrackingDto Bean
     *
     * @return EngagementFormTrackingEntityToEngagementFormTrackingDto
     */
    @Bean
    public EngagementFormTrackingEntityToEngagementFormTrackingDto engagementFormTrackingEntityToEngagementFormTrackingDto() {
        return new EngagementFormTrackingEntityToEngagementFormTrackingDto();
    }


    /**
     * EngagementFormTemplateEntityToEngagementFormTemplateDto Bean
     *
     * @return EngagementFormTemplateEntityToEngagementFormTemplateDto
     */
    @Bean
    public EngagementFormTemplateEntityToEngagementFormTemplateDto engagementFormTemplateEntityToEngagementFormTemplateDto() {
        return new EngagementFormTemplateEntityToEngagementFormTemplateDto();
    }

    /**
     * EngagementFormTemplateDtoToEngagementFormTemplateEntity Bean
     *
     * @return EngagementFormTemplateDtoToEngagementFormTemplateEntity
     */
    @Bean
    public EngagementFormTemplateDtoToEngagementFormTemplateEntity engagementFormTemplateDtoToEngagementFormTemplateEntity() {
        return new EngagementFormTemplateDtoToEngagementFormTemplateEntity();
    }

    /**
     * EmployeeEntityToEmployeeDto Bean
     *
     * @return EmployeeEntityToEmployeeDto
     */
    @Bean
    public EmployeeEntityToEmployeeDto employeeEntityToEmployeeDto() {
        return new EmployeeEntityToEmployeeDto();
    }

}
