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
	public List<Registered_list> getRegisteredStaffByStartDateAndEndDate(LocalDate start, LocalDate end) {
		return repository.getRegisteredStaffByDate(start, end);
	}

	@Override
	public List<Registered_list> getByStaffID(String id) {
		return repository.findByStaffIDContainsAllIgnoreCase(id);
	}

	@Override
	public List<Registered_list> searchByNameAndId(String name, String id, String team) {
		return repository.findByNameContainsAndStaffIDContainsAndTeamContainsAllIgnoreCase(name, id, team);
	}

	@Override
	public List<Registered_list> filterByDivision(String division) {
		return repository.findByDivisionContainsAllIgnoreCase(division);
	}

	@Override
	public List<Registered_list> filterByDept(String dept) {
		return repository.findByDeptContainsAllIgnoreCase(dept);
	}

	@Override
	public List<Registered_list> getRegisteredListByNameAndDate(String name, LocalDate startDate, LocalDate endDate) {
		return repository.findByNameAndDate(name, startDate, endDate);
	}

	@Override
	public List<Registered_list> getRegisteredStaffByDate(LocalDate date) {
		return repository.findByDate(date);
	}

	@Override
	public List<Registered_list> getRegisteredListByNameAndStaffID(String name, String team, String staffID, LocalDate startDate,
			LocalDate endDate) {
		return repository.findByNameContainsAndStaffIDContainsAndTeamContainsAndDineDateBetween(name, team, staffID, startDate, endDate);
	}

	@Override
	public List<Registered_list> getRegisteredListByIdAndDate(String id, LocalDate startDate, LocalDate endDate) {
		return repository.findByIdAndDate(id, startDate, endDate);
	}
	
	
}
