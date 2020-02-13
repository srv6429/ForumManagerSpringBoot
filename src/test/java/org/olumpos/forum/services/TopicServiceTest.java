package org.olumpos.forum.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.olumpos.forum.entities.Pair;
import org.olumpos.forum.entities.Post;
import org.olumpos.forum.entities.Topic;
import org.olumpos.forum.entities.User;
import org.olumpos.forum.repositories.PostRepository;
import org.olumpos.forum.repositories.TopicRepository;
import org.olumpos.forum.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 
 * @author daristote
 * 
 * Classe qui permet de tester les différentes méthodes de la class TopicService.java
 * Ces méthode ssont appelées à partir des contrôleurs afin qu'elles puisses interagir avec la base de données
 * en fonction des requêtes et données transmises.
 * 
 * On utilise @MockBean pour les 'repositories' et service (TopicReository, PostRepository) afin d'émuler les accès à la base de données
 * puisqu'il s'agit de tester uniquement les méthodes de la classe TopicService et non les accès à la bd
 * 
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class TopicServiceTest {

	private Logger logger =  Logger.getLogger(TopicServiceTest.class.getName());

	@Autowired
	private TopicService topicService;
	
	@MockBean
	private TopicRepository topicRepository;

	@MockBean
	private PostRepository postRepository;
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	//Méthode de service
	//@Test
	public void dummyMethod() {

		logger.log(Level.INFO, "In TopicServiceTest.dummyMethod: @Test executed " );

	}

	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste l'obtention de la liste de tous les topics actifs ou non
	 */
	@Test
	public void testFindAllTopics() {
		logger.log(Level.INFO, " =============================== > In TopicServiceTest.testFindAllTopics" );
		
		
		List<Topic> allTopics =  new ArrayList<>();
		
		for (int i = 0; i < 10; i++) {
			
			Topic topic =  new Topic();
			topic.setId(i+1);
			topic.setTitle("Topic # " + (i+1));
			topic.setCreatorId(((i%3) +1));
			topic.setCreatorName("Creator " + topic.getCreatorId());
			topic.setIsOpen((byte)((i%2)^1) );
			
			allTopics.add(topic);
		}
		
		logger.log(Level.INFO, " =============================== > In TopicServiceTest.testFindAllTopics: allTopics: " + allTopics );
		
		
		List<Topic> allOpenTopics = allTopics.stream().filter(t -> t.getIsOpen() == 1).collect(Collectors.toList());
		
		logger.log(Level.INFO, " =============================== > In TopicServiceTest.testFindAllTopics: allOpenTopics: " + allOpenTopics );
		
		
		Mockito.doReturn(allTopics)
			.when(topicRepository)
			.findAll(Mockito.any(Sort.class));
		
		Mockito.doReturn(allOpenTopics)
			.when(topicRepository)
			.findAllOpen();
		
		List<Topic> allTopicsTest = topicService.findAllTopics();
		
		assertNotNull(allTopicsTest);
		
		logger.log(Level.INFO, " =============================== > In TopicServiceTest.testFindAllTopics: allTopicsTest: " + allTopicsTest );

		assertEquals(allTopics.size(), allTopicsTest.size());
		
		
		List<Topic> allOpenTopicsTest = topicService.findAllOpenTopics();
		
		assertNotNull(allOpenTopicsTest);
		
		logger.log(Level.INFO, " =============================== > In TopicServiceTest.testFindAllTopics: allOopenTopicsTest: " + allOpenTopicsTest );
		
		assertEquals(allOpenTopics.size(), allOpenTopicsTest.size());
		
		assertTrue(allOpenTopicsTest.size() <= allTopicsTest.size());
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste l'obtention d'un topic en fonction de son identifiant
	 */
	@Test
	public void testFindTopicById() {
		logger.log(Level.INFO, " =============================== > In TopicServiceTest.testFindTopicById" );
		
		Topic topic =  new Topic();
		topic.setId(456);
		topic.setTitle("Mocked topic");
		topic.setCreatorName("ddeisenhower");
		topic.setIsOpen((byte) 1);
		Optional<Topic> optional = Optional.of(topic);
		
		Mockito.doReturn(optional)
		.when(topicRepository)
		.findById(Mockito.any(Integer.class));

		
		Topic topicFound = topicService.findTopicById(topic.getId());
		
		assertNotNull(topicFound);
		
		logger.log(Level.INFO, " =============================== > In TopicServiceTest.testDeleteTopic: topicFound  " + topicFound);

		assertEquals(topic.getId(), topicFound.getId());
		assertEquals(topic.getTitle(), topicFound.getTitle());
		assertEquals(topic.getCreatorName(), topicFound.getCreatorName());
		assertEquals(topic.getIsOpen(), topicFound.getIsOpen());
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste l'ajout d'un topic
	 */
	@Test
	public void testAddTopic() {
		logger.log(Level.INFO, " =============================== > In TopicServiceTest.testAddTopic" );
		
		Topic topic =  new Topic();
		topic.setId(456);
		topic.setTitle("Mocked topic");
		topic.setCreatorName("ddeisenhower");
		topic.setCreatorId(34);
		topic.setIsOpen((byte) 1);
		
		Post post = new Post();
		post.setComment("Mocked comment");
		post.setTopicFK(topic);
		post.setUsername(topic.getCreatorName());
		post.setId(1023);
		post.setIsActive((byte) 1);
		post.setUserId(topic.getCreatorId());
		
		logger.log(Level.INFO, " =============================== > In TopicServiceTest.testAddTopic: topic: " + topic  );
		logger.log(Level.INFO, " =============================== > In TopicServiceTest.testAddTopic: post: " + post );
		
		Mockito.doReturn(topic)
			.when(topicRepository)
			.save(Mockito.any(Topic.class));

		Mockito.doReturn(post)
			.when(postRepository)
			.save(Mockito.any(Post.class));
		
		Pair<Topic, Post> addedPair =  topicService.addTopic(topic.getTitle(), post.getComment(), new User());
		
		logger.log(Level.INFO, " =============================== > In TopicServiceTest.testAddTopic: "  );
		
		assertNotNull(addedPair);
		
		assertEquals(topic, addedPair.getFirst());
		assertEquals(post, addedPair.getSecond());
		
		
		
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste la mise à jour d'un topic
	 */
	@Test
	public void testUpdateTopic() {


		logger.log(Level.INFO, " =============================== > In TopicServiceTest.testDeleteTopic " );
		
		Topic topic =  new Topic();
		topic.setId(763);
		topic.setTitle("Mocked topic");
		topic.setIsOpen((byte) 1);
		topic.setCreatorName("troosevelt");
		topic.setCreatorId(26);
		
		Mockito.doReturn(Optional.of(topic))
			.when(topicRepository)
			.findById(Mockito.any(Integer.class));

		Mockito.doReturn(topic)
			.when(topicRepository)
			.save(Mockito.any(Topic.class));
		
		String newTitle =  "A new titile for a macked topic";
		
		Topic updatedTopic = topicService.updateTopic(topic.getId(), newTitle);
		
		assertNotNull(updatedTopic);

		
		logger.log(Level.INFO, " =============================== > In TopicServiceTest.testDeleteTopic: topicFound  " + updatedTopic);

		assertEquals(topic.getId(), updatedTopic.getId());
		assertEquals(newTitle, updatedTopic.getTitle());
		
	}


	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste la fermeture d'un topic
	 */
	@Test
	public void testDeleteTopic() {


		logger.log(Level.INFO, " =============================== > In TopicServiceTest.testDeleteTopic " );
		
		Topic topic =  new Topic();
		topic.setId(203);
		topic.setTitle("Mocked topic");
		topic.setIsOpen((byte) 1);
		topic.setCreatorName("hstruman");
		topic.setCreatorId(33);
		
		Optional<Topic> optional = Optional.of(topic);
		
		Mockito.doReturn(optional)
		.when(topicRepository)
		.findById(Mockito.any(Integer.class));

		Mockito.doReturn(topic)
		.when(topicRepository)
		.save(Mockito.any(Topic.class));
		
		Topic deletedTopic = topicService.deleteTopic(topic.getId());
		
		assertNotNull(deletedTopic);
		

		
		logger.log(Level.INFO, " =============================== > In TopicServiceTest.testDeleteTopic: topicDeleted  " + deletedTopic);

		assertEquals(topic.getId(), deletedTopic.getId());
		assertEquals(0, deletedTopic.getIsOpen());
		
	}
	
}
