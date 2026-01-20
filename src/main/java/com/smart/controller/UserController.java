package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	// method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);
		model.addAttribute("user", user);
	}

	// handler for user dashboard
	@GetMapping("/index")
	public String dashboard(Model model) {
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}

	// handler for opening add contact form
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}

	// handler for processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult bindingResult,
			@RequestParam("profileImage") MultipartFile file, Model model, Principal principal,
			HttpSession session) {

		System.out.println("DEBUG: Entering processContact method");

		try {
			String userName = principal.getName();
			User user = userRepository.getUserByUserName(userName);

			if (bindingResult.hasErrors()) {
				System.out.println("Validation Errors: " + bindingResult.toString());
				model.addAttribute("contact", contact);
				return "normal/add_contact_form";
			}

			// processing and uploading image file
			if (file.isEmpty()) {
				// if file is empty then try our message
				System.out.println("File is empty");
				contact.setImage("default.png");
			} else {
				// file is not empty then upload the file and
				// update the name to contact image
				contact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsoluteFile() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("Image is uploaded");
			}

			contact.setUser(user);

			user.getContacts().add(contact);

			this.userRepository.save(user);

			System.out.println("Added to Database");

			System.out.println("Contact: " + contact);

			// message success ....

			session.setAttribute("message", new Message("Contact added successfully", "success"));

			return "redirect:/user/add-contact";
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			e.printStackTrace();

			// message exception ....

			session.setAttribute("message", new Message("Something went wrong! Try again", "danger"));

		}

		return "normal/add_contact_form";
	}

	// handler for showing contacts
	// per page = [5]
	// current page = [0] (page)
	@GetMapping("/show-contacts/{page}")
	public String showContacts(Model model, @PathVariable("page") Integer page, Principal principal) {
		model.addAttribute("title", "Show User Contacts");

		// transfer contacts list from here
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);

		// current page = page
		// contacts per page = 3
		Pageable pageable = PageRequest.of(page, 3);

		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageable);

		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());

		return "normal/show_contacts";
	}

	// showing specific contact detail
	@GetMapping("/{cId}/contact")
	public String showContactDetail(Model model, @PathVariable("cId") Integer cId, Principal principal) {

		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();

		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", "Contact Detail");
		} else {
			return "redirect:/user/index";
		}

		return "normal/contact_detail";
	}

	// Delete contact handler
	@GetMapping("/delete-contact/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Model model, Principal principal,
			HttpSession session) {

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();

		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);

		if (user.getId() == contact.getUser().getId()) {
			// remove contact image too
			try {
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, contact.getImage());
				if (file1.exists() && !contact.getImage().equals("default.png")) {
					file1.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			user.getContacts().remove(contact);
			this.userRepository.save(user);

			System.out.println("DELETED :)");
			session.setAttribute("message", new Message("Contact deleted successfully!", "success"));
		}

		return "redirect:/user/show-contacts/0";
	}

	// update contact handler
	@PostMapping("/update-contact/{cId}")
	public String updateForm(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		model.addAttribute("title", "Update Contact");

		Contact contact = this.contactRepository.findById(cId).get();

		// check if contact belongs to user
		User user = this.userRepository.getUserByUserName(principal.getName());
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
		} else {
			return "redirect:/user/index";
		}

		return "normal/update_form";
	}

	// processing update contact handler
	@PostMapping("/process-update")
	public String updateHandler(@Valid @ModelAttribute("contact") Contact contact, BindingResult bindingResult,
			@RequestParam("profileImage") MultipartFile file, Model model, Principal principal,
			HttpSession session) {

		try {
			// image handling

			Contact oldContactDetail = this.contactRepository.findById(contact.getCId()).get();

			if (!file.isEmpty()) {
				// delete old contact image
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, oldContactDetail.getImage());
				if (file1.exists() && !oldContactDetail.getImage().equals("default.png")) {
					file1.delete();
				}

				// upload new contact image
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsoluteFile() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				contact.setImage(file.getOriginalFilename());

			} else {
				contact.setImage(oldContactDetail.getImage());
			}

			User user = this.userRepository.getUserByUserName(principal.getName());

			contact.setUser(user);
			this.contactRepository.save(contact);

			session.setAttribute("message", new Message("Contact updated successfully!", "success"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/user/" + contact.getCId() + "/contact";
	}

	// profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		model.addAttribute("title", "Profile Page");
		return "normal/profile";
	}

}
