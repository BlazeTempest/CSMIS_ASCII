package com.dat.CateringService.DTO;

import java.time.LocalDate;

public class ReportDTO {
	private String staffID;
	private LocalDate dineDate;
	private int doorlogno;
	private String name;
	private String division;
	private String dept;
	private String team;
	public ReportDTO() {
		super();
	}
	public ReportDTO(String staffID, LocalDate dineDate, int doorlogno, String name, String division, String dept,
			String team) {
		super();
		this.staffID = staffID;
		this.dineDate = dineDate;
		this.doorlogno = doorlogno;
		this.name = name;
		this.division = division;
		this.dept = dept;
		this.team = team;
	}
	public String getStaffID() {
		return staffID;
	}
	public void setStaffID(String staffID) {
		this.staffID = staffID;
	}
	public LocalDate getDineDate() {
		return dineDate;
	}
	public void setDineDate(LocalDate dineDate) {
		this.dineDate = dineDate;
	}
	public int getDoorlogno() {
		return doorlogno;
	}
	public void setDoorlogno(int doorlogno) {
		this.doorlogno = doorlogno;
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
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
}
