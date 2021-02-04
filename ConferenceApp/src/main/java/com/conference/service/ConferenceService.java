package com.conference.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conference.model.Conference;
import com.conference.repository.ConferenceRepository;

@Service
public class ConferenceService {
	
	private ConferenceRepository conferenceRepository;
	
	@Autowired
	public ConferenceService(ConferenceRepository conferenceRepository) {
		this.conferenceRepository = conferenceRepository;
	}
	
	public Conference findConference(String conferenceName) {
		return conferenceRepository.findByConferenceName(conferenceName);
	}
	
	public Conference findById(Integer id) {
		return conferenceRepository.findById(id);
	}
	
	public Conference saveConference(Conference conference) {
		conference.setConferenceName(conference.getConferenceName());
		return conferenceRepository.save(conference);
	}
	
	public List<Conference> findAll() {
		return conferenceRepository.findAll();
	}
	
	@Transactional
	public void deleteById(Integer id) {
		conferenceRepository.deleteById(id);
	}
}
