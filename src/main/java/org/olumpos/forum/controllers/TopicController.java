package org.olumpos.forum.controllers;

import java.util.List;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.olumpos.forum.entities.JSONResponse;
import org.olumpos.forum.entities.Pair;
import org.olumpos.forum.entities.Post;
import org.olumpos.forum.entities.Topic;
import org.olumpos.forum.entities.User;
import org.olumpos.forum.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


/**
 * 
 * @author daristote
 * 
 * Classe qui contient les 'endpoints' des requêtes http pour les topics
 * 
 * Les requêtes lieés aux topics sont gérées par les diffreéntes méthodes définies ici
 * Les paramètres des méthodes contiennent les paramètres transmis dans les requêtes (GET, POST, PUT, DELETE )
 * ainsi qu'une instance de l'interface Model qui permet d'inclure et modifier les attributs de session et des requêtes
 *
 */
//Définir la classe comme un contrôleur
@Controller
//base des uri relatives aux requêtes http pour les topics
@RequestMapping("/") 
//attribut de session après une connexion valide: l'utilisateur connecté et enregistré 
@SessionAttributes("user") 
public class TopicController{
	
	//Logger
	Logger logger =  Logger.getLogger(TopicController.class.getName());

	//injection des services requis
	@Autowired
	private TopicService topicService;
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui permet de récupérer l'ensmeble des topics ouverts une fois que L'utilisateur est dûment connecté  
	 * @param model : model qui permet d'enregistrer l'attribut 'topics' dans la requête et qui permttra à la vue 'topics' d'afficher la liste sur
	 * la page html 
	 * 
	 * @return : la vue associée à la page html
	 * 
	 * TODO: gestion d'une exception si 'id' ne correspond pas à un topic valide
	 */
	@GetMapping("/topics")
	public String getTopics(Model model){
		
		
		logger.log(Level.INFO, "GetMapping /topics: contains attribute: " + model.containsAttribute("user") + "; user id: " + model.toString());		
		
		//on retourne la liste triée en fonction de la date de mise à jour, de la plus récente à la plus ancienne 
		//l'ordre est toutefois défini dans la requête HQL
//		List<Topic> topics = topicService.findAllOpenTopics()
//				.stream()
//				.sorted((o1, o2)-> o2.getNonFormattedUpdateDate().compareTo(o1.getNonFormattedUpdateDate()))
//				.collect(Collectors.toList());
		
		List<Topic> topics = topicService.findAllOpenTopics();
		
		model.addAttribute("topics", topics);	
		
		return "topics";
	}
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * Méthode qui permet de récupérer un topic en fonction de son identifiant
	 * 
	 * @param id : dentifiant du topic
	 * 
	 * @return une chaîne de cractère correspondant à la vue enregistrée dnas le fichier de configuration et qui est associée 
	 * 	la page html appropriée
	 * 
	 * 
	 * 	For the eyes of testers only
	 * 
	 */
	@GetMapping(value="/topic/{id}")
	public String getTopic(@PathVariable("id") int id){
		
		
		logger.log(Level.INFO, "in GetMapping: getTopic: id: " + id);
		Topic topic =  topicService.findTopicById(id);
		logger.log(Level.INFO, "getting topic: " + topic);
		
		
		return "topic";
	}
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	
	/**
	 * Méthode qui permet d'enregistrer un nouveau thème (topic) ainsi qu'un nouveau commentaire
	 * Un topic ne peut être créé sans un commentaire 
	 * Utilisation de la méthode POST
	 * 
	 * @param title : le titre du nouveau topic
	 * @param comment : le premier commentaire à enregistrer
	 * @param model : le model de la requête
	 * 
	 * @return : ResponseEntity paramétré avec un objet de type JSONResponse qui détermine si la réponse est valide ou non
	 * 		Le résultat est géré sous forme d'objet JSON par jQuery sur la page html et affiche une message d'erreur si échec
	 * 
	 */
	
	@PostMapping(value="/topics/{title}/{comment}")//, produces = "application/json;charset=UTF-8")
	public ResponseEntity<JSONResponse>  addTopic(@PathVariable("title") String title, @PathVariable("comment") String comment, Model model){
	
		logger.log(Level.INFO, "in PostMapping /topics : " + title + "; comment: " + comment);
	
		JSONResponse result = new JSONResponse();
		
		Map<String, Object> attributes =  model.asMap();
		
		User user = (User)attributes.get("user");
		
		logger.log(Level.INFO, "user: " +  user);
		
		Pair<Topic, Post> pair = topicService.addTopic(title, comment, user); 
		
		if(pair != null && pair.getFirst() != null && pair.getSecond() != null) {
			result.setStatus("ok");
		} else {
			result.setStatus("error");
		}
		
		return ResponseEntity.ok(result);

	}
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui permet d'enregistrer un titre modifié pour un topic 
	 * Utilisation de la méthode PUT
	 * 
	 * @param id : l'identifiant du topic
	 * @param title : le titre du topic
	 * 
	 * @return : ResponseEntity paramétré avec un objet de type JSONReponse qui détermine si la réponse est valide ou non
	 * 		Le résultat est géré sous forme d'objet JSON par jQuery sur la page html et affiche une message d'erreur si échec
	 * 
	 */
	@PutMapping( value="/topics/{id}/{title}")//, produces = "application/json;charset=UTF-8")
	public ResponseEntity<JSONResponse> updateTopic(@PathVariable("id") int id, @PathVariable("title") String title){
		
		logger.log(Level.INFO, "in PutMapping: updateTopic: id: " + id  + "; title: " + title);
				
		JSONResponse result = new JSONResponse();

		Topic updatedTopic = topicService.updateTopic(id, title);
		
		logger.log(Level.INFO, "updatedTopic: " +  updatedTopic);
		
		
		if(updatedTopic != null) {
			result.setStatus("ok");

		} else {
			result.setStatus("error");
		}
		return ResponseEntity.ok(result);
	}
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * Méthode qui permet d'effacer un topic par l'utilisateur
	 * Utilisation de la méthode DELETE
	 * On n'efface pas le post de la bd; on ne fait que le désactivé. Ainsi il ne sera plus apparent dans la liste des topics
	 * 
	 * @param id : l'identifiant du topic
	 * 
	 * @return : ResponseEntity paramétré avec un objet de type JSONReponse qui détermine si la réponse est valide ou non
	 * 		Le résultat est géré sous forme d'objet JSON par jQuery sur la page html et affiche une message d'erreur si échec
	 * 
	 */
	
	@DeleteMapping( value="/topics/{id}")//, produces = "application/json;charset=UTF-8")
	public ResponseEntity<JSONResponse>deleteTopic(@PathVariable("id") int id){
		
		logger.log(Level.INFO, "in DeleteMapping: delete Topic with id: " + id);
		
		JSONResponse result = new JSONResponse();
			
		Topic updatedTopic = topicService.deleteTopic(id);

		logger.log(Level.INFO, "in DeleteMapping: updated Topic with : " + updatedTopic);
		
		if(updatedTopic != null) {
			result.setStatus("ok");

		} else {
			result.setStatus("error");
		}
		return ResponseEntity.ok(result);
	}
	
	
}
