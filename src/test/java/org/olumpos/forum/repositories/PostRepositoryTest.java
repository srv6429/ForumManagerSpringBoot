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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.olumpos.forum.entities.Post;
import org.olumpos.forum.entities.Topic;
import org.olumpos.forum.entities.User;
import org.olumpos.forum.repositories.PostRepository;
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
 * Classe qui permet de tester les méthodes de la class PostRepository et les  accès à la base de données<br>
 * <br>
 * Les méthode qui sont annotées avec @Transactional permettent de n'effectuer aucun changement dans la base de données<br>
 * En effet, jumulées avec @Test, l'opération 'rollback' est automatiquement effectuée après l'exécution de la fonction<br>
 * Ainsi, on peut procéder aux tests sans craindre de 'polluer' la bd<br>
 * <br>
 * Ces tests ne peuvent s'effecteur qu'avec une BD de données initialisée contenant le schéma du forum<br>
 * Pour désactiver les tests, on peut commenter les annotations @Test précédant les méthodes<br>
 * Il est toutefois nécessaire de laisse décommenté l'annotation devant dummyMethod(), car une classe de test doit au moins<br> 
 * obtenir une méthode à tester<br>
 *<br>
 */

@ContextConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest//(webEnvironment=WebEnvironment.RANDOM_PORT)
//@DataJpaTest
public class PostRepositoryTest {

	private Logger logger =  Logger.getLogger(PostRepositoryTest.class.getName());
		
