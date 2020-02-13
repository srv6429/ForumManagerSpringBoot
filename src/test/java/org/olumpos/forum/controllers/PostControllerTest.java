package org.olumpos.forum.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import org.olumpos.forum.entities.Pair;
import org.olumpos.forum.entities.Post;
import org.olumpos.forum.entities.Topic;
import org.olumpos.forum.entities.User;
import org.olumpos.forum.services.PostService;
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
 * Classe test qui permet de tester les endpoints de PostController.java
 * On utilise @MockBean pour marquer le PostService utilisé dans le PostController
 * Ainsi on émule les résultats obtenus sans avoir à appeler les vraies méthdoes de PostService.java
 * 
 *  On utilise MockMvc pour émuler un appel aux différentes fonctions de PostController qui gèrent les EndPoints
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public class PostControllerTest {


	@Autowired
	TestRestTemplate restTemplate;


	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@MockBean
	private PostService postService;

	
	private Logger logger = Logger.getLogger(PostControllerTest.class.getName());

	@Before
	public void init() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}
	
	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************

	//Test de service
//	@Test
	public void dummyMethod() {

		logger.log(Level.WARNING, "In PingControllerTests.dummyMethod: @Test executed " );

	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste le Endpoint: /posts/{id} avec la méthode GET, et qui doit retourner la liste de tous els posts pour un topic (id) donné
	 * 
	 * @throws Exception
	 */
	
	
	@Test
	public void testAllPosts() throws Exception {
		
		ResponseEntity<String> responseEntity = restTemplate.getForEntity("/posts/1", String.class);
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);	
		
		User user =  new User();
		user.setId(28);
		user.setUsername("wwilson");
		user.setEmail("wwilson@whitehouse.us");
		user.setPassword("wwilson123");
		user.setCreationDate(LocalDateTime.of(1914, 11, 3, 13, 29));
		user.setUpdateDate(LocalDateTime.of(1915, 3, 4, 13, 0));
		
		Topic topic =  new Topic();
		topic.setId(345);
		topic.setTitle("Topic title 1");
		topic.setCreatorFK(user);
		topic.setCreationDate(LocalDateTime.of(20, 11, 30, 13, 29));
		topic.setUpdateDate(LocalDateTime.now());
		
		Post post1 =  new Post();
		post1.setId(567);
		post1.setComment("Comment 1");
		post1.setUserFK(user);
		post1.setCreationDate(LocalDateTime.of(2017, 11, 30, 13, 29));
		post1.setUpdateDate(LocalDateTime.now());
		
		Post post2 =  new Post();
		post2.setId(678);
		post2.setComment("Comment 2");
		post2.setUserFK(user);		
		post2.setCreationDate(LocalDateTime.of(2017, 11, 30, 13, 29));
		post2.setUpdateDate(LocalDateTime.now());
		
		given(postService.findAllActivePostsByTopicId(topic.getId())).willReturn(Arrays.asList(post1, post2));
		given(postService.findTopicById(topic.getId())).willReturn(topic);
		
		this.mvc.perform(get("/posts/" + topic.getId())
				.with(user("user").password("user123").roles("USER"))
				.accept(MediaType.TEXT_HTML)
				.sessionAttr("user", user))
				.andExpect(status().isOk())
				.andExpect(view().name("posts"))
				.andExpect(model().attributeExists("topic"))
				.andExpect(model().attributeExists("posts"))
				.andExpect(model().attribute("posts", hasSize(2)));

		verify(postService, times(1)).findAllActivePostsByTopicId(topic.getId());
		verify(postService, times(1)).findTopicById(topic.getId());
		
		
		
	}

	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************
	/**
	 * Méthode qui teste le Endpoint: /posts/{id}/{comment} avec la méthode POST et qui doit 
	 * enregistrer un nouveau post pour un topic donné 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddPost() throws Exception {


		Post post = new Post();
		post.setId(1);
		post.setComment("New comment");
		post.setTopicId(1);
		post.setUserId(1);
		

		Topic topic =  new Topic();
		topic.setId(1);
		
		Pair<Topic, Post> pair = new Pair<>(topic, post);
		
		logger.log(Level.INFO, "testAddPost test =========================> " + post);

		given(postService.addPost(post.getComment(), post.getTopicId(), null)).willReturn(pair);

				
				mvc
				.perform(post("/posts/" + post.getTopicId() + "/" + post.getComment())
						.with(user("user").password("user123").roles("USER"))
					.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("ok")))
				.andReturn();


		verify(postService, times(1)).addPost(post.getComment(), post.getTopicId(), null);

	}
	
	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************
	/**
	 * Méthode qui teste le Endpoint: /posts/{id}/{comment} avec la méthode PUT et qui doit 
	 * enregistrer un post dont le commentaire a été modifié par son auteur 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdatePost() throws Exception {


		Post post = new Post();
		post.setId(1);
		post.setComment("New comment");
		post.setTopicId(1);
		post.setUserId(1);
		
		Topic topic =  new Topic();
		topic.setId(1);
		
		Pair<Topic, Post> pair = new Pair<>(topic, post);
		logger.log(Level.INFO, "testUpdatePost test =========================> " + pair);

		given(postService.updatePost(post.getId(), post.getComment())).willReturn(pair);

				
				mvc
				.perform(put("/posts/" + post.getId() + "/" + post.getComment())
				.with(user("user").password("user123").roles("USER"))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("ok")))
				.andReturn();


		verify(postService, times(1)).updatePost(post.getId(), post.getComment());

	}
	
	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************
	/**
	 * Méthode qui teste le Endpoint: /posts/{id} avec la méthode DELETE et qui doit 
	 * enregistrer un post qui a été désactivé par son auteur  
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeletePost() throws Exception {


		Post post = new Post();
		post.setId(1);
		post.setComment("New comment");
		post.setTopicId(1);
		post.setUserId(1);
		

		logger.log(Level.INFO, "testDeletePost test =========================> " + post);

		given(postService.deletePost(post.getId())).willReturn(post);
				
				mvc
				.perform(delete("/posts/" + post.getId())
				.with(user("user").password("user123").roles("USER"))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("ok")))
				.andReturn();

		verify(postService, times(1)).deletePost(post.getId());

	}
}
