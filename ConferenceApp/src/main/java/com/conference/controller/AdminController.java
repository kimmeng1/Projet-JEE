package com.conference.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.servlet.ModelAndView;

import com.conference.model.Conference;
import com.conference.model.Contact;
import com.conference.model.Registration;
import com.conference.model.User;
import com.conference.service.ConferenceService;
import com.conference.service.ContactService;
import com.conference.service.RegistrationService;
import com.conference.service.UserService;

@Controller
public class AdminController {
	@Autowired
	private UserService userService;

	@Autowired
	private ConferenceService conferenceService;

	@Autowired
	private RegistrationService registrationService;
	
	@Autowired
	private ContactService contactService;

	@Autowired
	public JavaMailSender emailSender;
	
	@GetMapping(value = "/admin/home")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		
		Conference conference = new Conference();
		modelAndView.addObject("conference", conference);
		
		User user = new User();
		modelAndView.addObject("user", user);
		
		List<Registration> registrations = registrationService.findAll();
		List<Registration> refusedRegistrations = new ArrayList<>();
		List<Registration> waitingRegistrations = new ArrayList<>();
		List<Registration> validatedRegistrations = new ArrayList<>();
		for(Registration r : registrations) {
			if (r.getStatus() != null && r.getStatus().equals("refused")) {
				refusedRegistrations.add(r);
			}
			if (r.getStatus() != null && r.getStatus().equals("waiting")) {
				waitingRegistrations.add(r);
			}
			if (r.getStatus() != null && r.getStatus().equals("validated")) {
				validatedRegistrations.add(r);
			}
		}
		
		List<Conference> conferences = conferenceService.findAll();
		
		List<Contact> contacts = contactService.findAll();
		List<Contact> waitingContacts = new ArrayList<>();
		for(Contact c : contacts) {
			if (c.getStatus() != null && c.getStatus().equals("waiting")) {
				waitingContacts.add(c);
			}
		}
		
