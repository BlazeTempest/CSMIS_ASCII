package com.dat.CateringService.entity;

import java.sql.Timestamp;

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
	private int door_ID;
	
	@Column(name="doorLogNo")
	private int doorlog_no;
	
	@Column(name="timedate")
	private Timestamp datetime;

	public DailyDoorLog() {
		super();
	}

	public DailyDoorLog(int door_ID, int doorlog_no, Timestamp datetime) {
		super();
		this.door_ID = door_ID;
		this.doorlog_no = doorlog_no;
		this.datetime = datetime;
	}

	public int getDoor_ID() {
		return door_ID;
	}

	public void setDoor_ID(int door_ID) {
		this.door_ID = door_ID;
	}

	public int getDoorlog_no() {
		return doorlog_no;
	}

	public void setDoorlog_no(int doorlog_no) {
		this.doorlog_no = doorlog_no;
	}

	public Timestamp getDatetime() {
		return datetime;
	}

	public void setDatetime(Timestamp datetime) {
		this.datetime = datetime;
	}
}
