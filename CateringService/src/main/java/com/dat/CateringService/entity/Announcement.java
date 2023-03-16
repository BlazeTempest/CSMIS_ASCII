package com.dat.CateringService.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="announcement")
public class Announcement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int announce_ID;
	
	@Column(name="description")
	private String description;
	
	@Column(name="created_date")
	private Date created_date;
	
	@Column(name="created_by")
	private String created_by;
	
	@Column(name="modify_date")
	private Date modify_date;
	
	@Column(name="modify_by")
	private Date modify_by;

	public int getAnnounce_ID() {
		return announce_ID;
	}

	public void setAnnounce_ID(int announce_ID) {
		this.announce_ID = announce_ID;
	}

	public Announcement() {
		super();
	}

	public Announcement(int announce_ID, String description) {
		super();
		this.announce_ID = announce_ID;
		this.description = description;
	}

	public Announcement(String description) {
		super();
		this.description = description;
	}

	public Announcement(int announce_ID, String description, Date created_date, String created_by, Date modify_date,
			Date modify_by) {
		super();
		this.announce_ID = announce_ID;
		this.description = description;
		this.created_date = created_date;
		this.created_by = created_by;
		this.modify_date = modify_date;
		this.modify_by = modify_by;
	}

	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
