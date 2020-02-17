package org.olumpos.forum.validators;

import org.olumpos.forum.entities.User;
import org.olumpos.forum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <br>
 * @author daristote<br>
 * <br>
 * <br>Classe qui permet de calider les inforamtions entrées dans un formulaire
 * <br>
 *
 */

@Component
public class UserValidator implements Validator {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de vérifier, lorsqu'un nouvel utilisteur tente de s'enregistrer, si le nom d'un utilisateur ou son adresse courriel sont déjà<br>
	 * enregistrés dans la base de données. Si c'est le cas on retourne une erreur avec le message qu'ils ne peuvent être utilisés<br>
	 * puisque les psuedonymes et courriel doivent être unitque <br>
	 * <br>
	 */
	@Override
	public void validate(Object target, Errors errors) {
		
		User user =  (User) target;

		String username = user.getUsername();
		String email = user.getEmail();
		
//		Optional<User> userByEmail = userRepository.findByEmail(email);
	
		//vérification du pseudonyme
		boolean isUsernamePresent =  userRepository.findByUsername(username).isPresent();
		//si présent, erreur
		if(isUsernamePresent){
			errors.rejectValue("username", "error.exists", new Object[]{username}, "Username \""+ username +"\" already in use");
		}
		
		//vérification de l'adresse courriel
		boolean isEmailPresent =  userRepository.findByEmail(email).isPresent();
		
		//si présent, erreur
		if(isEmailPresent){
			errors.rejectValue("email", "error.exists", new Object[]{email}, "Email \""+email+"\" already in use");
		}
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de vérifier sir la mise à jour du pseudonyme ou du courriel, provenant de la page profil, d'un utilisateur enregistré<br> 
	 * n'entrent pas en conflit avec le pseudonyme ou le courriel d'un autre utillisteur<br>
	 * <br>
	 * @param currentUser: un User avec les données courantes de l'utilisateur connecté<br>
	 * @param updatedUser: les données à modifiées rentrées dansle formulaire de mise à jour<br>
	 * @param errors: un objet de type Errors enregistrant les messages d'erreur<br>
	 * <br>
	 */
	public void validateUpdate(User currentUser, User updatedUser, Errors errors) {
		

		String username = updatedUser.getUsername();
		String email = updatedUser.getEmail();
		
	
		if(!username.equals(currentUser.getUsername()) &&  userRepository.findByUsername(username).isPresent()) {
			errors.rejectValue("username", "error.exists", new Object[]{username}, "Username \""+ username +"\" already in use");
		}

		 if(!email.equals(currentUser.getEmail()) && userRepository.findByEmail(email).isPresent()) {
			errors.rejectValue("email", "error.exists", new Object[]{email}, "Email \""+email+"\" already in use");
			 
		 }
				
	}

}
