package org.olumpos.forum.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import org.olumpos.forum.repositories.PostRepository;
import org.olumpos.forum.repositories.TopicRepository;
import org.olumpos.forum.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 
 * @author daristote
 * 
 * Classe qui permet de tester les différentes méthodes de la class PostService.java
 * Ces méthode ssont appelées à partir des contrôleurs afin qu'elles puisses interagir avec la base de données
 * en fonction des requêtes et données transmises.
 * 
 * On utilise @MockBean pour les 'repositories' (TopicReository, PostRepository) afin d'émuler les accès à la base de données
 * puisqu'il s'agit de tester uniquement les méthodes de la classe PostService et non les accès à la bd
 * 
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class PostServiceTest {
	
	private Logger logger =  Logger.getLogger(PostServiceTest.class.getName());
		
	@Autowired
	private PostService postService;

	@MockBean
	private TopicRepository topicRepository;
	
	@MockBean
	private PostRepository postRepository;
	
	// *************************************************************************************************************************************************
	// *************************************************************************************************************************************************
	//Méthode test de service
	//@Test
	public void dummyMethod() {

		logger.log(Level.WARNING, "In PostServiceTest.dummyMethod: @Test executed " );

	}

	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste l'obtention de la liste de tous les posts actifs ou non
	 */
	@Test
	public void testFindAllPosts() {
	
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testFindAllPosts" );
		
		
		List<Post> allPosts =  new ArrayList<>();		
		
		for (int i = 0; i < 10; i++) {
			
			Post post = new Post();
			post.setId(i+1);
			post.setComment("Comment " + (i+1));
			post.setUserId((i%3) + 1);
			post.setIsActive((byte) ((i%2)^1) );
			post.setUsername("User " + post.getUserId()) ;
			post.setTopicId((i%4) +1);
		
			allPosts.add(post);
		}
		
		logger.log(Level.INFO, "in postServiceTest.testFindAllPosts() : allPosts  ============= > size: " + allPosts.size());

		List<Post> allActivePosts =  allPosts.stream().filter(p -> p.getIsActive() == 1).collect(Collectors.toList());
		
		logger.log(Level.INFO, "in postServiceTest.testFindAllPosts() : allActivePosts ============= > size: " + allActivePosts.size());
		
		Mockito.doReturn(allPosts)
			.when(postRepository)
			.findAll();
		
		Mockito.doReturn(allActivePosts)
			.when(postRepository)
			.findAllActive((byte) 1);

		
		//test méthote findAllPosts()
		List<Post> allPostsTest = postService.findAllPosts();
		
		assertNotNull(allPostsTest);
		
		logger.log(Level.INFO, "in postServiceTest.testFindAllPosts() : allPostsTest ============= > size: " + allPostsTest.size());
		logger.log(Level.INFO, "in postServiceTest.testFindAllPosts() : allPostsTest ============= >  " + allPostsTest);
		
		
		assertEquals(allPosts.size(), allPostsTest.size());
		
		//test méthote findAllActivePosts()		
		List<Post> allActivePostsTest = postService.findAllActivePosts();
		
		
		logger.log(Level.INFO, "in postServiceTest.testFindAllPosts() : allActivePostsTest ============= > size: " + allActivePostsTest.size());
		logger.log(Level.INFO, "in postServiceTest.testFindAllPosts() : allActivePostsTest============= >  " + allActivePostsTest);

		
		assertEquals(allActivePosts.size(), allActivePostsTest.size());

		assertTrue(allActivePostsTest.size() <= allPostsTest.size());
		
	}	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste l'obtention de la liste de tous les posts actifs ou non pour un topic donné (id)
	 */
	@Test
	public void testFindAllPostsByTopicId() {
	
		logger.log(Level.INFO, " =============================== > In postServiceTest.testFindAllPostsByTopicId" );
		
		
		List<Post> allPosts =  new ArrayList<>();

		
		for (int i = 0; i < 10; i++) {
			
			Post post = new Post();
			post.setId(i+1);
			post.setComment("Comment " + (i+1));
			post.setUserId(i%3);
			post.setIsActive((byte) ((i%2)^1) );
			post.setUsername("User " + post.getUserId()) ;
			post.setTopicId((i % 4) + 1);
		
			allPosts.add(post);
		}
		
		int topicId =  1;
		
		logger.log(Level.INFO, "in postServiceTest.testFindAllPostsByTopicId() : allPosts ============= > size: " + allPosts.size());

		List<Post> allPostsById =  allPosts.stream().filter(p -> (p.getTopicId() == topicId)).collect(Collectors.toList());
		
		logger.log(Level.INFO, "in postServiceTest.testFindAllPostsByTopicId() : allActivePosts by topicId = (" + topicId + ") ============= > size: " + allPostsById.size());

		List<Post> allActivePostsById =  allPosts.stream().filter(p -> (p.getTopicId() == topicId  &&  p.getIsActive() == 1)).collect(Collectors.toList());
		
		logger.log(Level.INFO, "in postServiceTest.testFindAllPostsByTopicId() : allActivePosts by topicId = (" + topicId + ") ============= > size: " + allActivePostsById.size());

		
		Mockito.doReturn(allPostsById)
			.when(postRepository)
			.findAllByTopicId(topicId);
		
		Mockito.doReturn(allActivePostsById)
			.when(postRepository)
			.findAllActiveByTopicId(topicId);
		
		List<Post> allPostsbyIdTest = postService.findAllPostsByTopicId(topicId);
		
		assertNotNull(allPostsbyIdTest);
		
		logger.log(Level.INFO, "in postServiceTest.testFindAllPostsByTopicId() : allPostsTest by topicId = (" + topicId + ") ============= > size: " + allPostsbyIdTest.size());
		logger.log(Level.INFO, "in postServiceTest.testFindAllPostsByTopicId() : allPostsTest by topicId = (" + topicId + ") ============= >  " + allPostsbyIdTest);
		
		
		assertEquals(allPostsById.size(), allPostsbyIdTest.size());
		
		List<Post> allActivePostsByIdTest = postService.findAllActivePostsByTopicId(topicId);
		
		
		logger.log(Level.INFO, "in postServiceTest.testFindAllPostsByTopicId() : allPostsTest by topicId = (" + topicId + ") ============= > size: " + allActivePostsByIdTest.size());
		logger.log(Level.INFO, "in postServiceTest.testFindAllPostsByTopicId() : allPostsTest by topicId = (" + topicId + ") ============= >  " + allActivePostsByIdTest);

		
		assertEquals(allActivePostsById.size(), allActivePostsByIdTest.size());

		assertTrue(allActivePostsByIdTest.size() <= allPostsbyIdTest.size());
		
	}	
		
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste l'obtention d'un post en fonction de son identifiant
	 */
	@Test
	public void testFindPostById() {
		
		Post post = new Post();
		post.setId(456);
		post.setComment("Nouveau commentaire");
		post.setTopicId(83);
		
	
		Mockito.doReturn(Optional.of(post))
			.when(postRepository)
			.findById(post.getId());
		
		Post postTest =  postService.findPostById(post.getId());
		
		assertNotNull(post);
		
		logger.log(Level.INFO, "in testFindPostById : " + post);
					
		assertEquals(post.getId(), postTest.getId());
		assertEquals(post.getComment(), postTest.getComment());
		assertEquals(post.getTopicId(), postTest.getTopicId());
				
				
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste l'ajout d'un post
	 */
	
	@Test
	public void testAddPost() {
		
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testAddPost" );
		
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
		
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testAddPost topic: " + topic  );
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testAddPost post: " + post );
		
		Mockito.doReturn(Optional.of(topic))
			.when(topicRepository)
			.findById(topic.getId());

		Mockito.doReturn(post)
			.when(postRepository)
			.save(Mockito.any(Post.class));
		
		Pair<Topic, Post> pair =  postService.addPost(post.getComment(), post.getTopicId(), null);
		
		assertNotNull(pair);
		
		Topic topic2 =  pair.getFirst();
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testAddPost topic2: " + topic2 );
		
		Post addedPost =  pair.getSecond();
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testAddPost post: " + addedPost );
		
		assertEquals(post.getId(), addedPost.getId());
		assertEquals(post.getComment(), addedPost.getComment());
		assertEquals(post.getTopicId(), addedPost.getTopicId());
		
	}

	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste l'ajout d'un post
	 */
	
	@Test
	public void testAddPost02() {
		
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testAddPost02" );
		
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
		
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testAddPost02 topic: " + topic  );
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testAddPost02 post: " + post );
	

		Mockito.doReturn(post)
			.when(postRepository)
			.save(Mockito.any(Post.class));
		
		Post addedPost =  postService.addPost(post);
		
		assertNotNull(addedPost);
		
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testAddPost02 post: " + addedPost );
		
		assertEquals(post.getId(), addedPost.getId());
		assertEquals(post.getComment(), addedPost.getComment());
		assertEquals(post.getTopicId(), addedPost.getTopicId());
		
		
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui teste la mise à jour d'un post
	 */
	@Test
	public void testUpdatePost() {
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testUpdatePost" );
	
		Post post = new Post();
		post.setComment("Mocked comment");
		post.setTopicId(197);
		post.setUsername("rbhayes");
		post.setUserId(19);
		post.setId(498);
		post.setIsActive((byte) 1);
	
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testUpdatePost: post: " + post );
		
		Topic topic =  new Topic();
		topic.setId(197);
		topic.setTitle("Title");
		
		
		Mockito.doReturn(Optional.of(topic))
			.when(topicRepository)
			.findById(Mockito.any(Integer.class));
		
		Mockito.doReturn(topic)
			.when(topicRepository)
			.save(Mockito.any(Topic.class));
		
		Mockito.doReturn(Optional.of(post))
			.when(postRepository)
			.findById(Mockito.any(Integer.class));
		
		Mockito.doReturn(post)
			.when(postRepository)
			.save(Mockito.any(Post.class));
		
		String comment ="A new comment";
		
		//Post updatedPost = postService.updatePost(post.getId(), comment);
		
		Pair<Topic, Post> pair = postService.updatePost(post.getId(), comment);
		
		assertNotNull(pair);
		Post updatedPost =  pair.getSecond();
		
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testUpdatePost: updatedPost:" + updatedPost );
		
		assertEquals(post.getId(), updatedPost.getId());
		assertEquals(comment, updatedPost.getComment());
		
	}

	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * Méthode qui teste la désactivation d'un post
	 */
	@Test
	public void testDeletePost() {
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testDeletePost" );
	
		Post post = new Post();
		post.setComment("Mocked comment");
		post.setTopicId(197);
		post.setUsername("rbhayes");
		post.setUserId(19);
		post.setId(498);
		post.setIsActive((byte) 1);
	
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testDeletePost: post: " + post );
		
		Optional<Post> optional =  Optional.of(post);
		
		Mockito.doReturn(optional)
			.when(postRepository)
			.findById(Mockito.any(Integer.class));
		
		Mockito.doReturn(post)
			.when(postRepository)
			.save(Mockito.any(Post.class));
		
		Post deletedPost = postService.deletePost(post.getId());
		
		assertNotNull(deletedPost);
		
		logger.log(Level.INFO, " =============================== > In PostServiceTest.testDeletePost: deletedPost:" + deletedPost );
		
		assertEquals(post.getId(), deletedPost.getId());
		assertEquals(post.getComment(), deletedPost.getComment());		
		assertEquals(0, deletedPost.getIsActive());
		
	}	
	
}
