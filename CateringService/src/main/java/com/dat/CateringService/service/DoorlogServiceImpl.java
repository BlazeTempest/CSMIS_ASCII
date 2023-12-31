package com.dat.CateringService.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.DoorlogRepository;
import com.dat.CateringService.entity.DailyDoorLog;

@Service
public class DoorlogServiceImpl implements DoorlogService {
	@Autowired
	private DoorlogRepository repository;

	@Override
	public void addAll(List<DailyDoorLog> doorlog) {
		repository.saveAll(doorlog);
	}

	@Override
	public List<DailyDoorLog> getAllDoorlog() {
		return repository.findAll();
	}

	@Override
	public void add(DailyDoorLog doorlog) {
		repository.save(doorlog);
	}

	@Override
	public List<DailyDoorLog> getDoorlogByImportedDate(LocalDate today) {
		return repository.getDoorlogByImportedDate(today);
	}

	@Override
	public List<DailyDoorLog> getDoorlogByDineDate(LocalDate start, LocalDate end) {
		return repository.getDoorlogByDineDate(start, end);
	}

	@Override
	public DailyDoorLog getLastInserted() {
		return repository.getLastDoorlog();
	}

	@Override
	public List<String> getStaffIDByDineDate(LocalDate dineDate) {
		return repository.findByDineDate(dineDate);
	}

	@Override
	public List<DailyDoorLog> getByRegisteredAndStaffID(Boolean registered, LocalDate dineDateStart, LocalDate dineDateEnd) {
		return repository.findByRegisteredAndDineDateBetween(registered, dineDateStart, dineDateEnd);
	}

	@Override
	public List<DailyDoorLog> getByAll(Boolean registered, String name, String staffID, String team) {
		return repository.findByRegisteredAndNameContainsAndStaffIDContainsAndTeamContainsAllIgnoreCase(registered, name, staffID, team);
	}

	@Override
	public DailyDoorLog getByIdAndDate(String id, LocalDate date) {
		return repository.findByStaffIDAndDineDate(id, date);
	}

	@Override
	public void deleteAll(List<DailyDoorLog> doorlog) {
		repository.deleteAll(doorlog);
	}
	
	
}
