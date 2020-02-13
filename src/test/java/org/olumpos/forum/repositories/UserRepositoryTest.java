package org.olumpos.forum.repositories;


import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.olumpos.forum.entities.User;
import org.olumpos.forum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.context.ApplicationContext;
//import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author daristote
 *
 * Classe qui permet de tester les méthodes de la class userRepository et les  accès à la base de données
 * 
 * Les méthode qui sont annotées avec @Transactional permettent de n'effectuer aucun changement dans la base de données
 * En effet, jumulées avec @Test, l'opération 'rollback' est automatiquement effectuée après l'exécution de la fonction
 * Ainsi, on peut procéder aux tests sans craindre de 'polluer' la bd
 * 
 * Ces tests ne peuvent s'effecteur qu'avec une BD de données initialisée contenant le schéma du forum
 * Pour désactiver les tests, on peut commenter les annotations @Test précédant les méthodes
 * Il est toutefois nécessaire de laisse décommenté l'annotation devant dummyMethod(), car une classe de test doit au moins 
 * ontenir une méthode à tester
 *
 */


@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@SpringBootTest
public class UserRepositoryTest {

	//Logger
	private Logger logger =  Logger.getLogger(UserRepositoryTest.class.getName());
	
	@Autowired
	UserRepository userRepository;
	
	
	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************

