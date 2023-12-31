package com.dat.CateringService.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="registered_list")
public class Registered_list {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int reg_ID;
	
	@Column(name="staffID")
	private String staffID;
	
	@Column(name="dine")
	private Boolean dine;
	
	@Column(name="dine_date")
	private LocalDate dineDate;
	
	@Column(name="registered_date")
	private LocalDateTime created_date;
	
	@Column(name="modify_date")
	private LocalDateTime modify_date;
	
	@Column(name="door_log_no")
	private int doorlogno;
	
	@Column(name="name")
	private String name;
	
	@Column(name="division")
	private String division;
	
	@Column(name="dept")
	private String dept;
	
	@Column(name="team")
	private String team;
	
	@Column(name="status")
	private Boolean status;

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Registered_list(String staffID, Boolean dine, LocalDate dineDate, LocalDateTime created_date, int doorlogno,
			String name, String division, String dept, String team) {
		super();
		this.staffID = staffID;
		this.dine = dine;
		this.dineDate = dineDate;
		this.created_date = created_date;
		this.doorlogno = doorlogno;
		this.name = name;
		this.division = division;
		this.dept = dept;
		this.team = team;
	}

	public Registered_list(String staffID, Boolean dine, LocalDate dineDate, LocalDateTime created_date, LocalDateTime modify_date,
			int doorlogno, String name, String division, String dept, String team) {
		super();
		this.staffID = staffID;
		this.dine = dine;
		this.dineDate = dineDate;
		this.created_date = created_date;
		this.modify_date = modify_date;
		this.doorlogno = doorlogno;
		this.name = name;
		this.division = division;
		this.dept = dept;
		this.team = team;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public Registered_list(String staffID, Boolean dine, LocalDate dineDate, LocalDateTime created_date, int doorlogno,
			String name, String division, String dept) {
		super();
		this.staffID = staffID;
		this.dine = dine;
		this.dineDate = dineDate;
		this.created_date = created_date;
		this.doorlogno = doorlogno;
		this.name = name;
		this.division = division;
		this.dept = dept;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public Registered_list(String staffID, Boolean dine, LocalDate dineDate, LocalDateTime created_date, int doorlogno) {
		super();
		this.staffID = staffID;
		this.dine = dine;
		this.dineDate = dineDate;
		this.created_date = created_date;
		this.doorlogno = doorlogno;
	}

	public int getDoorlogno() {
		return doorlogno;
	}

	public void setDoorlogno(int doorlogno) {
		this.doorlogno = doorlogno;
	}

	public Registered_list(String staffID, Boolean dine, LocalDate dineDate, LocalDateTime created_date) {
		super();
		this.staffID = staffID;
		this.dine = dine;
		this.dineDate = dineDate;
		this.created_date = created_date;
	}

	public Registered_list() {
		super();
	}

	public Registered_list(String staff_ID, Boolean dine, LocalDate dineDate) {
		super();
		this.staffID = staff_ID;
		this.dine = dine;
		this.dineDate = dineDate;
	}

	public Registered_list(int reg_ID, String staff_ID, Boolean dine, LocalDate dineDate, LocalDateTime created_date,
			LocalDateTime modify_date) {
		this.reg_ID = reg_ID;
		this.staffID = staff_ID;
		this.dine = dine;
		this.dineDate = dineDate;
		this.created_date = created_date;
		this.modify_date = modify_date;
	}

	public int getReg_ID() {
		return reg_ID;
	}

	public void setReg_ID(int reg_ID) {
		this.reg_ID = reg_ID;
	}

	public String getStaffID() {
		return staffID;
	}

	public void setStaffID(String staff_ID) {
		this.staffID = staff_ID;
	}

	public Boolean getDine() {
		return dine;
	}

	public void setDine(Boolean dine) {
		this.dine = dine;
	}

	public LocalDate getDineDate() {
		return dineDate;
	}

	public void setDineDate(LocalDate dineDate) {
		this.dineDate = dineDate;
	}

	public LocalDateTime getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}

	public LocalDateTime getModify_date() {
		return modify_date;
	}

	public void setModify_date(LocalDateTime modify_date) {
		this.modify_date = modify_date;
	}
}
