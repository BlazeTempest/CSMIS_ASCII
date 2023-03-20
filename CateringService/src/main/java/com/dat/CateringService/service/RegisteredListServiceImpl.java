package com.dat.CateringService.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.RegisteredListRepository;
import com.dat.CateringService.entity.Registered_list;

@Service
public class RegisteredListServiceImpl implements RegisteredListService {
	@Autowired
	private RegisteredListRepository repository;

	@Override
	public void addAllRegisteredDate(List<Registered_list> registeredList) {
		repository.saveAll(registeredList);
	}

	@Override
	public List<Registered_list> getRegisteredListByStaffID(String staffID, LocalDate startDate, LocalDate endDate) {
		return repository.findRegisteredListByStaffID(staffID, startDate, endDate);
	}

	@Override
	public void addRegisteredDate(Registered_list registered_list) {
		repository.save(registered_list);
	}

	@Override
	public Registered_list getbyStaffIDAndDineDate(String staffID, LocalDate date) {
		return repository.findByStaffIDContainsAndDineDateContainsAllIgnoreCase(staffID, date);
	}

	@Override
	public List<Registered_list> getAllRegisteredStaff() {
		return repository.findAll();
	}

	@Override
	public List<Registered_list> getRegisteredStaffByDate(LocalDate start, LocalDate end) {
		return repository.getRegisteredStaffByDate(start, end);
	}

	@Override
	public List<Registered_list> getByStaffIDAndNameAndDivisionAndDept(String id, String name, String division,
			String dept) {
		return repository.findByStaffIDContainsAndNameContainsAndDivisionContainsAndDeptContainsAllIgnoreCase(id, name, division, dept);
	}

	@Override
	public List<Registered_list> getByStaffID(String id) {
		return repository.findByStaffIDContainsAllIgnoreCase(id);
	}
	
	
}
