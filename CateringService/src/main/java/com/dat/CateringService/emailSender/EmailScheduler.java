package com.dat.CateringService.emailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.service.StaffService;

@Component
public class EmailScheduler {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private StaffService staffService;
    
    @Scheduled(cron = "0 5 20 ? * MON-FRI") // send email every day at 12pm
    public void sendEmail() {
    	Staff staff = staffService.getStaffById("25-00009");
        SimpleMailMessage message = new SimpleMailMessage();
        //message.setTo("mr.bhonemyatp@gmail.com", "zhiendharuki89@gmail.com", "aung6997@gmail.com", "clandestine2002@gmail.com");
        message.setTo(staff.getEmail());
        message.setText("Hi testing 123...");
        message.setSubject("Test Scheduled");
        mailSender.send(message);
    }
}

