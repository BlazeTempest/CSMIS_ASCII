package com.dat.CateringService.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="staff")
public class Staff {
	@Id
	private String staffID;
	
	@Column(name="division")
	private String division;
	
	@Column(name="name")
	private String name;
	
	@Column(name="door_log_no")
	private int doorLogNo;
	
	@Column(name="dept")
	private String dept;
	
	@Column(name="team")
	private String team;
	
	@Column(name="email")
	private String email;
	
	@Column(name="email_noti")
	private Byte email_noti;
	
	@Column(name="password")
	private String password;
	
	@Column(name="role")
	private String role;
	
	@Column(name="suggestion_count")
	private int suggestion_count;
	
	@Column(name="created_date")
	private LocalDateTime created_date;
	
	@Column(name="created_by")
	private String created_by;
	
	@Column(name="modify_date")
	private LocalDateTime modify_date;
	
	@Column(name="modify_by")
	private String modify_by;
	
	@Column(name="deleted_date")
	private LocalDateTime delete_date;
	
	@Column(name="deleted_by")
	private String delete_by;
	
	@Column(name="enabled")
	private Byte enabled;
	
	@ManyToMany
    @JoinTable(name = "staff_avoid_meat",
               joinColumns = @JoinColumn(name = "staff_id"),
               inverseJoinColumns = @JoinColumn(name = "avoidmeat_id"))
    private List<Avoidmeat> avoidmeats;
	
	public Staff() {
		super();
	}

	public Staff(String staff_ID, String division, String name, int doorLogNo, String dept, String team,
			String email) {
		super();
		this.staffID = staff_ID;
		this.division = division;
		this.name = name;
		this.doorLogNo = doorLogNo;
		this.dept = dept;
		this.team = team;
		this.email = email;
	}

	public Staff(String staff_ID, String division, String name, int doorLogNo, String dept, String team,
			String email, Byte email_noti, String password, String role, int suggestion_count, LocalDateTime created_date,
			String created_by, LocalDateTime modify_date, String modify_by, LocalDateTime delete_date, String delete_by, Byte enabled,
			List<Avoidmeat> avoidmeats) {
		super();
		this.staffID = staff_ID;
		this.division = division;
		this.name = name;
		this.doorLogNo = doorLogNo;
		this.dept = dept;
		this.team = team;
		this.email = email;
		this.email_noti = email_noti;
		this.password = password;
		this.role = role;
		this.suggestion_count = suggestion_count;
		this.created_date = created_date;
		this.created_by = created_by;
		this.modify_date = modify_date;
		this.modify_by = modify_by;
		this.delete_date = delete_date;
		this.delete_by = delete_by;
		this.enabled = enabled;
		this.avoidmeats = avoidmeats;
	}

	public String getStaffID() {
		return staffID;
	}

	public void setStaffID(String staff_ID) {
		this.staffID = staff_ID;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDoorLogNo() {
		return doorLogNo;
	}

	public void setDoorLogNo(int i) {
		this.doorLogNo = i;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Byte getEmail_noti() {
		return email_noti;
	}

	public void setEmail_noti(Byte email_noti) {
		this.email_noti = email_noti;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getSuggestion_count() {
		return suggestion_count;
	}

	public void setSuggestion_count(int suggestion_count) {
		this.suggestion_count = suggestion_count;
	}

	public LocalDateTime getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public LocalDateTime getModify_date() {
		return modify_date;
	}

	public void setModify_date(LocalDateTime modify_date) {
		this.modify_date = modify_date;
	}

	public String getModify_by() {
		return modify_by;
	}

	public void setModify_by(String modify_by) {
		this.modify_by = modify_by;
	}

	public LocalDateTime getDelete_date() {
		return delete_date;
	}

	public void setDelete_date(LocalDateTime delete_date) {
		this.delete_date = delete_date;
	}

	public String getDelete_by() {
		return delete_by;
	}

	public void setDelete_by(String delete_by) {
		this.delete_by = delete_by;
	}

	public Byte getEnabled() {
		return enabled;
	}

	public void setStatus(Byte status) {
		this.enabled = status;
	}

	public List<Avoidmeat> getAvoidmeats() {
		return avoidmeats;
	}

	public void setAvoidmeats(List<Avoidmeat> avoidmeats) {
		this.avoidmeats = avoidmeats;
	}
}
