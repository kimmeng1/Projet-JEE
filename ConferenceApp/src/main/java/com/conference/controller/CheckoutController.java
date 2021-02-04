package com.conference.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.conference.model.ChargeRequest;
import com.conference.model.Conference;
import com.conference.model.Registration;
import com.conference.model.User;
import com.conference.service.ConferenceService;
import com.conference.service.RegistrationService;
import com.conference.service.UserService;
import com.conference.model.ChargeRequest.Currency;

@Controller
public class CheckoutController {
	@Autowired
	private UserService UserService;
	@Autowired
	private RegistrationService registrationService;

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey="sk_test_51IEvBEIL9njXsBcgcswkPV0Stmj2uX9q99eR7OfIGTSIjxpvFyrGjoLwjtM6CQspKKoSFSblbMis2VWHqgRDKIkf00snwcLIyK";
    
 
    
    @GetMapping(value = "/checkout/{UserEmail}/{id}")
	public ModelAndView checkoutget(@PathVariable(value = "UserEmail") String UserEmail,@PathVariable("id") Integer id) {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("UserEmail", UserEmail);
		
		Registration registration = registrationService.findById(id);
		
		String[] tab=registration.getType().split(":");
		String prix =tab[1].replaceAll("\\s+","");
		
		
		String prix2= prix.substring(0, prix.length() - 1);
		Integer price=Integer.parseInt(prix2);
		
		modelAndView.addObject("amount", price * 100); // in cents
        modelAndView.addObject("stripePublicKey", stripePublicKey);
        modelAndView.addObject("currency", ChargeRequest.Currency.EUR);
		modelAndView.setViewName("checkout");
		return modelAndView;
	}
    @PostMapping(value = "/checkout/{UserEmail}/{id}")
    public void checkoutpost(@PathVariable(value = "UserEmail") String UserEmail,@PathVariable Integer id) {
    	Registration registration = registrationService.findById(id);
    	registration.setStatus("paid");
    }
    
}
