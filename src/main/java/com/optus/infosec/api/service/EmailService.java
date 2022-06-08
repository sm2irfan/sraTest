package com.optus.infosec.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SM
 *
 * Class to Send eMails
 *
 */
@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${send.email}")
    private boolean sendEmailFlag;

    /**
     * Sends email.
     *
     * @param messageBody
     * @param subject
     * @param fromAddress
     * @param toAddressList
     * @param ccAddressList
     */
    public void sendEmail(String subject, String messageBody, String fromAddress, List<String> toAddressList, List<String> ccAddressList, boolean isHtml){

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending Email \n " + "Subject: [{}]\n" + "Message Body: [{}]\n" + "From Address: [{}]\n" + "To Address List: [{}]\n" + "CC Address List: [{}]\n", new Object[] {
                subject, messageBody, fromAddress, toAddressList, ccAddressList});
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            // set body
            messageHelper.setText(messageBody, isHtml);
            // set subject
            messageHelper.setSubject(subject);
            // populate from address
            messageHelper.setFrom(fromAddress);

            toAddressList = toAddressList.stream().filter(email-> (email != null && !email.isEmpty())).collect(Collectors.toList());
            ccAddressList = ccAddressList.stream().filter(email-> (email != null && !email.isEmpty())).collect(Collectors.toList());
            // populate to addresses
            messageHelper.setTo(toAddressList.toArray(new String[ccAddressList.size()]));
            // populate cc addresses
            if(ccAddressList.size()> 0){
                messageHelper.setCc(ccAddressList.toArray(new String[ccAddressList.size()]));
            }
            if(sendEmailFlag) {
                javaMailSender.send(message);
                LOGGER.info("Email Sent Successfully");
            } else {
                LOGGER.info("Send Email Flag Is False");
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            // todo: handle exception
            LOGGER.error("ErrorName In Sending Email");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
