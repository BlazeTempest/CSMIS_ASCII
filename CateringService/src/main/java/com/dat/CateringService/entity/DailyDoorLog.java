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
@Table(name="daily_doorlog")
public class DailyDoorLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int daily_door_id;
	
	@Column(name="door_log_no")
	private int doorLogNo;
	
	@Column(name="staffID")
	private String staffID;
	
	@Column(name="imported_by")
	private String imported_by;
	
	@Column(name="imported_date")
	private LocalDate imported_date;
	
	@Column(name="dine_date")
	private LocalDate dineDate;

	public LocalDate getDineDate() {
		return dineDate;
	}

	public void setDineDate(LocalDate dineDate) {
		this.dineDate = dineDate;
	}

	public LocalDate getImported_date() {
		return imported_date;
	}

	public void setImported_date(LocalDate imported_date) {
		this.imported_date = imported_date;
	}

	public String getImported_by() {
		return imported_by;
	}

	public void setImported_by(String imported_by) {
		this.imported_by = imported_by;
	}

	public DailyDoorLog() {
		super();
	}

	public DailyDoorLog(int door_ID, int doorlog_no, String staffID) {
		super();
		this.daily_door_id = door_ID;
		this.doorLogNo = doorlog_no;
		this.staffID = staffID;
	}

	public int getDaily_door_id() {
		return daily_door_id;
	}

	public void setDaily_door_id(int door_ID) {
		this.daily_door_id = door_ID;
	}

	public int getDoorLogNo() {
		return doorLogNo;
	}

	public void setDoorLogNo(int door_log_no) {
		this.doorLogNo = door_log_no;
	}

	public String getStaffID() {
		return staffID;
	}

	public void setStaffID(String staffID) {
		this.staffID = staffID;
	}
}
