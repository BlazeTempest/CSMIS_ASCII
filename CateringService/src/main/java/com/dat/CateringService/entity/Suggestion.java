package com.dat.CateringService.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="suggestion")
public class Suggestion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sug_ID;
	
	@Column(name="description")
	private String description;
	
	@Column(name="registered_date")
	private Date created_date;
	
	@Column(name="registered_by")
	private String created_by;

	public Suggestion() {
		super();
	}

	public Suggestion(String description) {
		super();
		this.description = description;
	}

	public Suggestion(int sug_ID, String description) {
		super();
		this.sug_ID = sug_ID;
		this.description = description;
	}

	public Suggestion(int sug_ID, String description, Date created_date, String created_by) {
		super();
		this.sug_ID = sug_ID;
		this.description = description;
		this.created_date = created_date;
		this.created_by = created_by;
	}

	public int getSug_ID() {
		return sug_ID;
	}

	public void setSug_ID(int sug_ID) {
		this.sug_ID = sug_ID;
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
}
