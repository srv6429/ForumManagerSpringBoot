package org.olumpos.forum.controllers;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.olumpos.forum.entities.User;
import org.olumpos.forum.services.UserService;
import org.olumpos.forum.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * @author daristote
 * 
 * Classe test qui permet de tester les endpoints de UserController.java
 * On utilise @MockBean pour marquer le UserService utilisé dans le User Controller
 * Ainsi on émule les résultats obtenus sans avoir à appeler les vraies méthdoes de PostService.java
 * 
 *  On utilise MockMvc pour émuler un appel aux différentes fonctions de UserController qui gèrent les EndPoints
 *
 */


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class UserControllerTest {

	private Logger logger = Logger.getLogger(UserControllerTest.class.getName());
	
	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@MockBean
	private UserService userService;
	
	@Before
	public void init() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}
	
	
	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************
	//Méthode test de service
	//@Test
	public void dummyMethod() {

		logger.log(Level.WARNING, "In UserControllerTest.dummyMethod: @Test executed " );

	}

	
	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************
	/**
	 * Méthode qui teste le Endpoint: /profile/ avec la méthode GET, et qui doit rediriger 
	 * l'utilisateur connecté vers la page profil
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetProfile() throws Exception {
		
		logger.log(Level.INFO, "testGetMethodProfile =========================> " );
		
		User user =  new User();
		user.setId(18);
		user.setUsername("usgrant");
		user.setEmail("usgrant@whitehouse.us");
		user.setPassword("usgrant123");
		
		mvc.perform(get("/profile")
				.with(user("user").password("user123").roles("USER"))
				.accept(MediaType.TEXT_HTML)
				.sessionAttr("user", user)
				)
				.andExpect (model().attribute("user", user))
				.andExpect(model().attributeExists("user"))
				.andExpect(status().isOk())
				.andExpect(view().name("profile"))
				;
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste le Endpoint: /profile/ avec la méthode POSTT, et qui doit enregistrer les modifications
	 * que l'utilisateur connecté a effectuée sur la page profil
	 * La méthode put n'est pas reconnue par le formulaire html
	 * On aurait pu utiliser javascript et AJAX pour envoyer la requête avec la méthode PUT
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostMethodProfile() throws Exception {


		logger.log(Level.INFO, "testPostMethodProfile =========================> " );

		User user =  new User();
		user.setId(18);
		user.setUsername("jfkennedy");
		user.setEmail("jfkennedy@whitehouse.us");
		user.setPassword("jfkennedy123");
		
		User updatedUser =  new User();
		updatedUser.setId(18);
		updatedUser.setUsername("lbjohnson");
		updatedUser.setEmail("lbjohnson@whitehouse.us");
		updatedUser.setPassword("lbjohnson123");


		given(userService.updateUser(user, user)).willReturn(updatedUser);
		
		//put non reconnue avec le formulaire thymeleaf
//		mvc.perform(put("/profile")
		mvc.perform(post("/profile")
		.with(user("user").password("user123").roles("USER"))
		.accept(MediaType.TEXT_HTML)
		.sessionAttr("user", user))
		.andExpect (model().attribute("user", user))
		.andExpect(model().attributeExists("user"))
		.andExpect(status() .isOk()); //.isFound());
		
		verify(userService, times(1)).updateUser(user, user);

	}

	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * Méthode qui teste le Endpoint: //dmin/users avec la méthode GET, et qui doit retourner la liste de tous les utilsiteurs
	 * Seuls les adminitrateurs peuvent avoir accès à cette page
	 * 
	 * @throws Exception
	 */

	@Test
	public void testGetUsers() throws Exception {
		

		logger.log(Level.INFO, "testGetUsers =========================> " );

		User user1 =  new User();
		user1.setId(245);
		user1.setUsername("user1");
		user1.setCreationDate(LocalDateTime.of(2017, 11, 30, 13, 29));
		user1.setUpdateDate(LocalDateTime.now());
		
		User user2 = new User();
		user2.setId(278);
		user2.setUsername("user2");
		user2.setCreationDate(LocalDateTime.of(2017, 11, 30, 13, 29));
		user2.setUpdateDate(LocalDateTime.now());

		User user =  new User();
		user.setId(28);
		user.setUsername("wwilson");
		user.setEmail("wwilson@whitehouse.us");
		user.setPassword("wwilson123");
		user.setCreationDate(LocalDateTime.of(2017, 11, 30, 13, 29));
		user.setUpdateDate(LocalDateTime.now());
	
		
		given(userService.findAllUsers()).willReturn(Arrays.asList(user1, user2));
		
		this.mvc.perform(get("/admin/users")
				.with(user("admin").password("admin123").roles("USER", "ADMIN")) //admin seulement
				.accept(MediaType.TEXT_HTML)
				.sessionAttr("user", user))
				.andExpect(status().isOk())
				.andExpect(view().name("users"))
				.andExpect(model().attributeExists("user"))
				.andExpect(model().attribute("users", hasSize(2)));

		verify(userService, times(1)).findAllUsers();
	}
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste le Endpoint: /admin/users/{id} avec la méthode PUT et qui doit 
	 * mettre à jour le statut d'un utilisateur, i.e. en l'avtivant u le désactivant 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testToggleUser() throws Exception {
		
		logger.log(Level.INFO, "testToggleUser =========================> " );

		User user =  new User();
		user.setId(18);
		user.setUsername("usgrant");
		user.setEmail("usgrant@whitehouse.us");
		user.setPassword("usgrant123");
		
		given(userService.toggleUser(user.getId())).willReturn(user);
		
		mvc.perform(put("/admin/users/"+ user.getId())
		.with(user("admin").password("admin123").roles("USER", "ADMIN"))
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status", is("ok")));
		
		verify(userService, times(1)).toggleUser(user.getId());
		
	}
	
	
	
}

//*************************************************************************************************************************************************
//*************************************************************************************************************************************************
//*************************************************************************************************************************************************
//*************************************************************************************************************************************************

