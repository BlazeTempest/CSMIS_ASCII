package com.dat.CateringService.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "payment_voucher")
public class PaymentVoucher {
	@Id
	private String voucher_ID;

	@Column(name = "restaurant_name")
	private String restaurant_name;

	@Column(name = "payment_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate payment_date;

	@Column(name = "from_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate start_date;

	@Column(name = "to_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate end_date;
	
	@Column(name="payment_method")
	private String payment_method;

	/*
	 * @Column(name="headcount_ID") private int headcount_ID;
	 */

	@Column(name = "cashier")
	private String cashier;

	@Column(name = "received_by")
	private String received_by;

	@Column(name = "approved_by")
	private String approved_by;

	@Column(name = "created_date")
	private LocalDate created_date;

	@Column(name = "created_by")
	private String created_by;

	public PaymentVoucher() {
		super();
	}

	public PaymentVoucher(String voucher_ID, String restaurant_name, String payment_method, LocalDate payment_date, LocalDate start_date,
			LocalDate end_date, String cashier, String received_by, String approved_by, LocalDate created_date,
			String created_by) {
		super();
		this.voucher_ID = voucher_ID;
	    this.restaurant_name = restaurant_name;
	    this.payment_method=payment_method;
		this.payment_date = payment_date;
		this.start_date = start_date;
		this.end_date = end_date;

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

	public String getRestaurant_name() {
		return restaurant_name;
	}

	public void setRestaurant_name(String restaurant_name) {
		this.restaurant_name = restaurant_name;
	}

	public LocalDate getPayment_date() {
		return payment_date;
	}

	public void setPayment_date(LocalDate payment_date) {
		this.payment_date = payment_date;
	}
	
	

	public String getPayment_method() {
		return payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}

	public LocalDate getStart_date() {
		return start_date;
	}

	public void setStart_date(LocalDate srart_date) {
		this.start_date = srart_date;
	}

	public LocalDate getEnd_date() {
		return end_date;
	}

	public void setEnd_date(LocalDate end_date) {
		this.end_date = end_date;
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

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

}
