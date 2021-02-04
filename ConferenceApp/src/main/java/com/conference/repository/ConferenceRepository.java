package com.conference.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.conference.model.Conference;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long>{
	Conference findByConferenceName(String conferenceName);
	Conference findById(Integer id);
	List<Conference> findAll();
	long deleteById(Integer id);
}
