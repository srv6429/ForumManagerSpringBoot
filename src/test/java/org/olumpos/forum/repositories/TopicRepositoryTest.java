package org.olumpos.forum.repositories;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.olumpos.forum.entities.Post;
import org.olumpos.forum.entities.Topic;
import org.olumpos.forum.entities.User;
import org.olumpos.forum.repositories.TopicRepository;
import org.olumpos.forum.services.TopicServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * <br>
 * @author daristote<br>
 *<br>
 * Classe qui permet de tester les méthodes de la class TopicRepository et les  accès à la base de données<br>
 * <br>
 * Les méthode qui sont annotées avec @Transactional permettent de n'effectuer aucun changement dans la base de données<br>
 * En effet, jumulées avec @Test, l'opération 'rollback' est automatiquement effectuée après l'exécution de la fonction<br>
 * Ainsi, on peut procéder aux tests sans craindre de 'polluer' la bd<br>
 * <br>
 * Ces tests ne peuvent s'effecteur qu'avec une BD de données initialisée contenant le schéma du forum<br>
 * Pour désactiver les tests, on peut commenter les annotations @Test précédant les méthodes<br>
 * Il est toutefois nécessaire de laisse décommenté l'annotation devant dummyMethod(), car une classe de test doit au moins<br> 
 * obtenir une méthode à tester<br>
 * <br>
 *<br>
 */

@ContextConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest//(webEnvironment=WebEnvironment.RANDOM_PORT)
//@DataJpaTest
public class TopicRepositoryTest {

	//Logger
	private Logger logger =  Logger.getLogger(TopicRepositoryTest.class.getName());
	
