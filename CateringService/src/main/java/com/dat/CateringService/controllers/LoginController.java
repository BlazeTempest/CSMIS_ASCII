package com.dat.CateringService.controllers;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dat.CateringService.DTO.ReportDTO;
import com.dat.CateringService.daos.PriceRepository;
import com.dat.CateringService.entity.Announcement;
import com.dat.CateringService.entity.AvoidMeat;
import com.dat.CateringService.entity.DailyDoorLog;
import com.dat.CateringService.entity.Headcount;
import com.dat.CateringService.entity.Price;
import com.dat.CateringService.entity.Registered_list;
import com.dat.CateringService.entity.Staff;
import com.dat.CateringService.service.AnnouncementService;
import com.dat.CateringService.service.AvoidMeatService;
import com.dat.CateringService.service.DoorlogService;
import com.dat.CateringService.service.HeadcountService;
import com.dat.CateringService.service.MenuPdfService;
import com.dat.CateringService.service.PriceService;
import com.dat.CateringService.service.RegisteredListService;
import com.dat.CateringService.service.StaffService;

@Controller
public class LoginController {
	@Autowired
	private PriceService priceService;
	
	@Autowired
	private MenuPdfService menuPdfService;
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private AnnouncementService announcementService;
	
	@Autowired
	private RegisteredListService registeredService;
	
	@Autowired
	private AvoidMeatService avoidMeatService;
	
	@Autowired
	private DoorlogService doorService;
	
	@Autowired
	private DoorlogService doorlogService;
	
	@Autowired
	private HeadcountService headcountService;

	@GetMapping("/showMyLoginPage")
	public String showMyLoginPage() {

		return "signin";

	}

	@GetMapping("/access-denied")
	public String showAccessDenied() {

		return "access-denied";

	}