		modelAndView.addObject("refusedRegistrations", refusedRegistrations);
		modelAndView.addObject("waitingRegistrations", waitingRegistrations);
		modelAndView.addObject("validatedRegistrations", validatedRegistrations);
		modelAndView.addObject("conferences", conferences);
		modelAndView.addObject("waitingContacts", waitingContacts);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User admin = userService.findUserByUserName(auth.getName());
		modelAndView.addObject("adminName", admin.getName() + " " + admin.getLastName());
		modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		modelAndView.setViewName("admin/home");
		return modelAndView;
	}

	@PostMapping(value = "/admin/home")
	public ModelAndView adminHome(@Valid Conference conference, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		Conference conferenceExists = conferenceService.findConference(conference.getConferenceName());
		if (conferenceExists != null) {
			bindingResult.rejectValue("conferenceName", "error.conference",
					"There is already a conference registered with the user name provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("/admin/home");
		} else {
			conferenceService.saveConference(conference);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("conference", new Conference());
			modelAndView.setViewName("admin/home");
		}
		return modelAndView;
	}

	
	
	@GetMapping(value = "/admin/create")
	public ModelAndView confCreate() {
		ModelAndView modelAndView = new ModelAndView();
		Conference conference = new Conference();
		modelAndView.addObject("conference", conference);
		modelAndView.setViewName("admin/create");
		return modelAndView;
	}

	@PostMapping(value = "/admin/create")
	public String createNewConference(@RequestParam("type[]") String [] type, @Valid Conference conference, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		Conference conferenceExists = conferenceService.findConference(conference.getConferenceName());
		
		for(String t : type) {
			conference.getTypes().add(t);
		}
		
		if (conferenceExists != null) {
			bindingResult.rejectValue("conferenceName", "error.conference",
					"There is already a conference registered with the user name provided");
		}
		if (bindingResult.hasErrors()) {
			return "redirect:/admin/home";
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			Date d = new Date();
			conference.setCreationDate(formatter.format(d));
			
			String[] str = conference.getDate().split("-");
			String date = str[2] + "/" + str[1] + "/" + str[0];
			conference.setDate(date);
			conferenceService.saveConference(conference);
			modelAndView.addObject("successMessage", true);
			modelAndView.addObject("conference", new Conference());
		}
		return "redirect:/admin/home";
	}
	
	
	@GetMapping(value = "admin/file/{fileId}")
	public ResponseEntity<byte[]> get(@PathVariable("fileId") Integer fileId) {
		Registration reg = registrationService.findById(fileId);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    return new ResponseEntity<>(reg.getFile(), headers, HttpStatus.OK);
	}
	
	@GetMapping(value = "admin/validate/{id}")
	public String validate(@PathVariable("id") Integer id) {
		Registration registration = registrationService.findById(id);
		
		if(registration.getEmail() != null) {
			SimpleMailMessage message = new SimpleMailMessage();
			String email = registration.getEmail();
		
			message.setTo(registration.getEmail());
			message.setSubject("Confirmation of your registration to " + registration.getConferenceName());
			message.setText("Dear " + registration.getFirstName() + ",\n\n"
					+ "Your registration has been validated.\n"
					+ "Here is your payment link: \n\n"
					+ "http://localhost:8080/checkout/"+email+"/"+id.toString()+"\n"
					+ "Best regards,\n\n" + "Conference App support.");

			this.emailSender.send(message);
		}else {
			return "redirect:/error";
		}
	    registration.setStatus("validated");
	    registrationService.saveRegistration(registration);
	    return "redirect:/admin/home";
	}
	
	@GetMapping(value = "admin/refuse/{id}")
	public String refuse(@PathVariable("id") Integer id) {
		Registration registration = registrationService.findById(id);
		
		if(registration.getEmail() != null) {
			SimpleMailMessage message = new SimpleMailMessage();

			message.setTo(registration.getEmail());
			message.setSubject("Refusal of your registration to " + registration.getConferenceName());
			message.setText("Dear " + registration.getFirstName() + ",\n\n"
					+ "Your registration has been refused.\n"
					+ "If you want more information, please contact our service.\n\n"
					+ "Best regards,\n\n" + "Conference App support.");

			this.emailSender.send(message);
		}else {
			return "redirect:/error";
		}
		
		registration.setStatus("refused");
		registrationService.saveRegistration(registration);
	    return "redirect:/admin/home";
	}
	
	
	@GetMapping(value = "/admin/pending")
	public ModelAndView pending() {
		ModelAndView modelAndView = new ModelAndView();
		List<Registration> registrations = registrationService.findAll();
		List<Registration> listRegistrations = new ArrayList<>();
		
		for(Registration r : registrations) {
			if (r.getStatus() != null && r.getStatus().equals("Waiting")) {
				listRegistrations.add(r);
			}
		}
		modelAndView.addObject("listRegistrations", listRegistrations);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByUserName(auth.getName());
		modelAndView.addObject("userName", user.getName() + " " + user.getLastName());
		modelAndView.setViewName("admin/pending");
		return modelAndView;
	}
	
	@PostMapping(value = "/admin/registration")
	public ModelAndView adminRegistration(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByUserName(user.getUserName());
		if (userExists != null) {
			bindingResult.rejectValue("userName", "error.user",
					"There is already a user registered with the user name provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("error");
		} else {
			userService.saveUser(user, "ADMIN");
			modelAndView.addObject("successMessage", true);
			modelAndView.addObject("conference", new Conference());
			modelAndView.addObject("user", new User());
			
			List<Registration> registrations = registrationService.findAll();
			List<Registration> listRegistrations = new ArrayList<>();
			for(Registration r : registrations) {
				if (r.getStatus() != null && r.getStatus().equals("waiting")) {
					listRegistrations.add(r);
				}
			}
			
			modelAndView.addObject("listRegistrations", listRegistrations);
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User admin = userService.findUserByUserName(auth.getName());
			modelAndView.addObject("adminName", admin.getName() + " " + admin.getLastName());
			modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
			modelAndView.setViewName("admin/home");
		}
		return modelAndView;
	}
	
	@GetMapping(value = "admin/remove/{id}")
	public String remove(@PathVariable("id") Integer id) {
		Conference conference = conferenceService.findById(id);
		System.out.println(conference.getConferenceName());
		conferenceService.deleteById(id);
	    return "redirect:/admin/home";
	}
	
	@GetMapping(value = "admin/reply/{id}")
	public ModelAndView getReply(@PathVariable("id") Integer id) {
		Contact contact = contactService.findById(id);
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.addObject("contact", contact);
		modelAndView.setViewName("admin/reply");
	    return modelAndView;
	}
	
	@PostMapping(value = "admin/reply")
	public String postReply(@Valid Contact contact, BindingResult bindingResult) {
		if(contact.getEmail() != null) {
			SimpleMailMessage message = new SimpleMailMessage();

			message.setTo(contact.getEmail());
			message.setSubject("Reply of your request");
			message.setText(contact.getAdminMessage());

			this.emailSender.send(message);
		}
		
		contact.setStatus("replied");
		contactService.saveContact(contact);
//		modelAndView.addObject("successMessage", true);
//		modelAndView.setViewName("admin/reply");
		
		return "redirect:/default";
	}
	
	@GetMapping(value = "admin/edit/{id}")
	public ModelAndView getEdit(@PathVariable("id") Integer id) {
		Conference conference = conferenceService.findById(id);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("conference", conference);
		modelAndView.setViewName("admin/create");
	    return modelAndView;
	}
	
	@PostMapping(value = "/admin/edit")
	public String editConference(@RequestParam("type[]") String [] type, @Valid Conference conference) {
		ModelAndView modelAndView = new ModelAndView();

		for(String t : type) {
			conference.getTypes().add(t);
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date d = new Date();
		conference.setCreationDate(formatter.format(d));
		
		String[] str = conference.getDate().split("-");
		String date = str[2] + "/" + str[1] + "/" + str[0];
		conference.setDate(date);
		
		conferenceService.saveConference(conference);
		modelAndView.addObject("successMessage", true);
		modelAndView.addObject("conference", new Conference());
		
		return "redirect:/admin/home";
	}
	
}
