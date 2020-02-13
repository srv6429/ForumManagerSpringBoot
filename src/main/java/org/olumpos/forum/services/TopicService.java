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
 * 
 * @author daristote
 *
 * Classe qui permet la gestion liée à l'ajout ou la modification de topic
 * 
 * Utilisation des interfaces qui permettent l'accès aux tables Topic (TopicRepository) 
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
public class TopicService {

	//Logger
	private Logger logger =  Logger.getLogger(TopicService.class.getName());
	
	//CDI: injection des instance nécessaire
	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	/**
	 * Méthode qui permet d'obtenir la liste de tous les topics de la BD
	 * @return List(Topic): liste des topics
	 */
	
	/**
	 * 
	 * @return List
	 */
	public List<Topic> findAllTopics(){
		
		List<Topic> topics =  topicRepository.findAll(Sort.by(Direction.DESC, "udpate_date"));
		return topics;
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * Méthode qui permet d'obtenir la liste de tous les topics ouverts de la BD
	 * @return List(Topic): liste des topics
	 */
	public List<Topic> findAllOpenTopics(){
		
		List<Topic> topics =  topicRepository.findAllOpen();
		
		return topics;
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui permet de chercher et retourner un topic en fonction de son identifiant
	 * 
	 * @param id : l'identifiant du topic
	 * 
	 * @return Topic: le topic si trouvé, null sinon
	 */
	
	public Topic findTopicById(Integer id) {
		
		return topicRepository.findById(id).orElse(null);
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * Méthode qui permet d'ajouter un nouveau topic accompagné d'un premier post
	 * 
	 * @param title : le titre du nouveau topic
	 * @param comment : le commentaire du premier post
	 * @param user : l'utilisateur connecté qui peut ajouter un nouveau topic
	 * 
	 * @return : Pair (Topic, Post) contenant les nouveaux topic et ost
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
	 * Méthode qui permet de mettre à jour un topic, i.e. le titre
	 * 
	 * @param id : l'identifiant du topic
	 * @param title : le nouveau titre
	 * 
	 * @return Topic: le topic sauvegardé dans la bd
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
	 * Méthode qui permet d'effacer un topic, i.e. initialiser le champ isActive à 0
	 * 
	 * @param id : l'identifiant du topic
	 * 
	 * @return Topic: le topic sauvegardé dans la bd
	 * 
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
	//Non utilisé
	//Utilisé uneiquement pour TopicServiceTest qui est remplacé par TopicServiceTest02
	
	public Topic saveTopic(Topic topic) {
		
		return topicRepository.save(topic);
		
	}
	

}
