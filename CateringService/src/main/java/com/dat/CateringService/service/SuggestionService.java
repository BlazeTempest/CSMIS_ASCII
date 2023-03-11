package com.dat.CateringService.service;

import java.time.LocalDate;
import java.util.List;

import com.dat.CateringService.entity.Suggestion;

public interface SuggestionService {
	public List<Suggestion> getAllSuggestions();
	public List<Suggestion> getByStartAndEndDate(LocalDate start, LocalDate end);
}