	@Autowired
	TopicRepository topicRepository;

	
	@Test
	public void dummyMethod() {

		logger.log(Level.WARNING, "In PingControllerTests.dummyMethod: @Test executed " );

	}

	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de tester l'obtnetion de la liste de tous les topics dans la bd<br>
	 * <br>
	 */
	@Test
	public void testFindAll() {
		
				
		List<Topic> topics =  topicRepository.findAll();

		assertNotNull(topics);
		
		logger.log(Level.INFO, "all topics: " + topics);
		
		//au moins un topic
		assertTrue(topics.size() > 0);
		
		topics =  topicRepository.findAll(Sort.by(Direction.DESC, "updateDate"));

		assertNotNull(topics);
		
		logger.log(Level.INFO, "all topics: " + topics);
		
		//au moins un topic
		assertTrue(topics.size() > 0);
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**<br>
	 * Méthode qui permet de tester l'obtnetion de la liste de tous les topics ouverts dans la bd<br>
	 * <br>
	 */
	@Test
	public void testFindAllOpen() {
		
				
		List<Topic> allOpenTopics =  topicRepository.findAllOpen();

		assertNotNull(allOpenTopics);
		
		logger.log(Level.INFO, "all open topics: " + allOpenTopics);
		
		List<Topic> allTopics =  topicRepository.findAll();

		assertNotNull(allTopics);
		
		logger.log(Level.INFO, "all topics: " + allTopics);
		
		//au moins un post
		assertTrue(allTopics.size() > 0);
		
		assertTrue(allOpenTopics.size() <= allTopics.size());
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * <br>
	 * Méthode qui permet de tester la recherhe d'un topic en fonction de son identifiant<br>
	 * <br>
	 */
	@Test
	public void testFindById() {
		
		Topic topic =  topicRepository.findById(1).orElse(null);
		logger.log(Level.INFO, "in TopicRepositoryTest.testFindById(): " + topic);
		assertNotNull(topic);
		assertEquals(1, topic.getId());
		assertEquals("La météo", topic.getTitle());

	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de tester l'ajout d'un topic<br>
	 * @Transactional assure qu'aucun ajout ne sera fait, i.e. qu'un rollback sera effectué à la fin de l'opération<br>
	 * <br>
	 */
	@Test
	//@Transactional(propagation = Propagation.NOT_SUPPORTED) // no rollback; changes will be persisted in DB
	@Transactional //insure a rollback after operations are complete; no persistence in DB
	public void addTopic() {
		
		
		User user = new User();
		user.setId(1);
		Topic topic = new Topic();
		topic.setTitle("Un nouveau topic again");
		topic.setCreatorFK(user);
		topic.setIsOpen((byte) 1);
		
		logger.log(Level.INFO, "in TopicRepositoryTest.addTopic(1): topic ======================>  " + topic);
		
		Topic savedTopic = topicRepository.save(topic);
		
		assertNotNull(savedTopic);
		assertNotNull(savedTopic.getNonFormattedCreationDate());
		assertNotNull(savedTopic.getNonFormattedUpdateDate());
		
		
		logger.log(Level.INFO, "in TopicRepositoryTest.addTopic(2): savedTopic ========================>  " + savedTopic);
		
		assertEquals(savedTopic.getTitle(), topic.getTitle());
		assertEquals(savedTopic.getCreatorFK().getId(), topic.getCreatorId());
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de tester la mise à jour d'un topic<br>
	 * @Transactional assure qu'aucun changementne sera fait, i.e. qu'un rollback sera effectué à la fin de l'opération<br>
	 * <br>
	 */
	@Test
	//@Transactional(propagation = Propagation.NOT_SUPPORTED) // no rollback; changes will be persisted in DB
	@Transactional //insure a rollback after operations are complete; no persistence in DB
	public void updateTopic() {
		
		
		Topic topic =  topicRepository.findById(8).orElse(null);
		
		logger.log(Level.INFO, "in TopicRepositoryTest.updateTopic() topic: " + topic);
		
		assertNotNull(topic);
		
		String originalTitle =  topic.getTitle();
		LocalDateTime initialupdateDate = topic.getNonFormattedUpdateDate();
		
		String updatedTitle = "Nouveau titre";

		topic.setTitle(updatedTitle);
		topic.setUpdateDate(LocalDateTime.now());
		
		Topic updatedTopic = topicRepository.save(topic);
		
		assertNotNull(updatedTopic);
		
		logger.log(Level.INFO, "in TopicRepositoryTest.updateTopic() updatedTopic (1): " + updatedTopic);
		
		assertEquals(updatedTopic.getTitle(), updatedTitle);
		LocalDateTime updatedUpdateDate = updatedTopic.getNonFormattedUpdateDate();
		
		//vérification que la date de mise à jour a été modifiée
		assertNotEquals(initialupdateDate, updatedUpdateDate);
		
		
		//redonner la valeur initiale du titre
		//on s'assure que la date de mise à jour ne sera pas la même
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
		
		updatedTopic.setTitle(originalTitle);
		updatedTopic.setUpdateDate(LocalDateTime.now());
		
		updatedTopic = topicRepository.save(updatedTopic);
		logger.log(Level.INFO, "in TopicRepositoryTest.updateTopic() updatedTopic (2): " + updatedTopic);
		
		assertEquals(updatedTopic.getTitle(), originalTitle);
		assertNotEquals(updatedUpdateDate, updatedTopic.getNonFormattedUpdateDate());
	}
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de tester la fermeture d'un topic<br>
	 * @Transactional assure qu'aucun changement ne sera fait, i.e. qu'un rollback sera effectué à la fin de l'opération<br>
	 * <br>
	 */
	@Test
	//@Transactional(propagation = Propagation.NOT_SUPPORTED) // no rollback; changes will be persisted in DB
	@Transactional //insure a rollback after operations are complete; no persistence in DB
	public void deleteTopic() {
		
		
		Topic topic =  topicRepository.findById(8).orElse(null);
		
		logger.log(Level.INFO, "in TopicRepositoryTest.deleteTopic() topic: " + topic);
		
		assertNotNull(topic);
		
		assertEquals(1, topic.getIsOpen());

		LocalDateTime initialupdateDate = topic.getNonFormattedUpdateDate();
		
		
		topic.setIsOpen((byte) 0);
		topic.setUpdateDate(LocalDateTime.now());
		
		Topic deletedTopic = topicRepository.save(topic);
		
		assertNotNull(deletedTopic);
		
		logger.log(Level.INFO, "in TopicRepositoryTest.deleteTopic() deletedTopic (1): " + deletedTopic);
		
		assertEquals(0, topic.getIsOpen());
		
		LocalDateTime deletedUpdateDate = deletedTopic.getNonFormattedUpdateDate();
		
		//vérification que la date de mise à jour a été modifiée
		assertNotEquals(initialupdateDate, deletedUpdateDate);

		//on s'assure que la date de mise à jour ne sera pas la même
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
		
		deletedTopic.setIsOpen((byte) 1);
		deletedTopic.setUpdateDate(LocalDateTime.now()); //testing default current timestamp 
		
		Topic reinsertedTopic = topicRepository.save(deletedTopic);
		
		assertNotNull(reinsertedTopic);
		
		logger.log(Level.INFO, "in TopicRepositoryTest.deleteTopic() reinsertedTopic (2): " + reinsertedTopic);
		
		assertEquals(1, reinsertedTopic.getIsOpen());
		
		assertNotNull(reinsertedTopic.getNonFormattedUpdateDate());
		
		assertNotEquals(deletedUpdateDate, reinsertedTopic.getNonFormattedUpdateDate());
		
	}
	
}

