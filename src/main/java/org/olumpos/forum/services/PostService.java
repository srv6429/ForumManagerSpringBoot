package org.olumpos.forum.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.olumpos.forum.entities.Pair;
import org.olumpos.forum.entities.Post;
import org.olumpos.forum.entities.Topic;
import org.olumpos.forum.entities.User;
import org.olumpos.forum.repositories.PostRepository;
import org.olumpos.forum.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author daristote
 *
 * Classe qui permet de la gestion des accès à la base de données pour les objets de type Post
 * 
 * Utilisation des interfaces qui permettent l'accès aux tables Topic (TopicRepository) et Post (PostRepository)
 * Les instances qui implémentent les interfaces sont automatiquement injectées (CDI: Context Dependency Injetion) lors de l'instanciation de la classe
 * 
 * La classe est annotée avec @Transactional, ce qui assure que toutes les méthodes qui accèdent à la base de données le sont dans 
 * le cadre d'une transaction
 * 
 * On aurait pu annoter seulement les méthodes qui nécessitent une transaction, i.e. qui modifie les entreés des tables
 * Mais il est plus simple d'annoter la classe 
 * 
 */

@Service
@Transactional
public class PostService {

	private Logger logger =  Logger.getLogger(PostService.class.getName());
	
	//Interface qui permet d'accéder à la table Post de la BD
	@Autowired
	private PostRepository postRepository;
	
