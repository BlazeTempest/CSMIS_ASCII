package com.dat.CateringService.daos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dat.CateringService.entity.RegisteredEat;

public interface RegisteredEatRepository extends JpaRepository<RegisteredEat, Integer> {
	@Query(value = "SELECT * FROM register_eat WHERE dine_date BETWEEN :start AND :end", nativeQuery = true)
	public List<RegisteredEat> findByDate(@Param("start")LocalDate start, @Param("end")LocalDate end);
	
	public List<RegisteredEat> findByDivisionContainsAllIgnoreCase(String division);
	public List<RegisteredEat> findByDeptContainsAllIgnoreCase(String dept);
}
