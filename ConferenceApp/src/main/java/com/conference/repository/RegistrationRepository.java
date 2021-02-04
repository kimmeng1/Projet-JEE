package com.conference.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.conference.model.Registration;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long>{
	Registration findById(Integer id);
	Long deleteById(Integer id);
	List<Registration> findAll();
}
