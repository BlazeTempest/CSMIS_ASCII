package com.dat.CateringService.daos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dat.CateringService.entity.Registered_list;

public interface RegisteredListRepository extends JpaRepository<Registered_list, Integer> {
	@Query(value="SELECT * FROM registered_list WHERE staffID = :staffID AND dine = true AND dine_date BETWEEN :start AND :end", nativeQuery = true)
	public List<Registered_list> findRegisteredListByStaffID(@Param("staffID")String staffID, @Param("start")LocalDate startDate,  @Param("end")LocalDate endDate);
	public List<Registered_list> findByStaffIDContainsAndNameContainsAndDivisionContainsAndDeptContainsAllIgnoreCase(String id, String name, String division, String dept);
	public List<Registered_list> findByStaffIDContainsAllIgnoreCase(String id);
	public Registered_list findByStaffIDContainsAndDineDateContainsAllIgnoreCase(String staffID, LocalDate date);
	@Query(value = "SELECT * FROM registered_list WHERE dine_date BETWEEN :start AND :end", nativeQuery = true)
	public List<Registered_list> getRegisteredStaffByDate(@Param("start")LocalDate start, @Param("end")LocalDate end);
}
