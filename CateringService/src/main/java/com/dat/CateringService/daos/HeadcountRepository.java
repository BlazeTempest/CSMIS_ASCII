package com.dat.CateringService.daos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dat.CateringService.entity.Headcount;

public interface HeadcountRepository extends JpaRepository<Headcount, Integer> {

}
