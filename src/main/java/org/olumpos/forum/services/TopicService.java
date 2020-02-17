package org.olumpos.forum.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.olumpos.forum.entities.Pair;
import org.olumpos.forum.entities.Post;
import org.olumpos.forum.entities.Topic;
import org.olumpos.forum.entities.User;
import org.olumpos.forum.repositories.PostRepository;
import org.olumpos.forum.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <br>
 * @author daristote<br>
 *<br>
 * Classe qui permet la gestion des requêtes liées à l'ajout ou la modification de topic<br>
 * <br>
 * Utilisation des interfaces qui permettent l'accès aux tables Topic (TopicRepository) <br>
 * Les instances qui implémentent les interfaces sont automatiquement injectées (CDI: Context Dependency Injetion) lors de l'instanciation de la classe<br>
 * <br>
 * La classe est annotée avec @Transactional, ce qui assure que toutes les méthodes qui accèdent à la base de données le sont dans <br>
 * le cadre d'une transaction<br>
 * <br>
 * On aurait pu annoter seulement les méthodes qui nécessitent une transaction, i.e. qui modifie les entreés des tables<br>
 * Mais il est plus simple d'annoter la classe <br>
 * <br>
 * <br>
 * 
 */

//Annotée comme Service
@Service
@Transactional
public class TopicService {

	//Logger
	private Logger logger =  Logger.getLogger(TopicService.class.getName());
	
	//CDI: injection des instance nécessaire
	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * <br>
	 * Méthode qui permet d'obtenir la liste de tous les topics de la BD<br>
	 * <br>
	 * @return List(Topic): liste des topics<br>
	 * <br>
	 */
	
	public List<Topic> findAllTopics(){
		
		List<Topic> topics =  topicRepository.findAll(Sort.by(Direction.DESC, "udpate_date"));
		return topics;
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * <br>
	 * Méthode qui permet d'obtenir la liste de tous les topics ouverts de la BD<br>
	 * <br>
	 * @return List(Topic): liste des topics<br>
	 * <br>
	 */
	public List<Topic> findAllOpenTopics(){
		
		List<Topic> topics =  topicRepository.findAllOpen();
		
		return topics;
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet de chercher et retourner un topic en fonction de son identifiant<br>
	 * <br>
	 * @param id : l'identifiant du topic<br>
	 * <br>
	 * @return Topic: le topic si trouvé, null sinon<br>
	 * <br>
	 */
	
	public Topic findTopicById(Integer id) {
		
		return topicRepository.findById(id).orElse(null);
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * <br>
	 * Méthode qui permet d'ajouter un nouveau topic accompagné d'un premier post<br>
	 * <br>
	 * @param title : le titre du nouveau topic<br>
	 * @param comment : le commentaire du premier post<br>
	 * @param user : l'utilisateur connecté qui peut ajouter un nouveau topic<br>
	 * <br>
	 * @return : Pair (Topic, Post) contenant les nouveaux topic et ost<br>
	 * <br>
	 */
	
	public Pair<Topic, Post> addTopic(String title, String comment, User user) {

		Topic topic =  new Topic();
		topic.setTitle(title);
		topic.setIsOpen((byte)1);
		topic.setCreatorFK(user);
		
		//Enregistrement du topic
		Topic savedTopic = topicRepository.save(topic);
		
		logger.log(Level.INFO, "Saved topic: " + savedTopic);

		//Enregistrement du post
		Post post =  new Post();
		post.setComment(comment);
		post.setTopicFK(savedTopic);
		post.setUserFK(user);
		post.setIsActive((byte)1);
		
		
		Post savedPost =  postRepository.save(post);
		
		logger.log(Level.INFO, "Saved post: " + savedPost);
		
		//return savedTopic != null && savedPost != null;
		return new Pair<Topic, Post>(savedTopic, savedPost);
		
		

	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * <br>
	 * Méthode qui permet de mettre à jour un topic, i.e. le titre<br>
	 * <br>
	 * @param id : l'identifiant du topic<br>
	 * @param title : le nouveau titre<br>
	 * <br>
	 * @return Topic: le topic sauvegardé dans la bd<br>
	 * <br>
	 * 
	 */
	public Topic updateTopic(Integer id, String title) {

		Topic topic =  topicRepository.findById(id).orElse(null);
		
		logger.log(Level.INFO, "getting topic: " + topic);
		
		if(topic != null) {
		
			//modifier le titre et la date de mise à jour
			topic.setTitle(title);
			topic.setUpdateDate(LocalDateTime.now());
		
			Topic updatedTopic = topicRepository.save(topic);
			
			logger.log(Level.INFO, "updatedTopic: " +  updatedTopic);
			
			return updatedTopic;
		}
		
		return null;

		

	}
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet d'effacer un topic, i.e. initialiser le champ isActive à 0<br>
	 * <br>
	 * @param id : l'identifiant du topic<br>
	 * <br>
	 * @return Topic: le topic sauvegardé dans la bd<br>
	 * <br>
	 */
	public Topic deleteTopic(Integer id) {

		logger.log(Level.INFO, "in TopicService.deleteTopic topic id: " + id);
		Topic topic =  topicRepository.findById(id).orElse(null);
		
		logger.log(Level.INFO, " topic found : " + topic);
		
		if(topic != null) {
			topic.setIsOpen((byte)0);
			topic.setUpdateDate(LocalDateTime.now()); 
			//	topic.setUpdateDate(null); //identique
		
			return topicRepository.save(topic);
		}
		return null;
	}
		
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************


}
