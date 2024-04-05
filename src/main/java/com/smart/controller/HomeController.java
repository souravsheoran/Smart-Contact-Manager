package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }
    
    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - Smart Contact Manager");
        return "about";
    }
    
    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Title - Smart Contact Manager");
        
        model.addAttribute("user",new User());
        return "signup";
    }
    
    // This handler for register user
    @RequestMapping(value ="/do_register",method = RequestMethod.POST)
    public String registerUser( @Valid @ModelAttribute("user") User user,BindingResult result,@RequestParam(value = "agreement",defaultValue="false")boolean agreement,Model model,HttpSession session) {
    	
    	try {
    		if(!agreement) {
        		System.out.println("You have not agreed terms and Conditions");
        throw new Exception("You have not agreed terms and Conditions");
    		}
    		
    		if(result.hasErrors()) {
    			System.out.println("ERROR "+result.toString());
    			model.addAttribute("user",user);
    			return "signup";
    		}
        	
        	user.setRole("ROLE_USER");
        	user.setEnabled(true);
        	user.setImageUrl("default.png");
        	user.setPassword(passwordEncoder.encode(user.getPassword()));
        	
        	User result1 = this.userRepository.save(user);
        	
        	model.addAttribute("user", new User());
        	
        	System.out.println("Agreement"+agreement);
        	System.out.println("USER"+user);
        	
        	model.addAttribute("user",result);
        	
    		session.setAttribute("message", new Message("Successfully Registered !! ","alert-success"));
            return "signup";

    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		model.addAttribute("use",user);
    		session.setAttribute("message", new Message("Something went Wrong !!"+e.getMessage(),"alert-danger"));
    	}
    	return "signup";
    }
    
    // Handler for Custom Login
    @GetMapping("/signin")
    public String customLogin(Model model) {
    	model.addAttribute("title","Login Page");
    	return "login";
    }
    
}
