package com.conference.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.conference.model.Contact;
import com.conference.service.ContactService;

@Controller
public class ContactController {
	@Autowired
	ContactService contactService;
	
	@GetMapping(value = "/contact")
	public ModelAndView getContact() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("contact", new Contact());
		modelAndView.setViewName("contact");
		return modelAndView;
	}
	
	
	@PostMapping(value = "/contact")
	public ModelAndView postContact(@Valid Contact contact, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		
		Contact contactExists = contactService.findById(contact.getId());
		if (contactExists != null) {
			bindingResult.rejectValue("userName", "error.user",
					"There is already a user registered with the user name provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			contact.setStatus("waiting");
			contactService.saveContact(contact);
			modelAndView.addObject("successMessage", true);
			modelAndView.setViewName("contact");
		}
		return modelAndView;
	}
	

}
