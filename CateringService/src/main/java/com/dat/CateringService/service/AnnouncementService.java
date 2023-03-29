package com.dat.CateringService.service;

import java.util.List;

import com.dat.CateringService.entity.Announcement;

public interface AnnouncementService {
	
	public List<Announcement> getAllAnnouncements();

	public Announcement findById(int theId);
	
	public void save(Announcement theAnnouncement);
	
	public void delete(Announcement announcement);
}
