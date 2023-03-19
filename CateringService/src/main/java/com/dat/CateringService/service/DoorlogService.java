package com.dat.CateringService.service;

import java.time.LocalDate;
import java.util.List;

import com.dat.CateringService.entity.DailyDoorLog;

public interface DoorlogService {
	public void addAll(List<DailyDoorLog> doorlog);
	public void add(DailyDoorLog doorlog);
	public List<DailyDoorLog> getAllDoorlog();
	public List<DailyDoorLog> getDoorlogByImportedDate(LocalDate today);
}
