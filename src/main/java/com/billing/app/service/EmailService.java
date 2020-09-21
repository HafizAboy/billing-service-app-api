package com.billing.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JavaMailSender mailSender;

	@Value("#{'${noetic.email.recipients}'.split(',')}")
	private String[] emailRecipients;

	@Value("${noetic.email.subject}")
	private String emailSubject;

	@Value("${noetic.email.text}")
	private String emailText;

	@Value("${spring.mail.from}")
	private String fromEmailAddress;

	public void sendSimpleMessage(String content) {
		logger.info("Sending email to recipients: {}", emailRecipients);
		try {
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setTo(emailRecipients);
			simpleMailMessage.setFrom(fromEmailAddress);
			simpleMailMessage.setSubject(emailSubject);
			simpleMailMessage.setText(emailText + " - " + content);
			mailSender.send(simpleMailMessage);
			logger.info("Sent");
		} catch (MailException ex) {
			logger.error("Sending email to recipients failed!");
			ex.printStackTrace();
			logger.error(ex.getLocalizedMessage(), ex);
		}
	}
}