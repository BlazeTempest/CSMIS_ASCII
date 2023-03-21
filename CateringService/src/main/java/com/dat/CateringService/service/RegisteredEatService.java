package com.dat.CateringService.service;

import java.time.LocalDate;
import java.util.List;

import com.dat.CateringService.entity.RegisteredEat;

public interface RegisteredEatService {
	public void add(RegisteredEat registered);
	public void addAll(List<RegisteredEat> registered);
	public List<RegisteredEat> getAllRegisteredEat();
	public List<RegisteredEat> getEatlistByDate(LocalDate start, LocalDate end);
	public List<RegisteredEat> filterByDiv(String div);
	public List<RegisteredEat> filterByDept(String dept);
}
