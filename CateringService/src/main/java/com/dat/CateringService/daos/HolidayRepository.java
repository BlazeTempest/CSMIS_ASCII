package com.dat.CateringService.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dat.CateringService.entity.Holidays;

public interface HolidayRepository extends JpaRepository<Holidays, Integer>{

}
