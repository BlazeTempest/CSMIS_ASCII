package com.dat.CateringService.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="holidays")
public class Holidays {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int holiday_ID;
	
	@Column(name="holiday_date")
	private LocalDate holidayDate;
	
	@Column(name = "holiday_name")
	private String holidayName;

	public Holidays() {
		super();
	}

	public Holidays(LocalDate holiday_date) {
		super();
		this.holidayDate = holiday_date;
	}
	
	public Holidays(LocalDate holiday_date, String holiday_name) {
		super();
		this.holidayDate = holiday_date;
		this.holidayName = holiday_name;
	}

	public int getHoliday_ID() {
		return holiday_ID;
	}

	public void setHoliday_ID(int holiday_ID) {
		this.holiday_ID = holiday_ID;
	}

	public LocalDate getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(LocalDate holiday_date) {
		this.holidayDate = holiday_date;
	}

	public String getHolidayName() {
		return holidayName;
	}

	public void setHolidayName(String holiday_name) {
		this.holidayName = holiday_name;
	}
	
}
