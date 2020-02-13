package org.olumpos.forum.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.olumpos.forum.entities.Pair;
import org.olumpos.forum.entities.Post;
import org.olumpos.forum.entities.Topic;
import org.olumpos.forum.entities.User;
import org.olumpos.forum.services.TopicService;
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


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 
 * @author daristote
 * 
 * Classe test qui permet de tester les endpoints de TopicController.java
 * On utilise @MockBean pour marquer le TopicService utilisé dans le Topic Controller
 * Ainsi on émule les résultats obtenus sans avoir à appeler les vraies méthdoes de PostService.java
 * 
 *  On utilise MockMvc pour émuler un appel aux différentes fonctions de TopicController qui gèrent les EndPoints
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration(classes = {BlogManagerApplication.class, WebSecurityConfig.class, WebConfig.class})

//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
//@WebMvcTest(controllers = {TopicController.class})
public class TopicControllerTest {

	private Logger logger = Logger.getLogger(TopicControllerTest.class.getName());
	
	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@MockBean
	private TopicService topicService;

	@Before
	public void init() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	
	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************
	//Méthode de service
	@Test
	public void dummyMethod() {

		logger.log(Level.WARNING, "In PingControllerTests.dummyMethod: @Test executed " );

	}

	
	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************
	/**
	 * Méthode qui teste le Endpoint: /topics avec la méthode GET, et qui doit retourner la liste de tous les topics ouverts
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAllTopics() throws Exception {
		
		User user =  new User();
		user.setId(28);
		user.setUsername("wwilson");
		user.setEmail("wwilson@whitehouse.us");
		user.setPassword("wwilson123");
		user.setCreationDate(LocalDateTime.of(2017, 11, 30, 13, 29));
		user.setUpdateDate(LocalDateTime.now());
		
		Topic topic1 =  new Topic();
		topic1.setTitle("Topic title 1");
		topic1.setCreatorFK(user);
		
		
		Topic topic2 = new Topic();
		topic2.setTitle("Topic title 2");
		topic2.setCreatorFK(user);		
		
		given(topicService.findAllOpenTopics()).willReturn(Arrays.asList(topic1, topic2));
		
		this.mvc.perform(get("/topics")
				.with(user("user").password("user123").roles("USER"))
				.accept(MediaType.TEXT_HTML)
				.sessionAttr("user", user))
				.andExpect(status().isOk())
				.andExpect(view().name("topics"))
				.andExpect(model().attributeExists("topics"))
				.andExpect(model().attribute("topics", hasSize(2)));

		verify(topicService, times(1)).findAllOpenTopics();
		
	}


	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************
	/**
	 * Méthode qui teste le Endpoint: /topics/{title}/{comment} avec la méthode POST et qui doit 
	 * enregistrer un nouveau topic ainsi qu'un premier post 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testPostOneTopic() throws Exception {

		Topic topic = new Topic();
		// topic.setId(0);
		topic.setTitle("New topic title");
		topic.setCreationDate(null);
		topic.setUpdateDate(null);

		topic.setIsOpen((byte) 1);

		Post post = new Post();
		post.setComment("New comment");
	

		logger.log(Level.INFO, "topic test =========================> " + topic);


		Pair<Topic, Post> pair =  new Pair<>(topic, post);
		
		given(topicService.addTopic(topic.getTitle(), post.getComment(), null)).willReturn(pair);
				
				mvc
				.perform(post("/topics/" + topic.getTitle() + "/" + post.getComment())
						.with(user("user").password("user123").roles("USER"))
					.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("ok")))
				.andReturn();

		verify(topicService, times(1)).addTopic(topic.getTitle(), post.getComment(), null);

	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste le Endpoint: /topics/{id}/{title} avec la méthode PUT et qui doit 
	 * mettre à jour et enregistrer  un topic dont le titre a été modifié par son auteur 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateTopic() throws Exception {

		Topic topic = new Topic();
		topic.setId(1);
		topic.setTitle("Topic title updated");

		logger.log(Level.INFO, "topic test =========================> " + topic);

		given(topicService.updateTopic(topic.getId(), topic.getTitle())).willReturn(topic);


				mvc
				.perform(put("/topics/"+ topic.getId() + "/" + topic.getTitle())
						.with(user("user").password("user123").roles("USER"))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("ok")))
				.andReturn();
				
		verify(topicService, times(1)).updateTopic(topic.getId(), topic.getTitle());

	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste le Endpoint: /topics/{id} avec la méthode DELETE et qui doit 
	 * mettre à jour et enregistrer un topic qui a été fermé par son auteur  
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeleteTopic() throws Exception {

		Topic topic = new Topic();
		topic.setId(1);
		topic.setTitle("Topic title updated");

		logger.log(Level.INFO, "in deleteTopicTest =========================> " + topic);

		given(topicService.deleteTopic(topic.getId())).willReturn(topic);
				
				mvc
				.perform(delete("/topics/"+ topic.getId())
				.with(user("user").password("user123").roles("USER"))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("ok")))
				.andReturn();
		
		verify(topicService, times(1)).deleteTopic(topic.getId());

	}
}
