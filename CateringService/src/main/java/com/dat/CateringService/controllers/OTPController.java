package com.dat.CateringService.controllers;

import com.dat.CateringService.emailSender.OTPSender;
import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.service.PasswordService;
import com.dat.CateringService.service.StaffService;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OTPController {

    private OTPSender otpSender;
    
    @Autowired
	private StaffService staffService;
    
	@Autowired
	private PasswordService passwordService;

    @Autowired
    public OTPController(OTPSender otpSender) {
        this.otpSender = otpSender;
    }


    @PostMapping("/send-otp")
    public String sendOTP(@RequestParam("email") String email, Model model, HttpSession session) throws InvalidKeyException, NoSuchAlgorithmException {
            	
        List<Staff> staffList = staffService.getStaffByEmail(email);
        if (staffList.isEmpty()) {
            // staffId doesn't exist, return an error message
            model.addAttribute("error", "Staff not found in the database");
            return "redirect:/";
        }

        String staffId = null;
        for (Staff staff : staffList) {
            if (staff.getEmail().equals(email)) {
                staffId = staff.getStaffID();
                break;
            }
        }

        if (staffId == null) {
            // no matching staff member was found
            model.addAttribute("error", "Staff not found in the database");
            return "redirect:/";
        }

        session.setAttribute("staffId", staffId);
        System.out.println("====>"+staffId);
            
        String otp = otpSender.generateOTP();
        otpSender.sendOTP(email, otp);
        model.addAttribute(staffId);
        System.out.println("====>"+staffId);
        model.addAttribute("email", email);
        return "redirect:/showMyLoginPage";
    }
    
    @PostMapping("/verify-otp")
    public String verifyOTP(@RequestParam("email") String email, @RequestParam("otp") String otp, Model model) {
    	 if (otpSender.verifyOTP(email, otp)) {
    	        model.addAttribute("email", email);
    	        return "redirect:/showMyLoginPage";
    	    } else {
    	       
    	        model.addAttribute("error", "Invalid OTP");
    	        return "redirect:/";
    	    }
    	}
    
    @PostMapping("/resetPassword")
    public String resetPassword(HttpSession session,
                                 @RequestParam("newPassword") String password,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) {
    	
        String staffId = (String) session.getAttribute("staffId");
        System.out.println(staffService.getStaffById(staffId));
        if (staffId==null) {
        	System.out.println("======>"+staffId);
            model.addAttribute("error", "Staff not found in the database");
            return "redirect:/";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("staff", staffId);
            System.out.println("-------->"+staffId);
            return "redirect:/showMyLoginPage";
        }	

        passwordService.updatePassword(staffId, password);

        model.addAttribute("success", "Password updated successfully");
        return "redirect:/";
    }
    
}