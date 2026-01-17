package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model model, HttpSession session) {
		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user", new User());
		session.removeAttribute("message");
		return "signup";
	}
	
	//handler for registering user
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult br, 
								Model model, HttpSession session) {
		
		try {
			
			if(!user.getAgreement()) {
				System.out.println("You have not accepted the terms and conditions");
				throw new Exception("You have not accepted the terms and conditions");
			}
			
			if(br.hasErrors()) {
				System.out.println("ERROR: " + br.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setImage("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("User: " + user);
			
			User result = this.userRepository.save(user);
			
			System.out.println("Result: " + result);
			
			model.addAttribute("user", new User());
			session.setAttribute("message", 
								new Message("Successfully Registered!! ", 
								"alert-success"));
			return "signup";
			
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", 
								new Message("Something went wrong: " + e.getMessage(), 
								"alert-danger"));
			return "signup";
		}

	}
	
	//handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model) {
		model.addAttribute("title", "Login Page");
		return "login";
	}
	
}

/*
 * 
 * In this lecture, we have improved processing and rendering on /user/index means dashboard page.
 * Improved user_dashboard.html and base.html which is inside normal folder.
 * Previously, we were having base.html for all visitors, now if the user is valid,
 * then we need to display the base.html from normal folder. Here, navbar is changed.
 * Included userName in place of login and for signin -> logout.
 * 
 *
 * 
 * */


