package com.dat.CateringService.service;

public interface PasswordService {
	
	 public boolean checkPassword(String userId, String password);
	 public boolean updatePassword(String userId, String newPassword);

}