	@Test
	public void dummyMethod() {

		logger.log(Level.WARNING, "In UserRepositoryTest.dummyMethod: @Test executed " );

	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui permet de tester l'obtnetion de la liste de tous les users dans la bd
	 */
	
	@Test
	public void testFindAll() {
		
		List<User> users =  userRepository.findAll();
		
		
		assertNotNull(users);

		
		logger.log(Level.INFO, "in UserRepositoryTest.testFindAll(): " + users);
		
		//au moins un utilisateur
		assertTrue(users.size() > 0);
		
		
	}
	

	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui permet de tester la recherhe d'un user en fonction de son identifiant
	 */
	@Test
	public void testFindById() {
		
		User user =  userRepository.findById(1).orElse(null);
		
		logger.log(Level.INFO, "in UserRepositoryTest.testFindById(): " + user);
		
		assertNotNull(user);
		assertEquals(1, user.getId());
		assertEquals("admin", user.getUsername());
		assertEquals("admin@olumpos.org", user.getEmail());

	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui permet de tester l'ajout d'un user
	 * @Transactional assure qu'aucun ajout ne sera fait, i.e. qu'un rollback sera effectué à la fin de l'opération
	 */
	@Test
	//@Transactional(propagation = Propagation.NOT_SUPPORTED) // no rollback; changes will be persisted in DB
	@Transactional //insure a rollback after operations are complete; no persistence in DB
	public void addUser() {
		
		
		User user = new User();
		user.setUsername("gwashington");
		user.setEmail("gwashington@whitehouse.us");
		user.setPassword("gwashington123");
		user.setCreationDate(LocalDateTime.now());
		user.setUpdateDate(LocalDateTime.now());
		
		logger.log(Level.INFO, "in TopicRepositoryTest.addTopic(1): user ======================>  " + user);
		
		User savedUser = userRepository.save(user);
		
		assertNotNull(savedUser);
		logger.log(Level.INFO, "in UserRepositoryTest.addUser(2): savedTopic ========================>  " + savedUser);
		
		
		assertEquals(savedUser.getUsername(), user.getUsername());
		assertEquals(savedUser.getEmail(), user.getEmail());
		assertEquals(savedUser.getPassword(), user.getPassword());
		assertEquals(savedUser.getNonFormattedCreationDate(), user.getNonFormattedCreationDate());
		assertEquals(savedUser.getNonFormattedUpdateDate(), user.getNonFormattedUpdateDate());

	}

	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui permet de tester la mise à jour d'un user
	 * @Transactional assure qu'aucun changementne sera fait, i.e. qu'un rollback sera effectué à la fin de l'opération
	 */
	@Test
	//@Transactional(propagation = Propagation.NOT_SUPPORTED) // no rollback; changes will be persisted in DB
	@Transactional //insure a rollback after operations are complete; no persistence in DB
	public void updateUser() {
		
		
		User user =  userRepository.findById(15).orElse(null);
		
		logger.log(Level.INFO, "in UserRepositoryTest.updateUser() user: " + user);
		
		assertNotNull(user);
		
		String originalUsername =  user.getUsername();
		String originalEmail =  user.getEmail();
		String originalPassword =  user.getPassword();
		
		LocalDateTime initialupdateDate = user.getNonFormattedUpdateDate();
		
		String updatedUsername = "tjefferson";
		String updatedEmail =  "tjefferson@whitehouse.us";
		String updatedPassword = "tjefferson123";
		
		user.setUsername(updatedUsername);
		user.setEmail(updatedEmail);
		user.setPassword(updatedPassword);
		
		user.setUpdateDate(LocalDateTime.now());
		
		User updatedUser = userRepository.save(user);
		
		assertNotNull(updatedUser);
		
		logger.log(Level.INFO, "in UserRepositoryTest.updateUser() updatedUser (1): " + updatedUser);
		
		assertEquals(updatedUser.getUsername(), updatedUsername);
		assertEquals(updatedUser.getEmail(), updatedEmail);
		assertEquals(updatedUser.getPassword(), updatedPassword);
		
		LocalDateTime updatedUpdateDate = updatedUser.getNonFormattedUpdateDate();
		
		//vérification que la date de mise à jour a été modifiée
		assertNotEquals(initialupdateDate, updatedUpdateDate);

		
		//on s'assure que la date de mise à jour ne sera pas la même
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
	
		
		//rétablir les valeurs initiales de l'utilisateur
		
		updatedUser.setUsername(originalUsername);
		updatedUser.setEmail(originalEmail);
		updatedUser.setPassword(originalPassword);
		
		updatedUser.setUpdateDate(LocalDateTime.now());
		
		updatedUser = userRepository.save(updatedUser);
		
		logger.log(Level.INFO, "in UserRepositoryTest.updateUser() updatedUser (2): " + updatedUser);
		
		assertEquals(updatedUser.getUsername(), originalUsername);
		assertEquals(updatedUser.getEmail(), originalEmail);
		assertEquals(updatedUser.getPassword(), originalPassword);
		
		assertNotEquals(updatedUpdateDate, updatedUser.getNonFormattedUpdateDate());
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui permet de tester la désactivation d'un user
	 * @Transactional assure qu'aucun changement ne sera fait, i.e. qu'un rollback sera effectué à la fin de l'opération
	 */
	@Test
	//@Transactional(propagation = Propagation.NOT_SUPPORTED) // no rollback; changes will be persisted in DB
	@Transactional //insure a rollback after operations are complete; no persistence in DB
	public void deleteUser() {
		
		
		User user =  userRepository.findById(15).orElse(null);
		
		logger.log(Level.INFO, "in UserRepositoryTest.deleteUser() user: " + user);
		
		assertNotNull(user);
		
		assertEquals(1, user.getIsActive());

		LocalDateTime initialupdateDate = user.getNonFormattedUpdateDate();
		
		byte isActive = 0;
		
		user.setIsActive(isActive);
		
		user.setUpdateDate(LocalDateTime.now());
		
		User deletedUser = userRepository.save(user);
		
		assertNotNull(deletedUser);
		
		logger.log(Level.INFO, "in UserRepositoryTest.deleteUser() deletedUser (1): " + deletedUser);
		
		assertEquals(0, user.getIsActive());
		
		LocalDateTime deletedUpdateDate = deletedUser.getNonFormattedUpdateDate();
		
		//vérification que la date de mise à jour a été modifiée
		assertNotEquals(initialupdateDate, deletedUpdateDate);

		//on s'assure que la date de mise à jour ne sera pas la même
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
		
		isActive = 1;
		deletedUser.setIsActive(isActive);
		deletedUser.setUpdateDate(LocalDateTime.now()); //testing default current timestamp 
		
		User reinsertedUser = userRepository.save(deletedUser);
		
		assertNotNull(reinsertedUser);
		
		logger.log(Level.INFO, "in UserRepositoryTest.deleteUser() reinsertedUser (2): " + reinsertedUser);
		
		assertEquals(1, reinsertedUser.getIsActive());
		
		assertNotNull(reinsertedUser.getNonFormattedUpdateDate());
		
		assertNotEquals(deletedUpdateDate, reinsertedUser.getNonFormattedUpdateDate());
		
	}
	
	
}
