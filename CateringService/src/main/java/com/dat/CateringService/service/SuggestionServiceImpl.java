package com.dat.CateringService.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dat.CateringService.daos.SuggestionRepository;
import com.dat.CateringService.entity.Suggestion;

@Service
public class SuggestionServiceImpl implements SuggestionService {
	@Autowired
	private SuggestionRepository repository;

	@Override
	public List<Suggestion> getByStartAndEndDate(LocalDate start, LocalDate end) {
		return repository.getByStartAndEndDate(start, end);
	}

	@Override
	public List<Suggestion> getAllSuggestions() {
		return repository.findAll();
	}

	@Override
	public void addSuggestion(Suggestion suggestion) {
		repository.save(suggestion);
	}
}
