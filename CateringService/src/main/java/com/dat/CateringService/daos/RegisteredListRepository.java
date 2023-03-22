package com.dat.CateringService.daos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dat.CateringService.entity.Registered_list;
import com.dat.CateringService.entity.Staff;

public interface RegisteredListRepository extends JpaRepository<Registered_list, Integer> {
	@Query(value="SELECT * FROM registered_list WHERE staffID = :staffID AND dine = true AND dine_date BETWEEN :start AND :end", nativeQuery = true)
	public List<Registered_list> findRegisteredListByStaffID(@Param("staffID")String staffID, @Param("start")LocalDate startDate,  @Param("end")LocalDate endDate);
	
	@Query(value="SELECT * FROM registered_list WHERE dine_date = :date", nativeQuery = true)
	public List<Registered_list> findByDate(@Param("date")LocalDate date);
	
	@Query(value = "SELECT * FROM registered_list WHERE dine_date BETWEEN :start AND :end", nativeQuery = true)
	public List<Registered_list> getRegisteredStaffByDate(@Param("start")LocalDate start, @Param("end")LocalDate end);
	
	@Query(value = "SELECT * FROM registered_list WHERE name LIKE %:name% AND dine_date BETWEEN :start AND :end", nativeQuery = true)
	public List<Registered_list> findByNameAndDate(@Param("name")String name, @Param("start")LocalDate startDate,  @Param("end")LocalDate endDate);
	
	@Query(value = "SELECT * FROM registered_list WHERE staffID LIKE %:id% AND dine_date BETWEEN :start AND :end", nativeQuery = true)
	public List<Registered_list> findByIdAndDate(@Param("id")String id, @Param("start")LocalDate startDate,  @Param("end")LocalDate endDate);
	
	public List<Registered_list> findByStatusAndDivisionContainsAllIgnoreCase(Boolean status, String division);
	
	public List<Registered_list> findByDivisionContainsAllIgnoreCase(String division);
	
	public List<Registered_list> findByStatusAndDeptContainsAllIgnoreCase(Boolean status, String dept);
	
	public List<Registered_list> findByDeptContainsAllIgnoreCase(String dept);
	
	public List<Registered_list> findByNameContainsAndStaffIDContainsAndTeamContainsAllIgnoreCase(String name, String id, String team);
	
	public List<Registered_list> findByStaffIDContainsAllIgnoreCase(String id);
	
	public Registered_list findByStaffIDContainsAndDineDateContainsAllIgnoreCase(String staffID, LocalDate date);

	public List<Registered_list> findByStatusAndDineDateBetween(Boolean status, LocalDate dineDateStart, LocalDate dineDateEnd);
	
	@Query(value = "SELECT * FROM registered_list WHERE status=:status AND dine=:dine AND dine_date BETWEEN :start AND :end", nativeQuery = true)
	public List<Registered_list> findByStatusAndDineAndDineDateBetween(@Param("status")Boolean status, @Param("dine")Boolean dine, @Param("start")LocalDate dineDateStart, @Param("end")LocalDate dineDateEnd);
	
	public List<Registered_list> findByStatusAndNameContainsAndStaffIDContainsAndTeamContainsAllIgnoreCase(Boolean status, String name, String id, String team);
	
	public List<Registered_list> findByStatusAndDineDateBetweenAndNameContainsAndStaffIDContainsAndTeamContainsAllIgnoreCase(Boolean status,  LocalDate dineDateStart, LocalDate dineDateEnd, String name, String id, String team);
	
	public List<Registered_list> findByNameContainsAndStaffIDContainsAndTeamContainsAndDineDateBetween(String name, String staffID, String team, LocalDate dineDateStart, LocalDate dineDateEnd);
}
