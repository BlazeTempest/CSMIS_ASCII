package com.dat.CateringService.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "toggle_data")
public class ToggleData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "toggle_status")
    private boolean toggle_status;
    
    public ToggleData() {
		super();
	}

	public ToggleData(String toggle_name, boolean toggle_status) {
		super();
		
		this.toggle_status = toggle_status;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public boolean isToggle_status() {
		return toggle_status;
	}

	public void setToggle_status(boolean toggle_status) {
		this.toggle_status = toggle_status;
	}
    
    
	
    
}
