package com.dat.CateringService.daos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dat.CateringService.entity.Registration_time;

public interface RegistrationTimeRepository extends JpaRepository<Registration_time, Integer> {

}
