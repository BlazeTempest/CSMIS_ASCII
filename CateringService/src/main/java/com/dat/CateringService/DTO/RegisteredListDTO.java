package com.dat.CateringService.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegisteredListDTO {
	private List<Boolean> checkValue = new ArrayList<>();
	
	public RegisteredListDTO() {
		
	}
	
	public RegisteredListDTO(int size) {
        this.checkValue = new ArrayList<>(Collections.nCopies(size, false));
    }
	
	public List<Boolean> getCheckValue() {
		return checkValue;
	}

	public void setCheckValue(List<Boolean> checkboxValue) {
		this.checkValue = checkboxValue;
	}
}
