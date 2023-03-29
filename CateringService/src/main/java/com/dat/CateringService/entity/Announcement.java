package com.dat.CateringService.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
	private LocalDateTime created_date;
	
	@Column(name="created_by")
	private String created_by;
	
	@Column(name="deleted_date")
	private LocalDate deleted_date;

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

	public Announcement(int announce_ID, String description, LocalDateTime created_date, String created_by, LocalDate deleted_date) {
		super();
		this.announce_ID = announce_ID;
		this.description = description;
		this.created_date = created_date;
		this.created_by = created_by;
		this.deleted_date = deleted_date;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public LocalDate getDeleted_date() {
		return deleted_date;
	}

	public void setDeleted_date(LocalDate modify_date) {
		this.deleted_date = modify_date;
	}
}
