package org.olumpos.forum.services;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.olumpos.forum.entities.Role;
import org.olumpos.forum.entities.User;
import org.olumpos.forum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>
 * @author daristote<br>
 * <br>
 * Classe qui permet de gérer les opération concernant les utilisateurs<br>
 *
 */
//Service Spring
@Service
@Transactional
public class UserService {

	
	//Logger
	private Logger logger =  Logger.getLogger(UserService.class.getName());
	
	//CDI: Injection du répository
	@Autowired
	UserRepository userRepository;
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui prmet d,Obtenir la liste de tous les utilisteurs<br>
	 * <br>
	 * L'annotation 'Secured' permet de renforcer la sécurité: seuls les utilisateurs ayant le rôle d'administrateur<br>
	 * ont l'autorisation de récupérer la liste de la bd<br>
	 * <br>
	 * @return List contenant tous les utilisteurs<br>
	 * <br>
	 */
	@Secured("ROLE_ADMIN")
	public List<User> findAllUsers(){
		return userRepository.findAll();
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de cherher un utilisateur avec soit son pseudonyme ou son adresse courriel<br>
	 * <br>
	 * @param usernameOrEmail: String représetnant le pseudo/courriel<br>
	 * <br>
	 * @return Optional (User) si trouvé, sinon un Optional avec valeur null<br>
	 * <br>
	 */
	public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
		logger.log(Level.INFO, "in UserService.findByUsernameOrEmail ");
		
		return userRepository.findByUsernameOrEmail(usernameOrEmail);
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de cherher un utilisateur par son identifiant<br>
	 * <br>
	 * @param id : identifiant de l'utilisateur<br>
	 * <br>
	 * @return : User ou null si non trouvé<br>
	 * <br>
	 */
	public User findUserById(Integer id) {
		
		logger.log(Level.INFO, "in UserService.findByUsernameOrEmail ");

		return userRepository.findById(id).orElse(null);
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************	

	/**
	 * <br>
	 * Méthode qui permet de créer un nouvel utilisteur, i.e. de s'inscrire<br> 
	 * <br>
	 * @param user: User avec les champs correctement remplis<br>
	 * <br>
	 * @return : Utilisateur enregistré dans la BD<br>
	 * <br>
	 */
	//@Secured("ROLE_USER")
	public User createUser(User user) {
		
		logger.log(Level.INFO, "in UserService.createUser: user to create: " + user);
		
		//Ajout d'un rôle 
		Role role = new Role();
		role.setName("ROLE_USER");
		role.setId(2);

		user.addRole(role);
		
		user.setIsActive((byte)1);
		//mot de passe crypté
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		
		//Enregistrer dans la bd
		User registeredUser =  userRepository.save(user);
		
		logger.log(Level.INFO, "in UserService.creaetUser: registeredUser: " + registeredUser);
		
		return registeredUser;
	//	return userRepository.save(user);
	}
	

	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de mettre à jour les informations conernant un utilisateur<br>
	 * <br> 
	 * L'annotation 'PreAuthorize' permet de limiter l'accès à la méthode aux utilisateurs connectés seulemeent<br>
	 * <br>
	 * @param user: User avec les champs mis à jour<br>
	 * <br>
	 * @return User enregistré dans la bd<br>
	 * <br>
	 */
	@PreAuthorize("isAuthenticated()")
	public User updateUser(User user) {
		logger.log(Level.INFO, "in UserService.updateUser: user to update: " + user);
		return userRepository.save(user);
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de mettre à jour les informations d'un utilisateurs<br>
	 *  <br>
	 * L'annotation 'PreAuthorize' permet de limiter l'accès à la méthode aux utilisateurs connectés seulemeent<br>
	 * <br>
	 * @param currentUser : User avec les informations de l'utilisateur connecté avant la mise à jour<br>
	 * @param updatedUser : User avec les informations modifiées de l'utilisateur <br>
	 * <br>
	 * @return User: l'utilsateur avec les informations enregistrées dans la bd<br>
	 * <br>
	 */
	@PreAuthorize("isAuthenticated()")
	public User updateUser(User currentUser, User updatedUser) {
		logger.log(Level.INFO, "in UserService.updateUser: currentUser "+currentUser+ ";  user to update: " + updatedUser);
		
		currentUser.setUsername(updatedUser.getUsername());
		currentUser.setEmail(updatedUser.getEmail());
		currentUser.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));
		currentUser.setUpdateDate(null);
		
		return userRepository.save(currentUser);


	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet à un adminstrateur d'activer/désactiver un utiisateur<br>
	 * <br>
	 * L'annotation 'Secured' qui renforce la sécurité: seuls les utilisateur ayant le role d'administrateur<br>
	 * <br>
	 * @param userId : lIdentifiant de l'utilisateur<br>
	 * <br>
	 * @return User: l'utisateur avec le champ isActive modifié dans la bd<br>
	 * <br>
	 */
	@Secured("ROLE_ADMIN")
	public User toggleUser(Integer userId) {
		
		//récupère l'utilisateur
		User userToUpdate =  userRepository.findById(userId).orElse(null);
		
		
		if(userToUpdate != null) {
			logger.log(Level.INFO, " in UserService.toggleUser (1): userToUpdate ==>  " + userToUpdate );
			
			//récupère le statut (is_active) de l'utilisateur
			byte status =  userToUpdate.getIsActive();
			
			//modifie la valeur du statut en effectuant un XOR: [ status ^1 ] => (0 devient 1,  1 devient 0)
			userToUpdate.setIsActive((byte)(status^1));
			
			User savedUser =  userRepository.save(userToUpdate);
			
			logger.log(Level.INFO, " in UserService.toggleUser (2): userToUpdate ==>  " + savedUser );

			return savedUser;
			
		} 
		
		return null;
	}
	
	
}
