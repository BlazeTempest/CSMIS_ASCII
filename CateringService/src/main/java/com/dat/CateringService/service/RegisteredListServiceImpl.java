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
		return repository.findByNameAndDate(name, startDate, endDate);
	}

	@Override
	public List<Registered_list> getRegisteredListByIdAndDate(String id, LocalDate startDate, LocalDate endDate) {
		return repository.findByIdAndDate(id, startDate, endDate);
	}

	@Override
	public List<Registered_list> getRegisteredStaffByStatusAndDate(Boolean status, LocalDate start, LocalDate end) {
		return repository.findByStatusAndDineDateBetween(status, start, end);
	}

	@Override
	public List<Registered_list> getRegisteredStaffByStatusAndNameAndID(Boolean status, String name, String id,
			String team) {
		return repository.findByStatusAndNameContainsAndStaffIDContainsAndTeamContainsAllIgnoreCase(status, name, id, team);
	}

	@Override
	public List<Registered_list> getRegisteredStaffByAll(Boolean status, LocalDate start, LocalDate end, String name, String id, String team) {
		return repository.findByStatusAndDineDateBetweenAndNameContainsAndStaffIDContainsAndTeamContainsAllIgnoreCase(status, start, end, name, id, team);
	}

	@Override
	public List<Registered_list> filterByStatusAndDivision(Boolean status, String division) {
		return repository.findByStatusAndDivisionContainsAllIgnoreCase(status, division);
	}

	@Override
	public List<Registered_list> filterByStatusAndDept(Boolean status, String dept) {
		return repository.findByStatusAndDeptContainsAllIgnoreCase(status, dept);
	}

	@Override
	public List<Registered_list> getRegisteredStaffByStatusAndDineAndDate(Boolean status, Boolean dine, LocalDate start,
			LocalDate end) {
		return repository.findByStatusAndDineAndDineDateBetween(status, dine, start, end);
	}

	@Override
	public List<Registered_list> getRegisteredListByTeamAndDate(String team, LocalDate startDate, LocalDate endDate) {
		return repository.findByTeamContainsAndDineDateBetween(team, startDate, endDate);
	}

	@Override
	public List<Registered_list> getRegisteredStaffByDateAfter(LocalDate date) {
		return repository.findByDateAfter(date);
	}

	@Override
	public int getDeptCount(String dept, Boolean dine) {
		return repository.getDeptCount(dept, dine , LocalDate.now());
	}
	
	@Override
	public int getTeamCount(String team, Boolean dine) {
		return repository.getTeamCount(team, dine , LocalDate.now());
	}

	@Override
	public int findByDineDateWithStaffID(String id, LocalDate start, LocalDate end) {
		return repository.findByDineDateWithStaffID(id, start, end);
	}
	
}
