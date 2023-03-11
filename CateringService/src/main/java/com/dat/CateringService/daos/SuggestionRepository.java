package com.dat.CateringService.daos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dat.CateringService.entity.Suggestion;

public interface SuggestionRepository extends JpaRepository<Suggestion, Integer> {
	@Query(value="SELECT * FROM suggestion WHERE registered_date >= :start AND registered_date <= :end", nativeQuery = true)
	public List<Suggestion> getByStartAndEndDate(@Param("start")LocalDate start, @Param("end")LocalDate end);
}
