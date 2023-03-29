package com.dat.CateringService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.StaffRepository;
import com.dat.CateringService.entity.Staff;

@Service
public class StaffServiceImpl implements StaffService {
	@Autowired
	private StaffRepository repository;

	@Override
	public void addStaff(Staff staff) {
		repository.save(staff);
	}

	@Override
	public void addAllStaff(List<Staff> staffs) {
		repository.saveAll(staffs);
	}
	
	@Override
	public List<Staff> getAllStaffs() {
		return repository.findAll();
	}

	@Override
	public void deleteById(String id) {
		repository.deleteById(id);
	}

	@Override
	public List<Staff> searchBy(String name, String id, String team) {
		return repository.findByNameContainsAndStaffIDContainsAndTeamContainsAllIgnoreCase(name, id, team);
	}

	@Override
	public List<Staff> filterByDivision(String division) {
		return repository.findByDivisionContainsAllIgnoreCase(division);
	}
	
	@Override
	public List<Staff> filterByDept(String dept) {
		return repository.findByDeptContainsAllIgnoreCase(dept);
	}
	
	@Override
	public List<Staff> filterByStatus(int status) {
		return repository.findByEnabledContains(status);
	}

	@Override
	public List<String> getTeamNames() {
		return repository.findTeamNames();
	}

	@Override
	public List<String> getDivNames() {
		return repository.findDivNames();
	}

	@Override
	public List<String> getDeptNames() {
		return repository.findDeptNames();
	}

	@Override
	public Staff getStaffById(String id) {
		return repository.getById(id);
	}

	@Override
	public List<Staff> getActiveStaffs(int status) {
		return repository.findActiveStaffs(status);
	}

	@Override
	public List<Staff> filterByRole(String role) {
		return repository.findByRoleContainsAllIgnoreCase(role);
	}

	@Override
	public List<String> getEmails() {
		return null;
	}

	@Override
	public List<Staff> getStaffByEmail(String email) {
		return repository.findEmails(email);
	}
	
	@Override
    public List<String> findActiveEmailNoti(boolean email_noti) {
        return repository.findActiveEmailNoti(email_noti);
    }
	
	@Override
	public Staff getByDoorlog(int doorlog) {
		return repository.findByDoorlog(doorlog);
	}

	@Override
	public List<Staff> getAdminTeam() {
		return repository.getAdminTeam();
	}


}
