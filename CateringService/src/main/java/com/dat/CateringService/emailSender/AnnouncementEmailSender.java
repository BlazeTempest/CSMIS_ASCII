package com.dat.CateringService.emailSender;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
@Component
public class AnnouncementEmailSender {
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendAnnouncementEmail(String gpName, String description) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
		    helper.setTo(gpName);
		    helper.setSubject("New Announcement");
		    helper.setText(description);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(message);
	}
}
