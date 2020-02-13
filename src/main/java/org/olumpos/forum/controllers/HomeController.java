package org.olumpos.forum.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author daristote
 *
 * Contrôleur de base qui gère la page d'accueil
 * On redirige vers la page 'topics'. Si L'utilisteur n'est pas connecté, il sera redirigé vers la page 'login'
 *
 */
@Controller
@RequestMapping("/")
public class HomeController{
		
	@GetMapping("/")
	public String home(Model model){
		
//		model.addAttribute("messages", messageRepository.findAll());		
		return "redirect:/topics";
	}
	
//	@RequestMapping(value="/403")
//	public String accessDenied(){
//		return "error/403";
//	}
	
//	@GetMapping("/admin")
//	public String adminHome(Model model){
//		return "redirect:/admin/home";
//	}

}
