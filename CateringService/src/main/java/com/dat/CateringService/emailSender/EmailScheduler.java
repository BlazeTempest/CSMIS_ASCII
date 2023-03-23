package com.dat.CateringService.emailSender;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.dat.CateringService.entity.Staff;
import org.springframework.stereotype.Service;

import com.dat.CateringService.entity.Staff;


import com.dat.CateringService.service.StaffService;

@Component
public class EmailScheduler {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private StaffService staffService;

	@Scheduled(cron = "0 46 13 ? * MON-FRI") // send email every day at 12pm
	public void sendEmail() {
		List<String> email = staffService.findActiveEmailNoti(true);
//		String email=staff.getEmail();
		System.out.println(email);
//		for (String temp : email) {
//			SimpleMailMessage message = new SimpleMailMessage();
//			message.setTo("mr.bhonemyatp@gmail.com", "zhiendharuki89@gmail.com",
//			"aung6997@gmail.com", "clandestine2002@gmail.com");
//			message.setTo(temp);
//	        String htmlMsg = "<html><body>"
//		            + "<h3>Dear our valuable employee,</h3>"
//		            + "<p>Remember to have lunch today!</p>"
//		            + "</body></html>";
//	        message.setText(htmlMsg);
//
//			String htmlContent = "<h1>Dear our valuable employee,</h1>"
//					+ "<p>Remember to have lunch today!</p>";
//			message.setContent(htmlContent, "text/html; charset=utf-8");
//
//			message.setSubject("Lunch Reminder");
//			mailSender.send(message);
//		}
	
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

