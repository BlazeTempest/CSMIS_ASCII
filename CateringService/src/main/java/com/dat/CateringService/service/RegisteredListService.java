package com.dat.CateringService.service;

import java.util.List;

import com.dat.CateringService.entity.Registered_list;

public interface RegisteredListService {
	public void addAllRegisteredDate(List<Registered_list> registeredList);
	public List<Registered_list> getRegisteredListByStaffID(String staffID);
}
