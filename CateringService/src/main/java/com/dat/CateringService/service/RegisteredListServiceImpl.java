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
	public List<Registered_list> getByStaffID(String id) {
		return repository.findByStaffIDContainsAllIgnoreCase(id);
	}

	@Override
	public Registered_list getbyDineDate(LocalDate date) {
		return repository.findByDineDateContainsAllIgnoreCase(date);
	}
	
	
}
