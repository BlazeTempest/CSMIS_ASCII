package com.dat.CateringService.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.PriceRepository;
import com.dat.CateringService.entity.Price;

@Service
public class PriceServiceImpl implements PriceService {

	private PriceRepository priceRepository;

	@Autowired
	public PriceServiceImpl(PriceRepository thePriceRepository) {
		priceRepository = thePriceRepository;
	}

	@Override
	public List<Price> findAll() {
		return priceRepository.findAll();
	}

	@Override
	public void save(Price thePrice) {
		priceRepository.save(thePrice);
	}

	@Override
	public Price findById(int theId) {
		Optional<Price> result = priceRepository.findById(theId);

		Price thePrice = null;

		if (result.isPresent()) {
			thePrice = result.get();
		} else {
			// we didn't find the employee
			throw new RuntimeException("Did not find price id - " + theId);
		}
		return thePrice;
	}

	@Override
	public Price findActivePrice() {
		return priceRepository.findActivePrice();
	}

	@Override
	public Price findByDAT_price(int totalPrice, int DATprice) {
		// TODO Auto-generated method stub
		return priceRepository.findUniquePrice(totalPrice, DATprice);
	}
}
