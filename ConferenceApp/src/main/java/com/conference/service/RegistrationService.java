package com.conference.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conference.model.Registration;
import com.conference.repository.RegistrationRepository;


@Service
public class RegistrationService {
	
	private RegistrationRepository registrationRepository;
	
	@Autowired
	public RegistrationService(RegistrationRepository registrationRepository) {
		this.registrationRepository = registrationRepository;
	}

	public Registration saveRegistration(Registration registration) {
		return registrationRepository.save(registration);
	}
	
	public List<Registration> findAll() {
		return registrationRepository.findAll();
	}
	
	public Registration findById(Integer id) {
		return registrationRepository.findById(id);
	}
	
	public Long deleteById(Integer id) {
		return registrationRepository.deleteById(id);
	}
	
}
