package com.dat.CateringService.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="register_eat")
public class RegisteredEat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="staffID")
	private String staffID;
	
	@Column(name="name")
	private String name;
	
	@Column(name="division")
	private String division;
	
	@Column(name="dept")
	private String dept;
	
	@Column(name="dine_date")
	private LocalDate dineDate;
	
	@Column(name="team")
	private String team;

	public RegisteredEat(String staffID, String name, String division, String dept, LocalDate dineDate, String team) {
		super();
		this.staffID = staffID;
		this.name = name;
		this.division = division;
		this.dept = dept;
		this.dineDate = dineDate;
		this.team = team;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public RegisteredEat(String staffID, String name, String division, String dept, LocalDate dineDate) {
		super();
		this.staffID = staffID;
		this.name = name;
		this.division = division;
		this.dept = dept;
		this.dineDate = dineDate;
	}

	public RegisteredEat(int id, String staffID, String name, String division, String dept, LocalDate dineDate) {
		super();
		this.id = id;
		this.staffID = staffID;
		this.name = name;
		this.division = division;
		this.dept = dept;
		this.dineDate = dineDate;
	}

	public RegisteredEat() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStaffID() {
		return staffID;
	}

	public void setStaffID(String staffID) {
		this.staffID = staffID;
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

	public LocalDate getDineDate() {
		return dineDate;
	}

	public void setDineDate(LocalDate dineDate) {
		this.dineDate = dineDate;
	}
}
