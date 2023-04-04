package com.dat.CateringService.emailSender;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import com.dat.CateringService.service.StaffService;

@Component
public class EmailScheduler {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private StaffService staffService;

	@Scheduled(cron = "0 5 16 ? * MON-FRI") // send email every day at 12pm
	public void sendEmail() {
		List<String> email = staffService.findActiveEmailNoti(true);
		for(String temp : email) {
			if(temp==null) email.remove(email.indexOf(temp));
			System.out.println(temp);
		}
	
		for(String temp : email) {
			MimeMessage message = mailSender.createMimeMessage();
		    MimeMessageHelper helper = new MimeMessageHelper(message);
		    
		    try {
		        helper.setTo(temp);
		        helper.setSubject("Lunch Reminder");
		        String htmlContent = "<p>Dear esteemed employee,<br/>"
		        		+"<br/>Remember to have lunch today!</p>"
		        		+"<p>As a reminder, please do not forget to take a lunch today and prioritize your well-being.</p>"
		        		+"<p>In addition, we want to inform you that you have the option to cancel your scheduled lunch plans for next week. If you need to make any changes, please ensure to do so before 2 pm every Friday.</p>"
		        		+"<p>Thank you for your dedication and hard work.</p>"
		        		+"<br/><br/><br/><br/><br/><br/><br/><br/>"
		        		+"<br/>Best regards,"
		        		+"<br/>DAT Admin Team.";
		        helper.setText(htmlContent, true);
		    } catch (MessagingException e) {
		        e.printStackTrace();
		    }

		    mailSender.send(message);
		}
	}
}

