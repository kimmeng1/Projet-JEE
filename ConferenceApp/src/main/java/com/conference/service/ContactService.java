package com.conference.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conference.model.Contact;
import com.conference.repository.ContactRepository;

@Service
public class ContactService {
	
	private ContactRepository contactRepository;
	
	@Autowired
	public ContactService(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}
	
	public Contact findById(Integer id) {
		return contactRepository.findById(id);
	}
	
	public Contact saveContact(Contact contact) {
		return contactRepository.save(contact);
	}
	
	public List<Contact> findAll() {
		return contactRepository.findAll();
	}
	
}
