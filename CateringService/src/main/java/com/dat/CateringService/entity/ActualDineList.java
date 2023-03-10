package com.dat.CateringService.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="actual_dine_list")
public class ActualDineList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int act_id;
	 
	@Column(name="staff_ID")
	private int staff_ID;
	 
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

	public ActualDineList() {
		super();
	}

	public ActualDineList(int staff_ID, Date dineDate) {
		super();
		this.staff_ID = staff_ID;
		this.dineDate = dineDate;
	}

	public ActualDineList(int act_id, int staff_ID, Date dineDate, Date created_date, String created_by,
			Date modify_date, Date modify_by) {
		super();
		this.act_id = act_id;
		this.staff_ID = staff_ID;
		this.dineDate = dineDate;
		this.created_date = created_date;
		this.created_by = created_by;
		this.modify_date = modify_date;
		this.modify_by = modify_by;
	}

	public int getAct_id() {
		return act_id;
	}

	public void setAct_id(int act_id) {
		this.act_id = act_id;
	}

	public int getStaff_ID() {
		return staff_ID;
	}

	public void setStaff_ID(int staff_ID) {
		this.staff_ID = staff_ID;
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
