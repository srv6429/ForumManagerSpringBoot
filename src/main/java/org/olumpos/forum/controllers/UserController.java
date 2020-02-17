package org.olumpos.forum.controllers;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.olumpos.forum.entities.JSONResponse;
import org.olumpos.forum.entities.User;
import org.olumpos.forum.services.UserService;
import org.olumpos.forum.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


/**
 * <br>
 * @author daristote<br>
 * <br>
 * Classe qui contient les 'endpoints' des requêtes http pour les utilisateurs (users)<br>
 * <br>
 * Les requêtes lieés aux users sont gérées par les différentes méthodes définies ici<br>
 * Les paramètres des méthodes contiennent les paramètres transmis dans les requêtes (GET, POST, PUT, DELETE )<br>
 * ainsi qu'une instance de l'interface Model qui permet d'inclure et modifier les attributs de session et des requêtes<br>
 *<br>
 */

//Définir la classe comme un contrôleur
@Controller
//base des uri relatives aux requêtes http pour les users
@RequestMapping("/") 
//attribut de session après une connexion valide: l'utilisateur connecté et enregistré 
@SessionAttributes("user") 
public class UserController {

	//Logger
	private Logger logger =  Logger.getLogger(UserController.class.getName());
	
	//injection des service requis
	@Autowired
	private UserService userService;
	
	//Valideur pour les champs des formulaires
	@Autowired
	private UserValidator userValidator;
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * <br>
	 * Méthode qui permet de récupérer l'ensemble des utilisateurs<br>
	 * <br> 
	 * Seul un gestionnaire peut obtenir la liste des utilisateurs<br>  
	 * <br>
	 * @param model : model qui permet d'enregistrer l'attribut 'users' dans la requête et qui permettra à la vue 'users' d'afficher<br> 
	 * la liste des utilisateurs sur la page html  <br>
	 * <br>
	 * @return : une chaîne de caractères correspondant à la vue associée à la page html<br>  
	 * <br>
	 * TODO: gestion d'une exception si la liste est vide ou si une erreur survient lors de la connection avec la base de données<br>
	 * <br>
	 */
	@GetMapping("/admin/users")
	public String getUsers(Model model){
		
		List<User> users = userService.findAllUsers();
		
		model.addAttribute("users", users);
		
		return "users";
	}
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet à une administrateur d'activer1/désactiver un utilisateur en modifiant<br> 
	 * la valeur du champ 'isActive' (byte): valeurs possibles: 0 ou 1<br>
	 * <br>
	 * @param id : l'identifiant de l'utilisteur<br>
	 * @param model : model<br>
	 *  <br>
	 * @return : ResponseEntity paramétré avec un objet de type JSONResponse qui détermine si la réponse est valide ou non<br>
	 * 		Le résultat est géré sous forme d'objet JSON par jQuery sur la page html et affiche une message d'erreur si échec<br>
	 * <br>
	 */
	@PutMapping(value="/admin/users/{id}", produces = "application/json;charset=UTF-8")
	public ResponseEntity<JSONResponse> toggleUser(@PathVariable int id, Model model){
		
		logger.log(Level.INFO, " in toggleUser with method PUT: id ==>  " + id );
					
		JSONResponse result = new JSONResponse();
		
		//on met à jour le statut et récupère l'utilisateur
		User userToUpdate =  userService.toggleUser(id);

		if(userToUpdate != null) {
			result.setStatus("ok");
		} else {
			result.setStatus("error");
		}
		return ResponseEntity.ok(result);
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet d'afficher la page de profil pour un utilisateur connecté<br>
	 * L'utilisateur peut modifier son pseudonyme, son adresse courriel et son mot de passe<br>
	 * Les champs sont automatiquement remplies avec les valeurs actuelles<br>
	 * <br>
	 * @param model : le modèele conetnant la vairable de session de l'utilisateur et ses paramètres<br>
	 *<br>
	 * @return : une chaîne de caractères correspondant à la vue 'profile' qui permet d'afficher la page html appropriée<br>
	 *<br>
	 */
	
	@GetMapping(value = "/profile")
	public String profileForm(Model model) {
		logger.log(Level.INFO,"Get profile User with method GET: ==========================> ");
		return "profile";
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de modifier les paramètres de l'utilisteur une fois le formulaire de la vue 'profile' soumis<br>
	 * <br> 
	 * Utilisation d'un validator pour les différents champs<br>
	 * <br>
	 * @param updatedUser :objet de type User contenant les champs modifiés dans le formulaire<br>
	 * @param result : objet implémentant l'interface BindingResult et qui permettra d'enregistrer le résultat de la validation<br>
	 * @param model : le model contenant l'attribut de session ('user') qui contient les données de l'utilisateur connecté <br>
	 * <br>
	 * @return : une chaîne de caractères correspondant à la vue vers laquelle l'utilisateur sera dirigée<br>
	 * <br>
	 * Ici on reste sur la page de profil et on affiche un message de succès ou échec si le profil a été modifiéavec scuccès ou non<br> 
	 *<br>
	 * TODO: trouver la source du problème<br>
	 * Problème: methode 'put' non reconnue depuis la mise à jour de la version de Spring Boot: de 2.1.7 à 2.2.4<br>
	 * Thymeleaf semble accepter la méthode 'put' pour un formulaire, mais la requête malgré tout est transmise via la méthode 'post'<br>
	 * Note: Les formulaires html n'acceptent pas les méthode 'put' et 'delete'; seulement 'get' et 'post'<br>
	 * <br>
	 */
	
	//	@PutMapping(value = "/profile") 
	@PostMapping(value = "/profile")
	public String updateProfile(@Valid User updatedUser, BindingResult result, Model model) {
		
		logger.log(Level.INFO,"Update profile User  with method PUT: ==========================> " + updatedUser);
		
		//Table associative contenant les attributs du modèle
		Map<String, Object> attributes =  model.asMap();
		
		//On récupère la variable de session: l'objet 'user' 
		User currentUser = (User)attributes.get("user");
		
		logger.log(Level.INFO, "currentUser: " +  currentUser);
		
		//Validation des données 
		userValidator.validateUpdate(currentUser, updatedUser, result);
	
		//si au moins une erreur enregistrée, échec
		if(result.hasErrors()){
			model.addAttribute("errorMsg", "Une erreur s'est produite lors de la tentative de mise à jour de votre profil.");
			return "profile";
		}
		
		//on sauvegarde les inormations modifiées dans la bd
		User savedUser =  userService.updateUser(currentUser, updatedUser);
		
		logger.log(Level.INFO, "saved user: " + savedUser);
		
		//on réinitialise l'attribut de session avec les nouvelles valeurs
		model.addAttribute("user", currentUser);
		//in enrigistre message de succès dans un attribut du 'model'
		model.addAttribute("successMsg", "Votre profil a été modifié avec succès.");
		
		return "profile";
	}
	
}
