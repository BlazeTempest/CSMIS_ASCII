package com.dat.CateringService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.HeadcountRepository;
import com.dat.CateringService.entity.Headcount;
@Service
public class HeadcountServiceImpl implements HeadcountService {
	
	@Autowired
	private HeadcountRepository headcountRepository;

	@Override
	public void saveHeadcount(Headcount headcount) {
		
		headcountRepository.save(headcount);
		

	}

}
