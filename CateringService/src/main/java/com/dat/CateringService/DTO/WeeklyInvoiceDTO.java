package com.dat.CateringService.DTO;

import java.time.LocalDate;

public class WeeklyInvoiceDTO {

	private int totalPrice;

	private int totalAmount;

	private int totalCost;

	private int totalRegisterCount;

	private int totalActuralCount;

	private int totalDifference;
	
	private int numberOfPax;

	private LocalDate invoiceDate;
	private int registeredCount;
	private int actualCount;
	private int difference;

	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}


	public int getNumberOfPax() {
		return numberOfPax;
	}
	public void setNumberOfPax(int numberOfPax) {
		this.numberOfPax = numberOfPax;
	}
	public int getTotalRegisterCount() {
		return totalRegisterCount;
	}
	public void setTotalRegisterCount(int totalRegisterCount) {
		this.totalRegisterCount = totalRegisterCount;
	}
	public int getTotalActuralCount() {
		return totalActuralCount;
	}
	public void setTotalActuralCount(int totalActuralCount) {
		this.totalActuralCount = totalActuralCount;
	}
	public int getTotalDifference() {
		return totalDifference;
	}
	public void setTotalDifference(int totalDifference) {
		this.totalDifference = totalDifference;
	}
	public int getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}
	public int getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(LocalDate invoiceDate) {
		this.invoiceDate = invoiceDate;
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
	public int getDifference() {
		return difference;
	}
	public void setDifference(int difference) {
		this.difference = difference;
	}
	public WeeklyInvoiceDTO(int totalPrice, LocalDate invoiceDate,int totalAmount,int totalCost, int registeredCount, int actualCount,
			int difference) {
		super();
		this.totalPrice = totalPrice;
		this.invoiceDate = invoiceDate;
		this.totalAmount=totalAmount;
		this.totalCost=totalCost;
		this.registeredCount = registeredCount;
		this.actualCount = actualCount;
		this.difference = difference;
	}
	public WeeklyInvoiceDTO() {
		super();
	}
}