package com.dat.CateringService.service;

import java.time.LocalDate;
import java.util.List;

import com.dat.CateringService.entity.Registered_list;

public interface RegisteredListService {
	public void addRegisteredDate(Registered_list registered_list);
	public void addAllRegisteredDate(List<Registered_list> registeredList);
	public List<Registered_list> getRegisteredListByStaffID(String staffID, LocalDate startDate, LocalDate endDate);
	public List<Registered_list> getByStaffID(String id);
	public Registered_list getbyStaffIDAndDineDate(String staffID, LocalDate date);
	public List<Registered_list> getAllRegisteredStaff();
	public List<Registered_list> getRegisteredStaffByDate(LocalDate start, LocalDate end);
	
}
