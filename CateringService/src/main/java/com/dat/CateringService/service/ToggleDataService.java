package com.dat.CateringService.service;

import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.ToggleDataRepository;
import com.dat.CateringService.entity.ToggleData;

@Service
public class ToggleDataService {
	
	private final ToggleDataRepository toggleDataRepository;

    public ToggleDataService(ToggleDataRepository toggleDataRepository) {
        this.toggleDataRepository = toggleDataRepository;
    }

    public void saveToggleData(ToggleData theToggleData) {
     
//        toggleData.setChecked(checked);
        toggleDataRepository.save(theToggleData);
    }

}
