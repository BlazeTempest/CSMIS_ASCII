package com.dat.CateringService.controllers;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dat.CateringService.entity.DailyDoorLog;
import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.service.DoorlogService;
import com.dat.CateringService.service.HeadcountService;
import com.dat.CateringService.service.PasswordService;
import com.dat.CateringService.service.PriceService;
import com.dat.CateringService.service.RegisteredListService;
import com.dat.CateringService.service.StaffService;

@Controller
public class ProfileController {
	
	@Autowired
	private PasswordService passwordService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private DoorlogService doorlogService;
	
	@Autowired
	private HeadcountService headcountService;
	
	@Autowired
	private PriceService priceService;
	
	@Autowired
	private RegisteredListService registeredService;
	
	@GetMapping("/userProfile")
	public String userProfile(Model model, Authentication authentication) {
		
		if (authentication != null) {
			String role = authentication.getAuthorities().toArray()[0].toString();
			LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
			LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
			int currentAmount = 0;
			int preAmount = 0;
			
			if (role.equals("admin")) { 
				Staff staff = staffService.getStaffById(authentication.getName());
				if (staff != null) {
					for(LocalDate i=LocalDate.now().withDayOfMonth(1); i.isBefore(LocalDate.now().plusDays(1)); i.plusDays(1)) {
						DailyDoorLog log = doorlogService.getByIdAndDate(staff.getStaffID(), i);
						if(log!=null) {
							currentAmount += priceService.findById(headcountService.getHeadcountByDate(i).getPrice()).getStaff_price();
						}
						i = i.plusDays(1);
					}
					
					for(LocalDate i=LocalDate.now().minusMonths(1).withDayOfMonth(1); i.isBefore(LocalDate.now().minusMonths(1).plusDays(1)); i.plusDays(1)) {
						DailyDoorLog log = doorlogService.getByIdAndDate(staff.getStaffID(), i);
						if(log!=null) {
							preAmount += priceService.findById(headcountService.getHeadcountByDate(i).getPrice()).getStaff_price();
						}
						i = i.plusDays(1);
					}
					
					model.addAttribute("totalDay", registeredService.findByDineDateWithStaffID(staff.getStaffID(), firstDayOfMonth, lastDayOfMonth));
					model.addAttribute("preAmount", preAmount);
					model.addAttribute("currentAmount", currentAmount);
					model.addAttribute("month", LocalDate.now().getMonth());
					model.addAttribute("id", staff.getStaffID());
					model.addAttribute("name", staff.getName());
					model.addAttribute("email", staff.getEmail());
					model.addAttribute("team", staff.getTeam());
					model.addAttribute("password", passwordEncoder.encode(staff.getPassword()));
					model.addAttribute("doorLogNo",staff.getDoorLogNo());
					model.addAttribute("division", staff.getDivision());
					model.addAttribute("department", staff.getDept());
					return "admin/profile";
				} else {
					return "404";
				}
			} else if (role.equals("operator")) {
				Staff staff = staffService.getStaffById(authentication.getName());
				if (staff != null) {
					for(LocalDate i=LocalDate.now().withDayOfMonth(1); i.isBefore(LocalDate.now().plusDays(1)); i.plusDays(1)) {
						DailyDoorLog log = doorlogService.getByIdAndDate(staff.getStaffID(), i);
						if(log!=null) {
							currentAmount += priceService.findById(headcountService.getHeadcountByDate(i).getPrice()).getStaff_price();
						}
						i = i.plusDays(1);
					}
					
					for(LocalDate i=LocalDate.now().minusMonths(1).withDayOfMonth(1); i.isBefore(LocalDate.now().minusMonths(1).plusDays(1)); i.plusDays(1)) {
						DailyDoorLog log = doorlogService.getByIdAndDate(staff.getStaffID(), i);
						if(log!=null) {
							preAmount += priceService.findById(headcountService.getHeadcountByDate(i).getPrice()).getStaff_price();
						}
						i = i.plusDays(1);
					}
					
					model.addAttribute("totalDay", registeredService.findByDineDateWithStaffID(staff.getStaffID(), firstDayOfMonth, lastDayOfMonth));
					model.addAttribute("preAmount", preAmount);
					model.addAttribute("currentAmount", currentAmount);
					model.addAttribute("month", LocalDate.now().getMonth());
					model.addAttribute("id", staff.getStaffID());
					model.addAttribute("name", staff.getName());
					model.addAttribute("email", staff.getEmail());
					model.addAttribute("team", staff.getTeam());
					model.addAttribute("password", passwordEncoder.encode(staff.getPassword()));
					model.addAttribute("doorLogNo",staff.getDoorLogNo());
					model.addAttribute("division", staff.getDivision());
					model.addAttribute("department", staff.getDept());
					return "employee/EmployeeProfile";
				} else {
					return "404";
				}
			} else {
				return "redirect:/access-denied";
			}
		} else {
			return "redirect:/showMyLoginPage";
		}
	}
	
	
	@GetMapping("/changePassword")
	public String changePassword(Model model, Authentication authentication,@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword,@RequestParam("confirmPassword") String confirmPassword) {
	
		if (authentication != null) {
			String userId = authentication.getName();
			
			if (passwordService.checkPassword(userId, oldPassword)) {
				if (newPassword.equals(confirmPassword)) {
					if (passwordService.updatePassword(userId, newPassword)) {
						model.addAttribute("successMsg", "Password changed successfully");
					} else {
						model.addAttribute("errorMsg", "Failed to update password");
					}
				} else {
					model.addAttribute("errorMsg", "New password and confirm password do not match");
				}
			} else {
				model.addAttribute("errorMsg", "Invalid old password");
			}
			
			return "redirect:/userProfile";
		} else {
			return "redirect:/showMyLoginPage";
		}
	}

}