	//Interface qui permet d'accéder à la table Topic de la BD
	@Autowired
	private TopicRepository topicRepository;

	
	/**
	 * 
	 * Fonction qui permet d'obtenir tous les 'posts' de la table Post
	 * 
	 * @return: Une Collection de type List contenant toutes les entrées
	 */
	public List<Post> findAllPosts(){
		logger.log(Level.INFO, "in postService.findAllPosts" );

		List<Post> posts =  postRepository.findAll();
		
		logger.log(Level.INFO, "in postService.findAllPosts: posts size: " + posts.size() );
		
		return posts;
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * 
	 * Fonction qui permet d'obtenir tous les 'posts' actifs, i.e. dont le champ 'isActive' = 1, de la table Post
	 * 
	 * @return: Une 'List' (interface Java) de type 'ArrayList' contenant toutes les entrées
	 */
	public List<Post> findAllActivePosts(){
		
		logger.log(Level.INFO, "in postService.findAllActivePosts" );
		
		List<Post> posts =  postRepository.findAllActive((byte) 1);
		
		logger.log(Level.INFO, "in postService.findAllActivePosts: posts size: " + posts.size() );
		
		return posts;
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * Fonction qui permet d'obtenir tous les 'posts' actifs de la table Post qui appartiennent à un 'topic' en particulier
	 *  
	 * @param topicId: l'identifiant du 'topic'
	 * @return : la liste de tous les 'posts' actifs du 'topic'
	 */
	
	public List<Post> findAllPostsByTopicId(int topicId){
		
		logger.log(Level.INFO, "in postService.findAllPostsByTopicId : topicId ==> " + topicId );
		List<Post> posts =  postRepository.findAllByTopicId(topicId);
		
		logger.log(Level.INFO, "in postService.findAllPostsByTopicId: posts size: " + posts.size() );
		
		return posts;
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	
	/**
	 * Fonction qui permet d'obtenir tous les 'posts' de la table Post mais qui appartienne à un Topic en particulier
	 *  
	 * @param topicId: L'identifiant du Topic
	 * @return : la liste de tous les posts du topic
	 */
	
	public List<Post> findAllActivePostsByTopicId(int topicId){
		
		logger.log(Level.INFO, "in postService.findAllActivePostsByTopicId : topicId ==> " + topicId );
		
		List<Post> posts =  postRepository.findAllActiveByTopicId(topicId);
		
		logger.log(Level.INFO, "in postService.findAllActivePostsByTopicId: posts size: " + posts.size() );
		
		
		return posts;
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * Fonction qui permet de trouver une 'post' en fonction de son identifiant (id)
	 * @param id : l'identifiant du 'post'
	 * @return : une intance de la classe 'Post' contenant tous les détails du 'post'
	 */
	
	public Post findPostById(Integer id) {
		
		logger.log(Level.INFO, "in postService.findPostById : Id ==> " + id);
		
		Optional<Post> optional =  postRepository.findById(id);
		logger.log(Level.INFO, "in postService.findPostById : post found:  ==> " + optional.orElse(null));
		
		return postRepository.findById(id).orElse(null);
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	
	/**
	 * Fonction qui permet de trouver un 'topic' en fonction de son identifiant (id)
	 * @param id : l'identifiant du 'topic'
	 * @return : une intance de la classe 'Topic' contenant tous les détails du 'topic'
	 */
	public Topic findTopicById(int id) {
		
		logger.log(Level.INFO, "in postService.findTopicById : Id ==> " + id);
		Optional<Topic> optional =  topicRepository.findById(id);
		logger.log(Level.INFO, "in postService.findPostById : post found:  ==> " + optional.orElse(null));
		
		return topicRepository.findById(id).orElse(null);
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * Fonction qui permet d'ajouter un 'post' dans la BD
	 * @param comment : le cmmentaire du 'post'
	 * @param topicId : l'identifiant du topic auquel le 'post' est associé
	 * @param user : l'utilisateur qui est l'auteur du 'post'
	 * @return : Le 'post' ajouté à la BD , null si l'opération échoue
	 */
	
	public Pair<Topic, Post> addPost(String comment, Integer topicId, User user) {

		logger.log(Level.INFO, "in postService.addPost : comment ==> " + comment + "; topicId: " + topicId + "; user: " + user);
		
		//On optient d'abord le 'topic', null si échec ou non existant
		Topic topic = topicRepository.findById(topicId).orElse(null);
	
		logger.log(Level.INFO, "in postService.addPost : topic ====== >" + topic);
		
		//si le topic est trouvé
		if(topic != null) {
			//On crée une instance de la classe Post et on ajoute les informations
			Post post = new Post();
			post.setComment(comment);
			post.setTopicFK(topic);
			post.setUserFK(user);
			post.setIsActive((byte)1);
			
			topic.setUpdateDate(LocalDateTime.now());
			
			//persister dans la BD et retourné l'objet
			Post updatedPost =postRepository.save(post); 
			Topic updatedTopic =  topicRepository.save(topic);
			  
			return new Pair<Topic, Post>(updatedTopic, updatedPost);
		}
		
		//null si échec
		return null;
		

	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * 	Fonction qui permet d'ajouter un 'post' dans la BD
	 * 	variant de la pcédente mais le paramtère est un objet de type Post
	 * @param post : Le post contenant les infos
	 * @return : le post tel que sauvegardé dans la BD
	 */
	public Post addPost(Post post) {
		
		logger.log(Level.INFO, "in postService.addPost : post ==> " + post);
		
		return postRepository.save(post);
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * Fonction qui permet de mettre à jour le commentaire d'un 'post' 
	 * 
	 * @param id : l'identifiant du 'post'
	 * @param comment : le nouveau commentaire modifé par l'utilisateur
	 * @return : Le 'post' tel que sauvegardé dans la BD
	 */
	
	public Pair<Topic, Post> updatePost(Integer id, String comment) {
		
		logger.log(Level.INFO, "in postService.updatePost : id + " + id + "; comment ==> " + comment);
		
		//On recherche le post
		Post post =  postRepository.findById(id).orElse(null);
		
		logger.log(Level.INFO, "in postService.updatePost : post " + post);
		
		//Si le post est toruvé, on modifie le commentaire, la date de modification et on persiste
		if(post != null) {
			
			Topic topic =  topicRepository.findById(post.getTopicId()).orElse(null);
			if(topic != null) {
				
				post.setComment(comment);
				post.setUpdateDate(LocalDateTime.now());
				//sauvegarde
				Post updatedPost =  postRepository.save(post);
				topic.setUpdateDate(LocalDateTime.now());
				
				Topic updateTopic =  topicRepository.save(topic);
				
				return new Pair<Topic, Post>(updateTopic, updatedPost);
			}
			
		}
		
		//retour null si non trouvé
		return null;

	}
	
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * Fontion qui permet d'effacer un post. En fait on ne fait que modifier le champ 'isActive' qu'on met à 0
	 *  Ainsi un 'post' pourra être réactiver par une administrateur au besoin
	 * @param id : l'identifiant du 'post'
	 * @return : le post modifié et sauvegardé dans la BD
	 */
	
	public Post deletePost(Integer id) {

		Post post =  postRepository.findById(id).orElse(null);
		
		logger.log(Level.INFO, "in posetService.deletePost: post: " + post);
		
		if(post != null) {
			post.setIsActive((byte)0); 
			post.setUpdateDate(LocalDateTime.now());
			
			return postRepository.save(post);
		}
		
		return post;
		

	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	
	//@TODO: verify if not used before deleting it
	//Used in PostServiceTest: to remove first
	public Post save(Post post) {
		
		return postRepository.save(post);
	}
}
