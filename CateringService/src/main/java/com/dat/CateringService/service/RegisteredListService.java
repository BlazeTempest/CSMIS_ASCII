package com.dat.CateringService.service;

import java.time.LocalDate;
import java.util.List;

import com.dat.CateringService.entity.Registered_list;
import com.dat.CateringService.entity.Staff;

public interface RegisteredListService {
	public void addRegisteredDate(Registered_list registered_list);
	public void addAllRegisteredDate(List<Registered_list> registeredList);
	public List<Registered_list> getRegisteredListByStaffID(String staffID, LocalDate startDate, LocalDate endDate);
	public List<Registered_list> getRegisteredListByNameAndStaffID(String name, String team, String staffID, LocalDate startDate, LocalDate endDate);
	public List<Registered_list> getRegisteredListByNameAndDate(String name, LocalDate startDate, LocalDate endDate);
	public List<Registered_list> getRegisteredListByIdAndDate(String id, LocalDate startDate, LocalDate endDate);
	public List<Registered_list> getRegisteredListByTeamAndDate(String team, LocalDate startDate, LocalDate endDate);
	public List<Registered_list> searchByNameAndId(String name, String id, String team);
	public List<Registered_list> filterByDivision(String division);
	public List<Registered_list> filterByDept(String dept);
	public List<Registered_list> filterByStatusAndDivision(Boolean status, String division);
	public List<Registered_list> filterByStatusAndDept(Boolean status, String dept);
	public List<Registered_list> getByStaffID(String id);
	public Registered_list getbyStaffIDAndDineDate(String staffID, LocalDate date);
	public List<Registered_list> getAllRegisteredStaff();
	public List<Registered_list> getRegisteredStaffByStartDateAndEndDate(LocalDate start, LocalDate end);
	public List<Registered_list> getRegisteredStaffByDate(LocalDate date);
	public List<Registered_list> getRegisteredStaffByDateAfter(LocalDate date);
	public List<Registered_list> getRegisteredStaffByStatusAndDate(Boolean status, LocalDate start, LocalDate end);
	public List<Registered_list> getRegisteredStaffByStatusAndDineAndDate(Boolean status, Boolean dine, LocalDate start, LocalDate end);
	public List<Registered_list> getRegisteredStaffByStatusAndNameAndID(Boolean status, String name, String id, String team);
	public List<Registered_list> getRegisteredStaffByAll(Boolean status, LocalDate start, LocalDate end, String name, String id, String team);
	public int getDeptCount(String dept, Boolean dine);
	public int getTeamCount(String team, Boolean dine);
	public int findByDineDateWithStaffID(String id, LocalDate start, LocalDate end);
}
