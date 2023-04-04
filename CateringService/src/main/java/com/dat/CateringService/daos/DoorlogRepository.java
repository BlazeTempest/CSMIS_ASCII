package com.dat.CateringService.daos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dat.CateringService.entity.DailyDoorLog;

public interface DoorlogRepository extends JpaRepository<DailyDoorLog, Integer> {
	@Query(value = "SELECT * FROM daily_doorlog WHERE imported_date = :today", nativeQuery = true)
	public List<DailyDoorLog> getDoorlogByImportedDate(@Param("today")LocalDate today);
	
	@Query(value = "SELECT * FROM daily_doorlog WHERE dine_date BETWEEN :start AND :end", nativeQuery = true)
	public List<DailyDoorLog> getDoorlogByDineDate(@Param("start")LocalDate start, @Param("end")LocalDate end);
	
	@Query(value = "SELECT * FROM daily_doorlog ORDER BY daily_door_id DESC LIMIT 1", nativeQuery = true)
	public DailyDoorLog getLastDoorlog();
	
	@Query(value = "SELECT DISTINCT staffID FROM daily_doorlog WHERE dine_date=:dineDate", nativeQuery = true)
	public List<String> findByDineDate(@Param("dineDate")LocalDate dineDate);
	
	@Query(value = "SELECT * FROM daily_doorlog WHERE registered=:registered AND dine_date BETWEEN :start AND :end", nativeQuery = true)
	public List<DailyDoorLog> findByRegisteredAndDineDateBetween(@Param("registered")Boolean registered, @Param("start")LocalDate dineDateStart, @Param("end")LocalDate dineDateEnd);

	public List<DailyDoorLog> findByRegisteredAndNameContainsAndStaffIDContainsAndTeamContainsAllIgnoreCase(Boolean registered, String name, String staffID, String team);

	public DailyDoorLog findByStaffIDAndDineDate(String staffID, LocalDate dineDate);
}
