package com.dat.CateringService.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="restaurant")
public class Restaurant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int restaurant_ID;
	
	@Column(name="restaurant_name")
	private String restaurant_name;
	
	@Column(name="received_by")
	private String received_by;
	
	@Column(name="email")
	private String email;
	
	@Column(name="address")
	private String address;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="status")
	private String status;
	
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

	public Restaurant() {
		super();
	}

	public Restaurant(int restaurant_ID, String restaurant_name, String received_by, String email, String address,
			String phone, String status, Date created_date, String created_by, Date modify_date, Date modify_by,
			Date delete_date, String delete_by) {
		super();
		this.restaurant_ID = restaurant_ID;
		this.restaurant_name = restaurant_name;
		this.received_by = received_by;
		this.email = email;
		this.address = address;
		this.phone = phone;
		this.status = status;
		this.created_date = created_date;
		this.created_by = created_by;
		this.modify_date = modify_date;
		this.modify_by = modify_by;
		this.delete_date = delete_date;
		this.delete_by = delete_by;
	}

	public int getRestaurant_ID() {
		return restaurant_ID;
	}

	public void setRestaurant_ID(int restaurant_ID) {
		this.restaurant_ID = restaurant_ID;
	}

	public String getRestaurant_name() {
		return restaurant_name;
	}

	public void setRestaurant_name(String restaurant_name) {
		this.restaurant_name = restaurant_name;
	}

	public String getReceived_by() {
		return received_by;
	}

	public void setReceived_by(String received_by) {
		this.received_by = received_by;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