	@GetMapping("/dashboard")
	public String showDashboard(Model theModel, Authentication authentication) throws IOException {
		try {
			List<Registered_list> registeredStaffs = registeredService.getRegisteredStaffByDate(LocalDate.now());
			Headcount tempHeadcount = headcountService.getHeadcountByDate(LocalDate.now());
			Price price = priceService.findActivePrice();
			int actual = doorlogService.getStaffIDByDineDate(LocalDate.now()).size();
			int registeredCount = registeredStaffs.size();
			int amount = 0;
			
			// Update the headcount table with the total registered count
			if(tempHeadcount==null) {
			    Headcount headcount = new Headcount();
				headcount.setRegisteredCount(registeredCount);
				headcount.setActualCount(actual);
				headcount.setInvoiceDate(LocalDate.now());
				headcount.setDifference(registeredStaffs.size() - doorlogService.getStaffIDByDineDate(LocalDate.now()).size());
				headcount.setPrice(price.getPrice_ID());
				if(actual>registeredCount) {
					headcount.setAmount(amount * price.getTotal_price());
				}else {
					headcount.setAmount(amount * price.getTotal_price());
				}
				headcountService.saveHeadcount(headcount);
			}else {
				tempHeadcount.setRegisteredCount(registeredStaffs.size());
				tempHeadcount.setActualCount(doorlogService.getStaffIDByDineDate(LocalDate.now()).size());
				tempHeadcount.setDifference(registeredStaffs.size() - doorlogService.getStaffIDByDineDate(LocalDate.now()).size());
				tempHeadcount.setPrice(price.getPrice_ID());
				if(actual>registeredCount) {
					tempHeadcount.setAmount(amount * price.getTotal_price());
				}else {
					tempHeadcount.setAmount(amount * price.getTotal_price());
				}
				headcountService.saveHeadcount(tempHeadcount);
			}
			String role = authentication.getAuthorities().toArray()[0].toString();
			if (role.equals("admin")) {
				List<Announcement> announcements = announcementService.orderByCreatedDate();
				for(Announcement temp : announcements) {
					if(temp.getDeleted_date().equals(LocalDate.now())) {
						announcementService.delete(temp);
					}
				}
				theModel.addAttribute("announcement", new Announcement());
				//dashboard overview counts
				List<String> teams = staffService.getTeamNames();
				List<String> depts = staffService.getDeptNames();
				List<Staff> staffs = new ArrayList<>();
				List<ReportDTO> deptCounts = new ArrayList<>();
				List<ReportDTO> teamCounts = new ArrayList<>();
				List<Staff> admin = new ArrayList<>();
				
				for(Staff temp : staffService.getActiveStaffs(1)) {
					staffs.add(temp);
					if(temp.getRole().equals("admin") && !temp.getName().equals("Admin")) {
						admin.add(temp);
					}
				}
				
				for(String dept : depts) {
					ReportDTO dto = new ReportDTO(registeredService.getDeptCount(dept, true), registeredService.getDeptCount(dept, false));
					dto.setDept(dept);
					if(!(registeredService.getDeptCount(dept, true)==0 && registeredService.getDeptCount(dept, false)==0)) {
						deptCounts.add(dto);
					}
				}
				
				for(String team : teams) {
					ReportDTO dto = new ReportDTO(registeredService.getTeamCount(team, true), registeredService.getTeamCount(team, false));
					dto.setTeam(team);
					if(!(registeredService.getTeamCount(team, true)==0 && registeredService.getTeamCount(team, false)==0)) {
						teamCounts.add(dto);
					}
				}
				Price activePrice = priceService.findActivePrice();
				Byte status = activePrice.getStatus();
				if (status != null || status.equals((byte)1)) {
					// perform actions when status is equal to myByteObject
					theModel.addAttribute("totalPrice", activePrice.getTotal_price());
					theModel.addAttribute("datPrice", activePrice.getDATprice());
					theModel.addAttribute("staffPrice", activePrice.getStaff_price());
				}
				List<DailyDoorLog> doorlogs = doorService.getDoorlogByDineDate(LocalDate.now(), LocalDate.now());
				List<DailyDoorLog> unregisteredComplete = new ArrayList<>();
				for(DailyDoorLog temp : doorlogs) {
					if(temp.getRegistered()==false) {
						unregisteredComplete.add(temp);
					}
				}
				
				//For graph
				List<Registered_list> registered = registeredService.getRegisteredStaffByDate(LocalDate.now());
				List<String> registeredIds = new ArrayList<>();
				for(Registered_list temp:registered) {
					registeredIds.add(temp.getStaffID());
				}
				List<AvoidMeat> avoidMeats = avoidMeatService.findAll();
				String meatTypes = "";
				String staffCounts = "";
				for(AvoidMeat meat : avoidMeats) {
					List<Staff> tempStaffs = staffService.getByAvoidMeatIds(String.valueOf(meat.getAvoidmeat_ID()));
					List<Staff> toRemove = new ArrayList<>();
					for(Staff staff:tempStaffs) {
						if(!registeredIds.contains(staff.getStaffID())) {
							toRemove.add(staff);
						}
					}
					tempStaffs.removeAll(toRemove);
					if(staffCounts=="") {
						staffCounts = String.valueOf(tempStaffs.size());
					}else {
						staffCounts = staffCounts + "," + tempStaffs.size();
					}
					
					if(meatTypes=="") {
						meatTypes = meat.getType();
					} else {
						meatTypes = meatTypes + "," + meat.getType();
					}
				}
				
				
				String countGraph = "";
				countGraph = registeredService.getRegisteredStaffByDate(LocalDate.now()).size() + "," + registeredService.getRegisteredStaffByStatusAndDineAndDate(true, true, LocalDate.now(), LocalDate.now()).size() + "," + registeredService.getRegisteredStaffByStatusAndDineAndDate(false, true, LocalDate.now(), LocalDate.now()).size() + "," + unregisteredComplete.size();
				
				theModel.addAttribute("noti", staffService.getStaffById(authentication.getName()).getEmail_noti());
				theModel.addAttribute("meatTypes", meatTypes);
				theModel.addAttribute("staffCounts", staffCounts);
				theModel.addAttribute("countGraph", countGraph);
				theModel.addAttribute("completedCount", registeredService.getRegisteredStaffByStatusAndDineAndDate(true, true, LocalDate.now(), LocalDate.now()).size());
				theModel.addAttribute("skippedCount", registeredService.getRegisteredStaffByStatusAndDineAndDate(false, true, LocalDate.now(), LocalDate.now()).size());
				theModel.addAttribute("unregisteredComplete", unregisteredComplete.size());
				theModel.addAttribute("registeredCount", registeredService.getRegisteredStaffByDate(LocalDate.now()).size());
				theModel.addAttribute("name", staffService.getStaffById(authentication.getName()).getName());
				theModel.addAttribute("announcements", announcements);
				theModel.addAttribute("admins", admin);
				theModel.addAttribute("teamCounts", teamCounts);
				theModel.addAttribute("deptCounts", deptCounts);
				theModel.addAttribute("totalTeam", teams.size());
				theModel.addAttribute("totalDept", depts.size());
				theModel.addAttribute("TeamNames", teams.size());
				theModel.addAttribute("DeptNames", depts.size());
				theModel.addAttribute("totalStaff", staffs.size());
				return "admin/adminDashboard";

			} else if (role.equals("operator")) {

				Staff staff = staffService.getStaffById(authentication.getName());
				theModel.addAttribute("name", staff.getName());
				theModel.addAttribute("noti", staff.getEmail_noti());
				List<Announcement> announcements = announcementService.orderByCreatedDate();
				theModel.addAttribute("announcements", announcements);
				
					String pdfFileName = "currentweek.pdf";
					System.out.println(pdfFileName);
					if (pdfFileName != null) {

						String encodedPdf = menuPdfService.getPdfAsByteString(pdfFileName);
						theModel.addAttribute("pdf", encodedPdf);

					}

					String pdfFileName2 = "nextweek.pdf";
					if (pdfFileName2 != null) {

						String encodedPdf1 = menuPdfService.getPdfAsByteString(pdfFileName2);
						theModel.addAttribute("pdf1", encodedPdf1);
					}
					Price activePrice = priceService.findActivePrice();
					Byte status = activePrice.getStatus();
					if (status != null || status.equals((byte)1)) {
						
						// perform actions when status is equal to myByteObject
						theModel.addAttribute("totalPrice", activePrice.getTotal_price());
						theModel.addAttribute("datPrice", activePrice.getDATprice());
						theModel.addAttribute("staffPrice", activePrice.getStaff_price());
					}
				return "employee/employeeDashboard";
			} else {

				return "redirect:/access-denied";
			}
		} catch (NullPointerException e) {
			return "redirect:/showMyLoginPage";

		}

	}
	
	@RequestMapping("/toggle-switch")
	@ResponseBody
	public void handleToggleSwitch(@RequestParam("isChecked") boolean isChecked ,Authentication authentication) {

		String staffId=authentication.getName();
		// JDBC or an ORM to save the value of the toggle switch to the database
		// Example using JDBC:
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/csmis_service", "csmisadmin", "csmisdat")) {
			
			String sql = "UPDATE staff SET email_noti = ? WHERE staffID=?";
			PreparedStatement statement = conn.prepareStatement(sql);
			 statement.setBoolean(1, isChecked);
			 statement.setString(2, staffId);
			 statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
