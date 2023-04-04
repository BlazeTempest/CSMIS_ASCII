package com.dat.CateringService.entity;

import java.time.LocalDate;
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
	private LocalDate invoiceDate;
	
	@Column(name="registered_count")
	private int registeredCount;
	
	@Column(name="actual_count")
	private int actualCount;
	
	@Column(name="amount")
	private int amount;
	
	@Column(name="difference")
	private int difference;
	
	@Column(name="price")
	private int price;

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Headcount() {
		super();
	}

	public Headcount(int headcount_id, LocalDate invoice_date, int registeredCount, int actualCount, int amount,
			int difference) {
		super();
		this.headcount_id = headcount_id;
		this.invoiceDate = invoice_date;
		this.registeredCount = registeredCount;
		this.actualCount = actualCount;
		this.amount = amount;
		this.difference = difference;
	}

	public int getHeadcount_id() {
		return headcount_id;
	}

	public void setHeadcount_id(int headcount_id) {
		this.headcount_id = headcount_id;
	}

	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(LocalDate invoice_date) {
		this.invoiceDate = invoice_date;
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

	public int getDifference() {
		return difference;
	}

	public void setDifference(int difference) {
		this.difference = difference;
	}
}
