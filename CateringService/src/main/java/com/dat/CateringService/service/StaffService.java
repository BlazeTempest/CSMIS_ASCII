package com.dat.CateringService.service;

import java.util.List;

import com.dat.CateringService.entity.Staff;

public interface StaffService {
	//add and update
	public void addStaff(Staff staff);
	public void addAllStaff(List<Staff> staffs);
	
	//select staffs
	public List<Staff> getAllStaffs();
	public List<Staff> getActiveStaffs(int status);
	public Staff getStaffById(String id);
	public List<Staff> getStaffByEmail(String email);
	public List<String> findActiveEmailNoti(boolean email_noti);
	public Staff getByDoorlog(int doorlog);
	public List<Staff> getByAvoidMeatIds(String id);
	
	//search
	public List<Staff> searchBy(String name, String id, String team);
	
	//filter
	public List<Staff> filterByDivision(String division);
	public List<Staff> filterByDept(String dept);
	public List<Staff> filterByStatus(int status);
	public List<Staff> filterByRole(String role);
	
	//get name of divisions, departments and teams
	public List<String> getTeamNames();
	public List<String> getDivNames();
	public List<String> getDeptNames();
	public List<String> getEmails();
	
	//delete
	public void deleteById(String id);
	
	public List<Staff> getAdminTeam();
}