	@Autowired
	PostRepository postRepository;

	
	@Test
	public void dummyMethod() {

		logger.log(Level.WARNING, "In PostRepositoryTest.dummyMethod: @Test executed " );

	}
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**<br>
	 * Méthode qui permet de tester l'obtnetion de la liste de tous les posts dans la bd<br>
	 * <br>
	 */
	@Test
	public void testFindAll() {
		
				
		List<Post> allPosts =  postRepository.findAll();

		assertNotNull(allPosts);
		
		logger.log(Level.INFO, "all posts: " + allPosts);
		//au moins un post
		assertTrue(allPosts.size() > 0);
		
		allPosts =  postRepository.findAll(Sort.by(Direction.DESC, "updateDate"));

		assertNotNull(allPosts);
		
		
		logger.log(Level.INFO, "all posts: " + allPosts);
		
		//au moins un post
		assertTrue(allPosts.size() > 0);
		
		List<Post> allActivePosts =  postRepository.findAllActive((byte) 1);

		assertNotNull(allActivePosts);
		
		logger.log(Level.INFO, "all active posts: " + allActivePosts);
		
		assertTrue(allActivePosts.size() <= allPosts.size());
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de tester l'obtnetion de la liste de tous les posts appartenant d'un topic<br>
	 * <br> 
	 */
	@Test
	public void testFindAllByTopicId() {
		
		int topicId = 1;
		List<Post> allPosts =  postRepository.findAllByTopicId(topicId);

		assertNotNull(allPosts);
		
		logger.log(Level.INFO, "all active posts for id : " +topicId +"; posts: " + allPosts);
		
		assertTrue(allPosts.size() > 0);
		
		List<Post> allActivePosts  =  postRepository.findAllActiveByTopicId(1);

		assertNotNull(allActivePosts);
				
		logger.log(Level.INFO, "all active posts: " + allPosts);
		
		assertTrue(allActivePosts.size() <= allPosts.size());
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de tester la recherhe d'un post en fonction de son identifiant<br>
	 * <br>
	 */
	@Test
	public void testFindById() {
		
		Post post =  postRepository.findById(1).orElse(null);
		logger.log(Level.INFO, "in PostRepositoryTest.testFindById(): " + post);
		assertNotNull(post);
		assertEquals(1, post.getId());
		assertEquals("L'Europe agonise sous la canicule", post.getComment());

	}
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de tester l'ajout d'un post dans la table 'post'<br>
	 * <br>
	 * L'annontation @Transactional assure qu'aucun changement ne sera fait, i.e. qu'un rollback sera effectué à la fin de l'opération<br>
	 * <br>
	 */
	@Test
	//@Transactional(propagation = Propagation.NOT_SUPPORTED) // pas de rollback; les changements affecteront la bd
	@Transactional //insure a rollback after operations are complete; no persistence in DB
	public void addPost() {
		
		
		User user = new User();
		user.setId(1);
		
		Topic topic =  new Topic();
		topic.setCreatorFK(user);
		topic.setId(1);
				
		logger.log(Level.INFO, "in PostRepositoryTest.addPost(1): topic ======================>  " + topic);
		

		Post post =  new Post();
		post.setComment("New comment again");
		post.setTopicFK(topic);
		post.setUserFK(user);
		post.setIsActive((byte) 1);
		post.setCreationDate(LocalDateTime.now());
		post.setUpdateDate(LocalDateTime.now());

		logger.log(Level.INFO, "in PostRepositoryTest.addPost(2): post ========================>  " + post);
		
		Post savedPost =  postRepository.save(post);
		
		assertNotNull(savedPost);
		assertNotNull(savedPost.getNonFormattedCreationDate());
		assertNotNull(savedPost.getNonFormattedUpdateDate());
		

		
		logger.log(Level.INFO, "in PostRepositoryTest.addPost(2): savedPost ========================>  " + savedPost);
		
		assertEquals(savedPost.getComment(), post.getComment());
		assertEquals(savedPost.getUserFK().getId(), post.getUserId());
		assertEquals(savedPost.getTopicFK().getId(), post.getTopicId());
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de tester la mise à jour d'un post en fonction de son identifiant<br>
	 * <br>
	 * L'annotation @Transactional assure qu'aucun changement ne sera fait, i.e. qu'un rollback sera effectué à la fin de l'opération<br>
	 * <br>
	 */
	@Test
	//@Transactional(propagation = Propagation.NOT_SUPPORTED) // no rollback; changes will be persisted in DB
	@Transactional //insure a rollback after operations are complete; no persistence in DB
	public void updatePost() {
		
		Post post =  postRepository.findById(13).orElse(null);
		
		logger.log(Level.INFO, "in PostRepositoryTest.updatePost() post: " + post);
		
		assertNotNull(post);
		
		String originalComment =  post.getComment();
		LocalDateTime initialupdateDate = post.getNonFormattedUpdateDate();
		
		String updatedComment = "Nouveau commentaire à mettre à jour";

		post.setComment(updatedComment);
		post.setUpdateDate(LocalDateTime.now());
		
		Post updatedPost = postRepository.save(post);
		
		assertNotNull(updatedPost);
		
		logger.log(Level.INFO, "in POstRepositoryTest.updatePost() updatedPost (1): " + updatedPost);
		
		assertEquals(updatedPost.getComment(), updatedComment);
		
		LocalDateTime updatedUpdateDate = updatedPost.getNonFormattedUpdateDate();
		
		//vérification que la date de mise à jour a été modifiée
		assertNotEquals(initialupdateDate, updatedUpdateDate);


		
		//on s'assure que la date de mise à jour ne sera pas la même
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}

		updatedPost.setComment(originalComment);
		updatedPost.setUpdateDate(LocalDateTime.now());
		
		updatedPost = postRepository.save(updatedPost);
		
		logger.log(Level.INFO, "in PostRepositoryTest.updatePost() updatedPost (2): " + updatedPost);
		
		assertEquals(updatedPost.getComment(), originalComment);
		assertNotEquals(updatedPost.getNonFormattedUpdateDate(), updatedUpdateDate);
	}
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de tester la désactivation d'un post<br>
	 * L'annotation @Transactional assure qu'aucun changement ne sera fait, i.e. qu'un rollback sera effectué à la fin de l'opération<br>
	 *<br>
	 */
	@Test
	//@Transactional(propagation = Propagation.NOT_SUPPORTED) // no rollback; changes will be persisted in DB
	@Transactional //insure a rollback after operations are complete; no persistence in DB
	public void deletePost() {
		
		
		Post post =  postRepository.findById(8).orElse(null);
		
		logger.log(Level.INFO, "in PostRepositoryTest.deletePost() post: "  + post);
		
		assertNotNull(post);
		
		assertEquals(1, post.getIsActive());

		LocalDateTime initialupdateDate = post.getNonFormattedUpdateDate();
		
		
		post.setIsActive((byte) 0);
		post.setUpdateDate(LocalDateTime.now());
		
		Post deletedPost = postRepository.save(post);
		
		assertNotNull(deletedPost);
		
		logger.log(Level.INFO, "in PostRepositoryTest.deletePost() deletedPost (1): " + deletedPost);
		
		assertEquals(0, post.getIsActive());
		
		LocalDateTime deletedUpdateDate = deletedPost.getNonFormattedUpdateDate();
		
		//vérification que la date de mise à jour a été modifiée
		assertNotEquals(initialupdateDate, deletedUpdateDate);
		
		//on s'assure que la date de mise à jour ne sera pas la même
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
		
		deletedPost.setIsActive((byte) 1);
		deletedPost.setUpdateDate(LocalDateTime.now()); //testing default current timestamp 
		
		Post reinsertedPost = postRepository.save(deletedPost);
		
		assertNotNull(reinsertedPost);
		
		logger.log(Level.INFO, "in PostRepositoryTest.deletePost() reinsertedPost (2): " + reinsertedPost);
		
		assertEquals(1, reinsertedPost.getIsActive());
		
		assertNotNull(reinsertedPost.getNonFormattedUpdateDate());
		
		assertNotEquals(deletedUpdateDate, reinsertedPost.getNonFormattedUpdateDate());
		
	}
}
