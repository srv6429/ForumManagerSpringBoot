package org.olumpos.forum.validators;

import org.olumpos.forum.entities.User;
import org.olumpos.forum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 
 * @author daristote
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

	@Override
	public void validate(Object target, Errors errors) {
		
		User user =  (User) target;

		String username = user.getUsername();
		String email = user.getEmail();
		
//		Optional<User> userByEmail = userRepository.findByEmail(email);
	
		boolean isUsernamePresent =  userRepository.findByUsername(username).isPresent();
		
		
		if(isUsernamePresent){
			errors.rejectValue("username", "error.exists", new Object[]{username}, "Username \""+ username +"\" already in use");
		}
		
		boolean isEmailPresent =  userRepository.findByEmail(email).isPresent();
		
		
		if(isEmailPresent){
			errors.rejectValue("email", "error.exists", new Object[]{email}, "Email \""+email+"\" already in use");
		}
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
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
