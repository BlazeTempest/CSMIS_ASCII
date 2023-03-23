package com.dat.CateringService.service;

import java.util.List;

import com.dat.CateringService.entity.Staff;

public interface StaffService {
	//add and update
	public void addStaff(Staff staff);
	
	//select staffs
	public List<Staff> getAllStaffs();
	public List<Staff> getActiveStaffs(int status);
	public Staff getStaffById(String id);
	public Staff getByDoorlog(int doorlog);
	

	
	//search
	public List<Staff> searchBy(String name, String id, String team);
	
	//filter
	public List<Staff> filterByDivision(String division);
	public List<Staff> filterByDept(String dept);
	public List<Staff> filterByStatus(int status);
	public List<Staff> filterByRole(String role);
	public List<Staff> filterByNameAndIdAndDivision(String name, String id, String division);
	public List<Staff> filterByNameAndIdAndDept(String name, String id, String dept);
	
	//get name of divisions, departments and teams
	public List<String> getTeamNames();
	public List<String> getDivNames();
	public List<String> getDeptNames();
	public List<String> getEmails();
	
	//delete
	public void deleteById(String id);

	public void addAllStaff(List<Staff> staffs);
	
	public List<String> findActiveEmailNoti(boolean email_noti);
}
