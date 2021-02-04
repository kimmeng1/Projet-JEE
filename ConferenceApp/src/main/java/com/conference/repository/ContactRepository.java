package com.conference.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.conference.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>{
	Contact findById(Integer id);
	List<Contact> findAll();
}
