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
}
