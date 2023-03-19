package com.dat.CateringService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dat.CateringService.entity.Staff;
@Service
public class PasswordServiceImpl implements PasswordService {
	
	@Autowired
    private StaffService staffService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

	@Override
	public boolean  checkPassword(String userId, String password) {
		 Staff staff = staffService.getStaffById(userId);
	        if (staff != null) {
	            return passwordEncoder.matches(password, staff.getPassword());
	        }
	        return false;
	    }

	@Override
	public boolean  updatePassword(String userId, String newPassword) {
		 Staff staff = staffService.getStaffById(userId);
	        if (staff != null) {
	            staff.setPassword(passwordEncoder.encode(newPassword));
	            staffService.addStaff(staff);
	            return true;
	        }
	        return false;
	    }

}
