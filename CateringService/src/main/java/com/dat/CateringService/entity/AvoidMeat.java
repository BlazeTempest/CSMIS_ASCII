package com.dat.CateringService.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="avoid_meat")
public class AvoidMeat {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int avoidmeat_ID;

	@Column(name="type")
	@NotBlank (message=" Meat is required")
	private String type;

	@Column(name="created_date")
	private LocalDateTime created_date;

	public AvoidMeat() {
		super();
	}

	public AvoidMeat(String type) {
		super();
		this.type = type;
	}

	public AvoidMeat(int avoidmeat_ID, String type, LocalDateTime created_date, Byte status) {
		super();
		this.avoidmeat_ID = avoidmeat_ID;
		this.type = type;
		this.created_date = created_date;
		
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

	public LocalDateTime getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}

	

	
}
