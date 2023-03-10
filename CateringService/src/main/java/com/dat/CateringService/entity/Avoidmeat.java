package com.dat.CateringService.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="avoid_meat")
public class Avoidmeat {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int avoidmeat_ID;
	 
	@Column(name="type")
	private String type;
	
	@Column(name="created_date")
	private Date created_date;
	
	@Column(name="created_by")
	private String created_by;
	
	 @Column(name="modify_date")
	private Date modify_date;
	
	@Column(name="modify_by")
	private Date modify_by;
	
	@Column(name="delete_date")
	private Date delete_date;
	
	@Column(name="delete_by")
	private String delete_by;
	
	@ManyToMany(mappedBy = "avoidmeats")
    private List<Staff> staffs;
	
	public List<Staff> getStaffs() {
		return staffs;
	}

	public void setStaffs(List<Staff> staffs) {
		this.staffs = staffs;
	}

	public Avoidmeat() {
		super();
	}

	public Avoidmeat(String type) {
		super();
		this.type = type;
	}

	public Avoidmeat(int avoidmeat_ID, String type, Date created_date, String created_by, Date modify_date,
			Date modify_by, Date delete_date, String delete_by) {
		super();
		this.avoidmeat_ID = avoidmeat_ID;
		this.type = type;
		this.created_date = created_date;
		this.created_by = created_by;
		this.modify_date = modify_date;
		this.modify_by = modify_by;
		this.delete_date = delete_date;
		this.delete_by = delete_by;
	}

	public int getAvoidmeat_ID() {
		return avoidmeat_ID;
	}

	public void setAvoidmeat_ID(int avoidmeat_ID) {
		this.avoidmeat_ID = avoidmeat_ID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Date getDelete_date() {
		return delete_date;
	}

	public void setDelete_date(Date delete_date) {
		this.delete_date = delete_date;
	}

	public String getDelete_by() {
		return delete_by;
	}

	public void setDelete_by(String delete_by) {
		this.delete_by = delete_by;
	}
}
