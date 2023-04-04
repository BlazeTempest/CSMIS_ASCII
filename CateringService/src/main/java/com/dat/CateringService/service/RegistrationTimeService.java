package com.dat.CateringService.service;

import com.dat.CateringService.entity.Registration_time;

public interface RegistrationTimeService {
	public void addTime(Registration_time registration_time);
	public Registration_time getRegistration_time(int id);
}
