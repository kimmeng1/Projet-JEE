package com.conference.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.conference.model.Conference;
import com.conference.model.Registration;
import com.conference.model.User;
import com.conference.service.ConferenceService;
import com.conference.service.RegistrationService;
import com.conference.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ConferenceService conferenceService;

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	public JavaMailSender emailSender;

	@GetMapping(value = "/user/home")
	public ModelAndView userHome() {
		List<Conference> conferences = conferenceService.findAll();

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("listConferences", conferences);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUserName(auth.getName());
		modelAndView.addObject("userName", user.getName() + " " + user.getLastName());
		modelAndView.setViewName("user/home");
		return modelAndView;
	}

	@GetMapping(value = "/conference/{conferenceName}")
	public ModelAndView confRegisterGet(@PathVariable(value = "conferenceName") String conferenceName) {
		ModelAndView modelAndView = new ModelAndView();
		Conference conference = conferenceService.findConference(conferenceName);

		System.out.println(conference.getTypes());

		Registration registration = new Registration();
		modelAndView.addObject("registration", registration);
		modelAndView.addObject("conference", conference);
		modelAndView.addObject("conferenceName", conferenceName);
		modelAndView.addObject("conferenceType", conference.getTypes());
		modelAndView.setViewName("conference/registration");
		return modelAndView;
	}

	@PostMapping(value = "/conference/{conferenceName}")
	public ModelAndView confRegisterPost(@PathVariable(value = "conferenceName") String conferenceName,
			@RequestParam("file") MultipartFile file, @Valid Registration registration, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();

		try {
			byte[] contents = file.getBytes();
			registration.setFile(contents);
		} catch (IOException e) {
			e.printStackTrace();
		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date d = new Date();
		registration.setCreationDate(formatter.format(d));

		registration.setFileTitle(file.getOriginalFilename());
		registration.setStatus("waiting");
		registrationService.saveRegistration(registration);

		if (registration.getEmail() != null) {
			SimpleMailMessage message = new SimpleMailMessage();
			
			message.setTo(registration.getEmail());
			message.setSubject("Registration to " + registration.getConferenceName());
			message.setText("Dear " + registration.getFirstName() + ",\n\n"
					+ "Your registration has been registered successfully.\n"
					+ "Once your registration is validated, you will receive a link to make your payment if necessary.\n\n"
					
					+ "Best regards,\n\n" + "Conference App support.");

			this.emailSender.send(message);
		}
		modelAndView.addObject("registration", new Registration());
		modelAndView.setViewName("conference/succes");

		return modelAndView;
	}

	@GetMapping(value = "/conference")
	public ModelAndView conference() {
		List<Conference> conferences = conferenceService.findAll();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("listConferences", conferences);
		modelAndView.setViewName("conference/home");
		return modelAndView;
	}

	@GetMapping(value = "/terms")
	public ModelAndView terms() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("terms");
		return modelAndView;
	}

}
