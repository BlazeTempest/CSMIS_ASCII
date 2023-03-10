package com.dat.CateringService.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="headcount")
public class Headcount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int headcount_id;
	
	@Column(name="invoice_date")
	private Date invoice_date;
	
	@Column(name="registered_count")
	private int registeredCount;
	
	@Column(name="actual_count")
	private int actualCount;
	
	@Column(name="amount")
	private int amount;
	
	@Column(name="price_ID")
	private int price_ID;

	public Headcount() {
		super();
	}

	public Headcount(int headcount_id, Date invoice_date, int registeredCount, int actualCount, int amount,
			int price_ID) {
		super();
		this.headcount_id = headcount_id;
		this.invoice_date = invoice_date;
		this.registeredCount = registeredCount;
		this.actualCount = actualCount;
		this.amount = amount;
		this.price_ID = price_ID;
	}

	public int getHeadcount_id() {
		return headcount_id;
	}

	public void setHeadcount_id(int headcount_id) {
		this.headcount_id = headcount_id;
	}

	public Date getInvoice_date() {
		return invoice_date;
	}

	public void setInvoice_date(Date invoice_date) {
		this.invoice_date = invoice_date;
	}

	public int getRegisteredCount() {
		return registeredCount;
	}

	public void setRegisteredCount(int registeredCount) {
		this.registeredCount = registeredCount;
	}

	public int getActualCount() {
		return actualCount;
	}

	public void setActualCount(int actualCount) {
		this.actualCount = actualCount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getPrice_ID() {
		return price_ID;
	}

	public void setPrice_ID(int price_ID) {
		this.price_ID = price_ID;
	}
}
