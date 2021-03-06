package org.olumpos.forum.controllers;


import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;
import org.olumpos.forum.entities.User;
import org.olumpos.forum.services.UserService;
import org.olumpos.forum.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;


/**
 * <br>
 * @author daristote<br>
 * <br>
 * Classe qui contient les 'endpoints' des requêtes http pour l'enregistrement d'un nouvel utilisateur<br>
 * <br>
 * Les requêtes lieés aux 'users' sont gérées par les différentes méthodes définies ici<br>
 * Les paramètres des méthodes contiennent les paramètres transmis dans les requêtes (GET, POST )<br>
 * ainsi qu'une instance de l'interface Model qui permet d'inclure et modifier les attributs de session et des requêtes<br>
 * <br>
 *
 */

//Définir la classe comme un contrôleur
@Controller
//base des uri relatives aux requêtes http pour les users
@RequestMapping("/") 
//attribut de session après une connexion valide: l'utilisateur connecté et enregistré 
@SessionAttributes("user") 
public class RegistrationController {

	//Logger
	Logger logger =  Logger.getLogger(RegistrationController.class.getName());
	
	//Injection des service requis
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private UserService userService;
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet d'afficher la page contenant le formulaire d'inscription<br> 
	 * <br>
	 * @param model : model auqual on ajoute un attribut 'user' de type User qui permet d'associer les<br>
	 * champs du formulaire dûment remplis aux champs appropriés de la classe User <br>
	 * <br>
	 * @return : une chaîne de caractères correspondant à la vue associée à la page html<br>
	 * <br>  
	 */
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registrationForm(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de valider et d'enregistrer un nouvel utilisteur dans la base de données<br>
	 * <br>
	 * Elle est appelée suite à la soumission du formulaire de la page d'inscription<br>
	 * <br>
	 * @param user : Objet de type User qui contient les champs initialisés par les valeurs correspondantes du formulaire<br>
	 * @param result : objet implémentant l'interface BindingResult et qui permettra d'enregistrer le résultat de la validation<br>
	 * @param model : le 'model' <br>
	 * <br>
	 * @return : une chaîne de caractères correspondant à une redirection vers la cue 'login' associé à la page html de connexion<br>
	 * <br> 
	 */
	@PostMapping(value = "/register")//, method = RequestMethod.POST)
	public String registration(@Valid User user, BindingResult result, Model model) {
		
		logger.log(Level.INFO,"Registering User : " + user);
		
		//validatiion des champs
		userValidator.validate(user, result);
		
		//Si erreurs de validation on affiche un message d'erreur et on demeure sur la page d'inscription (vue 'register')
		if(result.hasErrors()){
			model.addAttribute("errorMsg", "Une erreur s'est produite lors de la tentative d'inscription.");
			return "register";
		}
		
		User registeredUser =  userService.createUser(user);
		
		logger.log(Level.INFO, "registered user: " + registeredUser);
		
		return "redirect:/login";
	}
	
	
}
