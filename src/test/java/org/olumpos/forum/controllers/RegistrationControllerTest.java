package org.olumpos.forum.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * @author daristote
 * 
 * Classe test qui permet de tester les endpoints de RegistrationController.java
 * On utilise @MockBean pour marquer le PostService utilisé dans le PostController
 * Ainsi on émule les résultats obtenus sans avoir à appeler les vraies méthodes de PostService.java
 * 
 *  On utilise MockMvc pour émuler un appel aux différentes fonctions de RegistrationController qui gèrent les EndPoints
 *
 *	@TODO: erreur lors du test avec la méthode post: à vérifier
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class RegistrationControllerTest {

	//Logger
	private Logger logger = Logger.getLogger(RegistrationControllerTest.class.getName());
	
	@Autowired
	TestRestTemplate restTemplate;
	
	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@MockBean
	private UserService userService;
	
	@MockBean
	private UserValidator userValidator;
	

	@Before
	public void init() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}
	
	
	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************

	//Méthode de service
	//@Test
	public void dummyMethod() {

		logger.log(Level.WARNING, "In RegisterControllerTest.dummyMethod: @Test executed " );

	}

	
	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************
	/**
	 * Méthode qui teste le Endpoint: /register/ avec la méthode GET, et qui doit rediriger l'utilisateur vers la page d'inscription  
	 * 
	 * @throws Exception
	 */
	
	@Test
	public void testGetMethodRegister() throws Exception {
		
		ResponseEntity<String> responseEntity = restTemplate.getForEntity("/register", String.class);
		
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);


		mvc.perform(get("/register"))
				.andExpect(model().attributeExists("user"))
				.andExpect(status().isOk())
				.andExpect(view().name("register"))
				;
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste le Endpoint: /register avec la méthode POST, et qui doit 
	 * enregistrer les informations d'un nouvel utilisteur àa partir de champs  du formulaire
	 * 
	 * @throws Exception
	 */
	
	//Error ??
	//@Test
	public void testPostMethodRegister() throws Exception {


		logger.log(Level.INFO, "testPostMethodRegister =========================> " );

		User user =  new User();
		user.setId(33);
		user.setUsername("hstruman");
		user.setEmail("hstruman");
		user.setPassword("hstruman123");
		
//		Mockito.doNothing().when(userValidator).validate(user, null);

		given(userService.createUser(user)).willReturn(user);
		
		
		mvc.perform(post("/register")
			.with(user("user").password("user123").roles("USER"))
			.accept(MediaType.TEXT_HTML)
			.sessionAttr("user", user))
		//	.andExpect(model().attributeExists("user"))
		//	.andExpect(model().attribute("user", user))
			.andExpect(status().isOk())
			.andExpect(view().name("register"));

//		verify(userValidator, times(1)).validate(user, null);
		verify(userService, times(1)).createUser(user);


	}
	
	
}
