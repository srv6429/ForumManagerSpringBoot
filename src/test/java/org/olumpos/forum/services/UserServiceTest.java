package org.olumpos.forum.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.olumpos.forum.entities.User;
import org.olumpos.forum.repositories.UserRepository;
import org.olumpos.forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * <br>
 * @author daristote<br>
 * <br>
 * Classe qui permet de tester les différentes méthodes de la class UserService.java<br>
 * Ces méthode ssont appelées à partir des contrôleurs afin qu'elles puisses interagir avec la base de données<br>
 * en fonction des requêtes et données transmises.<br>
 * <br>
 * On utilise @MockBean pour le 'repository' (UserReository) afin d'émuler les accès à la base de données<br>
 * puisqu'il s'agit de tester uniquement les méthodes de la classe UserService et non les accès à la bd<br>
 * <br>
 *<br>
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserServiceTest {

	//Logger
	private Logger logger = Logger.getLogger(UserServiceTest.class.getName());

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;


	@Test
	public void dummyMethod() {

		logger.log(Level.WARNING, "In UserServiceTest.dummyMethod: @Test executed ");

	}

	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de tester l'obtention d'un utilisateur en fonction de son courriel ou pseudonyme<br>
	 * <br>
	 */
	
	@Test
	public void testFindByUsernameOrEmail() {

		logger.log(Level.WARNING, "In UserServiceTest.testFindByUsernameOrEmail ");

		User user = new User();
		user.setId(16);
		user.setUsername("alincoln");
		user.setEmail("alincoln@whitehouse.us");
		user.setPassword("alincoln123");

		Optional<User> optional = Optional.of(user);

		logger.log(Level.WARNING, "In UserServiceTest.testFindByUsernameOrEmail ============== > user: " + user);

		Mockito.doReturn(optional)
			.when(userRepository)
			.findByUsernameOrEmail(Mockito.anyString());

		User foundUser = userService.findByUsernameOrEmail(user.getUsername()).orElse(null);

		assertNotNull(foundUser);

		logger.log(Level.WARNING,
				"In UserServiceTest.testFindByUsernameOrEmail ============== > foundUser: " + foundUser);

		assertEquals(user.getUsername(), foundUser.getUsername());
		assertEquals(user.getEmail(), foundUser.getEmail());
		assertEquals(user.getPassword(), foundUser.getPassword());

	}

	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de tester l'obtention d'un utilisateur en fonction de son identifiant<br>
	 * <br>
	 */
	@Test
	public void testFindById() {

		logger.log(Level.WARNING, "In UserServiceTest.testFindById ");

		User user = new User();
		user.setId(16);
		user.setUsername("alincoln");
		user.setEmail("alincoln@whitehouse.us");
		user.setPassword("alincoln123");

		Optional<User> optional = Optional.of(user);

		logger.log(Level.WARNING, "In UserServiceTest.testFindById ============== > user: " + user);

		Mockito.doReturn(optional).when(userRepository).findById(Mockito.any(Integer.class));

		User foundUser = userService.findUserById(user.getId());

		assertNotNull(foundUser);

		logger.log(Level.WARNING, "In UserServiceTest.testFindById ============== > foundUser: " + foundUser);

		assertEquals(user.getId(), foundUser.getId());
		assertEquals(user.getUsername(), foundUser.getUsername());
		assertEquals(user.getEmail(), foundUser.getEmail());
		assertEquals(user.getPassword(), foundUser.getPassword());

	}

	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de créer un nouvel utilisateur<br>
	 * <br> 
	 */
	@Test
	public void testCreateUser() {
		logger.log(Level.WARNING, "In UserServiceTest.testCreateUser ");

		User user = new User();
		user.setId(12);
		user.setUsername("ztaylor");
		user.setEmail("ztaylor@whitehouse.us");
		user.setPassword("ztaylor123");

		logger.log(Level.WARNING, "In UserServiceTest.testCreateUser: =====================> user: " + user);

		Mockito.doReturn(user).when(userRepository).save(Mockito.any(User.class));

		User createdUser = userService.createUser(user);

		assertNotNull(createdUser);

		logger.log(Level.WARNING,
				"In UserServiceTest.testCreateUser: ===================== > createdUser:  " + createdUser);

		assertEquals(user.getId(), createdUser.getId());
		assertEquals(user.getUsername(), createdUser.getUsername());
		assertEquals(user.getEmail(), createdUser.getEmail());
		assertEquals(user.getPassword(), createdUser.getPassword());

	}

	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************

	/**
	 * <br>
	 * Méthode qui permet de mettre à jour les informations d'un utilisateur<br> 
	 * L'utilisateur doit être connecté et donc avoir le statut USER ou ADMIN<br>
	 * <br>
	 */
	@Test
	@WithMockUser(username="admin",roles={"USER","ADMIN"})
	public void testUpdateUser() {
		logger.log(Level.WARNING, "In UserServiceTest.testUpdateUser ");

		User user = new User();
		user.setId(12);
		user.setUsername("ztaylor");
		user.setEmail("ztaylor@whitehouse.us");
		user.setPassword("ztaylor123");

		logger.log(Level.WARNING, "In UserServiceTest.testUpdateUser: =====================> user: " + user);

		user.setUsername("jagarfield");
		user.setEmail("jagarfield@whitehouse.us");
		user.setPassword("jagarfield123");
		
		logger.log(Level.WARNING,
				"In UserServiceTest.testUpdateUser: =====================> user To Update: " + user);

		Mockito.doReturn(user)
			.when(userRepository)
			.save(Mockito.any(User.class));

	//	SecurityContextHolder.getContext().setAuthentication(this.authentication);

		User updatedUser = userService.updateUser(user);

		assertNotNull(updatedUser);

		logger.log(Level.WARNING,
				"In UserServiceTest.testUpdateUser: ===================== > updatedUser:  " + updatedUser);

		assertEquals(user.getId(), updatedUser.getId());
		assertEquals(user.getUsername(), updatedUser.getUsername());
		assertEquals(user.getEmail(), updatedUser.getEmail());
		assertEquals(user.getPassword(), updatedUser.getPassword());

	}
	

	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************

	/**
	 * <br>
	 * Méthode qui permet de mettre à jour les informations d'un utilisateur<br> 
	 * L'utilisateur doit être connecté et donc avoir le statut USER ou ADMIN<br>
	 * <br>
	 */

	@Test
	@WithMockUser(username="user",roles={"USER"})
	public void testUpdateUser02() {
		logger.log(Level.WARNING, "In UserServiceTest.testUpdateUser02 ");

		User user = new User();
		user.setId(12);
		user.setUsername("ztaylor");
		user.setEmail("ztaylor@whitehouse.us");
		user.setPassword("ztaylor123");

		logger.log(Level.WARNING, "In UserServiceTest.testUpdateUser: =====================> user: " + user);

		User userToUpdate = new User();
		userToUpdate.setId(user.getId());
		userToUpdate.setUsername("jagarfield");
		userToUpdate.setEmail("jagarfield@whitehouse.us");
		userToUpdate.setPassword("jagarfield123");

		logger.log(Level.WARNING,
				"In UserServiceTest.testUpdateUser: =====================> userToUpdate: " + userToUpdate);

		Mockito
			.doReturn(userToUpdate)
			.when(userRepository)
			.save(Mockito.any(User.class));

	//	SecurityContextHolder.getContext().setAuthentication(this.authentication);

		User updatedUser = userService.updateUser(user);

		assertNotNull(updatedUser);

		logger.log(Level.WARNING,
				"In UserServiceTest.testUpdateUser: ===================== > updatedUser:  " + updatedUser);

		assertEquals(userToUpdate.getId(), updatedUser.getId());
		assertEquals(userToUpdate.getUsername(), updatedUser.getUsername());
		assertEquals(userToUpdate.getEmail(), updatedUser.getEmail());
		assertEquals(userToUpdate.getPassword(), updatedUser.getPassword());

	}
	
	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************

	/**
	 * <br>
	 * Méthode qui permet de mettre à jour les informations d'un utilisateur<br>
	 * <br> 
	 * L'utilisateur doit être connecté et donc avoir le statut USER ou ADMIN<br>
	 * <br>
	 */

	@Test
	@WithMockUser(username="user",roles={"USER"})
	public void testUpdateUser03() {
		logger.log(Level.WARNING, "In UserServiceTest.testUpdateUser03 ");

		User user = new User();
		user.setId(2);
		user.setUsername("jadams");
		user.setEmail("jadams@whitehouse.us");
		user.setPassword("jadams123");

		logger.log(Level.WARNING, "In UserServiceTest.testUpdateUser: =====================> user: " + user);

		User userToUpdate = new User();
		userToUpdate.setId(3);
		userToUpdate.setUsername("tjefferson");
		userToUpdate.setEmail("tjefferson@whitehouse.us");
		userToUpdate.setPassword("tjefferson123");

		logger.log(Level.WARNING,
				"In UserServiceTest.testUpdateUser: =====================> userToUpdate: " + userToUpdate);

		Mockito
			.doReturn(userToUpdate)
			.when(userRepository)
			.save(Mockito.any(User.class));

	//	SecurityContextHolder.getContext().setAuthentication(this.authentication);

		User updatedUser = userService.updateUser(user, userToUpdate);

		assertNotNull(updatedUser);

		logger.log(Level.WARNING,
				"In UserServiceTest.testUpdateUser: ===================== > updatedUser:  " + updatedUser);

		assertEquals(userToUpdate.getId(), updatedUser.getId());
		assertEquals(userToUpdate.getUsername(), updatedUser.getUsername());
		assertEquals(userToUpdate.getEmail(), updatedUser.getEmail());
		assertEquals(userToUpdate.getPassword(), updatedUser.getPassword());

	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************

	/**
	 * <br>
	 * Méthode qui permet de mettre à jour les informations d'un utilisateur<br> 
	 * en l'activant/désactivant<br>
	 * <br>
	 */
	@Test
	@WithMockUser(username="admin",roles={"ADMIN"})
	public void testToggleUser() {
		
		logger.log(Level.WARNING, "In UserServiceTest.testToggleUser ");

		User user = new User();
		user.setId(12);
		user.setUsername("ztaylor");
		user.setEmail("ztaylor@whitehouse.us");
		user.setPassword("ztaylor123");
		user.setIsActive((byte) 1);

		logger.log(Level.WARNING, "In UserServiceTest.testToggleUser: =====================> user: " + user);
		
		Optional<User> optional =  Optional.of(user);
		Mockito
			.doReturn(optional)
			.when(userRepository)
			.findById(Mockito.any(Integer.class));
		
		Mockito
			.doReturn(user)
			.when(userRepository)
			.save(Mockito.any(User.class));
		
		//désactiver		
		User updatedUser = userService.toggleUser(user.getId());
		assertNotNull(updatedUser);

		logger.log(Level.WARNING,
				"In UserServiceTest.testToggleUser: ===================== > updatedUser (1):  " + updatedUser);
		
		assertEquals(user.getId(), updatedUser.getId());
		assertEquals(0, updatedUser.getIsActive());
		
		//réactiver		
		updatedUser = userService.toggleUser(updatedUser.getId());
		assertNotNull(updatedUser);

		logger.log(Level.WARNING,
				"In UserServiceTest.testToggleUser: ===================== > updatedUser (2):  " + updatedUser);
		
		assertEquals(user.getId(), updatedUser.getId());
		assertEquals(1, updatedUser.getIsActive());

	}
	
}
