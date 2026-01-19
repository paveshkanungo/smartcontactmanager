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
 * In this lecture, we have learnt about Pagination.
 * On our show contacts page, we have added pagination.
 * For Pagination, we have used Pageable interface.
 * See UserController, 'currentPage' is representing 'page' which we
 * are taking on url. We are using PageRequest.of() to create a Pageable
 * object. We are using Pageable to get the contacts from the database
 * using contactRepository.findContactsByUser().
 * See ContactRepository, 'findContactsByUser' is a method which is
 * using Pageable to get the contacts from the database.
 * If you open Pageable, you mostly see getters like getPageNumber()
 * and getPageSize(). It looks like a simple data holder, and that
 * is exactly what it is. 
 * Pageable itself doesn't do the work. It is just an enveloper
 * that carries two numbers:
 * 1. Page Number, means which page?
 * 2. Page Size, means how many items per page?
 * The "Useful" work happens inside Spring Data JPA's internal query
 * builder, hidden deep inside the library.
 * When you write this line in your ContactRepository:
 * public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);
 * Spring Data JPA sees the Pageable argument and internally 
 * generates SQL like this:
 * SELECT * FROM contact WHERE user_id = 5 LIMIT 3 OFFSET 0;
 * 
 * */


