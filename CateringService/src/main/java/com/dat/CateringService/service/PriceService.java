package com.dat.CateringService.service;

import java.util.List;

import com.dat.CateringService.entity.Price;

public interface PriceService {
	public List<Price> findAll();
	public void save(Price thePrice);
	public Price findById(int theId);
	Price findActivePrice();
}
