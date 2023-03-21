package com.dat.CateringService.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.RegisteredEatRepository;
import com.dat.CateringService.entity.RegisteredEat;

@Service
public class RegisteredEatServiceImpl implements RegisteredEatService {
	
	@Autowired
	private RegisteredEatRepository repository;

	@Override
	public void add(RegisteredEat registered) {
		repository.save(registered);
	}

	@Override
	public void addAll(List<RegisteredEat> registered) {
		repository.saveAll(registered);
	}

	@Override
	public List<RegisteredEat> getAllRegisteredEat() {
		return repository.findAll();
	}

	@Override
	public List<RegisteredEat> getEatlistByDate(LocalDate start, LocalDate end) {
		return repository.findByDate(start, end);
	}

	@Override
	public List<RegisteredEat> filterByDiv(String div) {
		return repository.findByDivisionContainsAllIgnoreCase(div);
	}

	@Override
	public List<RegisteredEat> filterByDept(String dept) {
		return repository.findByDeptContainsAllIgnoreCase(dept);
	}

}
