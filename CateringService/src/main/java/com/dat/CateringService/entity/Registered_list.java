package com.dat.CateringService.entity;

import java.util.Date;

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
	
	@Column(name="staff_ID")
	private String staff_ID;
	
	@Column(name="dine")
	private String dine;
	
	@Column(name="dineDate")
	private Date dineDate;
	
	@Column(name="registered_date")
	private Date created_date;
	
	@Column(name="registered_by")
	private String created_by;
	
	@Column(name="modify_date")
	private Date modify_date;
	
	@Column(name="modify_by")
	private Date modify_by;

	public Registered_list() {
		super();
	}

	public Registered_list(String staff_ID, String dine, Date dineDate) {
		super();
		this.staff_ID = staff_ID;
		this.dine = dine;
		this.dineDate = dineDate;
	}

	public Registered_list(int reg_ID, String staff_ID, String dine, Date dineDate, Date created_date,
			String created_by, Date modify_date, Date modify_by) {
		super();
		this.reg_ID = reg_ID;
		this.staff_ID = staff_ID;
		this.dine = dine;
		this.dineDate = dineDate;
		this.created_date = created_date;
		this.created_by = created_by;
		this.modify_date = modify_date;
		this.modify_by = modify_by;
	}

	public int getReg_ID() {
		return reg_ID;
	}

	public void setReg_ID(int reg_ID) {
		this.reg_ID = reg_ID;
	}

	public String getStaff_ID() {
		return staff_ID;
	}

	public void setStaff_ID(String staff_ID) {
		this.staff_ID = staff_ID;
	}

	public String getDine() {
		return dine;
	}

	public void setDine(String dine) {
		this.dine = dine;
	}

	public Date getDineDate() {
		return dineDate;
	}

	public void setDineDate(Date dineDate) {
		this.dineDate = dineDate;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public Date getModify_date() {
		return modify_date;
	}

	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}

	public Date getModify_by() {
		return modify_by;
	}

	public void setModify_by(Date modify_by) {
		this.modify_by = modify_by;
	}
}
