package com.dat.CateringService.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="payment_voucher")
public class PaymentVoucher {
	@Id
	private String voucher_ID;
	
	@Column(name="restaurant_id")
	private int restaurant_id;
	
	@Column(name="payment_date")
	private Date payment_date;
	
	@Column(name="from_date")
	private Date from;
	
	@Column(name="headcount_ID")
	private int headcount_ID;
	
	@Column(name="cashier")
	private String cashier;
	
	@Column(name="received_by")
	private String received_by;
	
	@Column(name="approved_by")
	private String approved_by;
	
	@Column(name="created_date")
	private Date created_date;
	
	@Column(name="created_by")
	private String created_by;

	public PaymentVoucher() {
		super();
	}

	public PaymentVoucher(String voucher_ID, int restaurant_id, Date payment_date, Date from, int headcount_ID,
			String cashier, String received_by, String approved_by, Date created_date, String created_by) {
		super();
		this.voucher_ID = voucher_ID;
		this.restaurant_id = restaurant_id;
		this.payment_date = payment_date;
		this.from = from;
		this.headcount_ID = headcount_ID;
		this.cashier = cashier;
		this.received_by = received_by;
		this.approved_by = approved_by;
		this.created_date = created_date;
		this.created_by = created_by;
	}

	public String getVoucher_ID() {
		return voucher_ID;
	}

	public void setVoucher_ID(String voucher_ID) {
		this.voucher_ID = voucher_ID;
	}

	public int getRestaurant_id() {
		return restaurant_id;
	}

	public void setRestaurant_id(int restaurant_id) {
		this.restaurant_id = restaurant_id;
	}

	public Date getPayment_date() {
		return payment_date;
	}

	public void setPayment_date(Date payment_date) {
		this.payment_date = payment_date;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public int getHeadcount_ID() {
		return headcount_ID;
	}

	public void setHeadcount_ID(int headcount_ID) {
		this.headcount_ID = headcount_ID;
	}

	public String getCashier() {
		return cashier;
	}

	public void setCashier(String cashier) {
		this.cashier = cashier;
	}

	public String getReceived_by() {
		return received_by;
	}

	public void setReceived_by(String received_by) {
		this.received_by = received_by;
	}

	public String getApproved_by() {
		return approved_by;
	}

	public void setApproved_by(String approved_by) {
		this.approved_by = approved_by;
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
