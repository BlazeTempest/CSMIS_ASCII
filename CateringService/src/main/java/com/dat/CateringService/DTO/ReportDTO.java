package com.dat.CateringService.DTO;

public class ReportDTO {
	private String dept;
	private String team;
	private int registeredCount;
	private int actualCount;
	
	public ReportDTO(int registeredCount, int actualCount) {
		super();
		this.registeredCount = registeredCount;
		this.actualCount = actualCount;
	}
	public ReportDTO() {
		super();
	}
	public ReportDTO(String dept, String team, int registeredCount, int actualCount) {
		super();
		this.dept = dept;
		this.team = team;
		this.registeredCount = registeredCount;
		this.actualCount = actualCount;
	}
	public ReportDTO(String dept, int registeredCount, int actualCount) {
		super();
		this.dept = dept;
		this.registeredCount = registeredCount;
		this.actualCount = actualCount;
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
	public int getRegisteredCount() {
		return registeredCount;
	}
	public void setRegisteredCount(int registeredCount) {
		this.registeredCount = registeredCount;
	}
	public int getActualCount() {
		return actualCount;
	}
	public void setActualCount(int actualCount) {
		this.actualCount = actualCount;
	}
	
}
