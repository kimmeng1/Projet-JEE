package com.conference.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.conference.model.Conference;
import com.conference.model.Registration;
import com.conference.model.User;
import com.conference.service.RegistrationService;

@Controller
public class TestController {
	@Autowired
	RegistrationService registrationService;

	@GetMapping(value = "/test")
	public ModelAndView test() {
		ModelAndView modelAndView = new ModelAndView();

		Conference conference = new Conference();
		modelAndView.addObject("conference", conference);

		User user = new User();
		modelAndView.addObject("user", user);

		List<Registration> registrations = registrationService.findAll();
		List<Registration> listRegistrations = new ArrayList<>();
		for (Registration r : registrations) {
			if (r.getStatus() != null && r.getStatus().equals("waiting")) {
				listRegistrations.add(r);
			}
		}
		modelAndView.addObject("listRegistrations", listRegistrations);
		modelAndView.setViewName("test");
		return modelAndView;
	}

	@PostMapping(value = "/test")
	public ModelAndView testPost(@RequestParam("title[]") String[] title) {

		for (String e : title) {
			System.out.println(e);
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("test");
		return modelAndView;
	}
}
