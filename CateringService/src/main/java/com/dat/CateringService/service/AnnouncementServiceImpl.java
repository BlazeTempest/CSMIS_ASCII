package com.dat.CateringService.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.AnnouncementRepository;
import com.dat.CateringService.entity.Announcement;
@Service
public class AnnouncementServiceImpl implements AnnouncementService {
	
	private AnnouncementRepository announcementRepository;
	
	@Autowired
	public AnnouncementServiceImpl(AnnouncementRepository theAnnouncementRepository) {
		announcementRepository = theAnnouncementRepository;
	}
	@Override
	public List<Announcement> getAllAnnouncements() {
		
		return announcementRepository.findAll();
	}

	@Override
	public Announcement findById(int theId) {
		Optional<Announcement> result = announcementRepository.findById(theId);

		Announcement theAnnouncement = null;

		if (result.isPresent()) {
			theAnnouncement = result.get();
		} else {

			throw new RuntimeException("Did not find Announcement id - " + theId);
		}

		return theAnnouncement;
	}
	

	@Override
	public void save(Announcement theAnnouncement) {
		
		announcementRepository.save(theAnnouncement);
	}

}
