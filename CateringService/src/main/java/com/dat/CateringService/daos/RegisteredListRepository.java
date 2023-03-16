package com.dat.CateringService.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dat.CateringService.entity.Registered_list;

public interface RegisteredListRepository extends JpaRepository<Registered_list, Integer> {
	@Query(value="SELECT * FROM registered_list WHERE staffID = :staffID", nativeQuery = true)
	public List<Registered_list> findRegisteredListByStaffID(@Param("staffID")String staffID);
}
