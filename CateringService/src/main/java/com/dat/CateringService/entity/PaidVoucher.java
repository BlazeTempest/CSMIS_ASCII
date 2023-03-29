package com.dat.CateringService.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="paid_voucher")
public class PaidVoucher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int paid_ID;

	@Column(name="voucher_ID")
	private String voucher_ID;

	@Column(name="report_date")
	private Date report_date;

	@Column(name="voucher_status")
	private String status;

	@Column(name="payment_method")
	private String paymentMethod;

	@Column(name="created_date")
	private Date created_date;

	@Column(name="created_by")
	private String created_by;

	public PaidVoucher() {
		super();
	}

	public PaidVoucher(int paid_ID, String voucher_ID, Date report_date, String status, String paymentMethod,
			Date created_date, String created_by) {
		super();
		this.paid_ID = paid_ID;
		this.voucher_ID = voucher_ID;
		this.report_date = report_date;
		this.status = status;
		this.paymentMethod = paymentMethod;
		this.created_date = created_date;
		this.created_by = created_by;
	}

	public int getPaid_ID() {
		return paid_ID;
	}

	public void setPaid_ID(int paid_ID) {
		this.paid_ID = paid_ID;
	}

	public String getVoucher_ID() {
		return voucher_ID;
	}

	public void setVoucher_ID(String voucher_ID) {
		this.voucher_ID = voucher_ID;
	}

	public Date getReport_date() {
		return report_date;
	}

	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
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