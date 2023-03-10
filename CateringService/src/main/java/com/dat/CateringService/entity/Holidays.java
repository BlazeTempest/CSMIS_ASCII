package com.dat.CateringService.entity;

import java.util.Date;

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
	private Date holiday_date;

	public Holidays() {
		super();
	}

	public Holidays(Date holiday_date) {
		super();
		this.holiday_date = holiday_date;
	}

	public int getHoliday_ID() {
		return holiday_ID;
	}

	public void setHoliday_ID(int holiday_ID) {
		this.holiday_ID = holiday_ID;
	}

	public Date getHoliday_date() {
		return holiday_date;
	}

	public void setHoliday_date(Date holiday_date) {
		this.holiday_date = holiday_date;
	}
}
