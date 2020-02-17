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

import org.olumpos.forum.services.PostService;
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
 * <br>
 * @author daristote<br>
 * <br>
 * Classe qui contient les 'endpoints' des requêtes http pour les posts<br>
 * <br>
 * Les requêtes lieés aux posts sont gérées par les diffreéntes méthodes définies ici<br>
 * Les paramètres des méthodes contiennent les paramètres transmis dans les requêtes (GET, POST, PUT, DELETE )<br>
 * ainsi qu'une instance de l'interface Model qui permet d'inclure et modifier les attributs de session et des requêtes<br>
 *<br>
 */

//Définir la classe comme un contrôleur
@Controller
//base des uri relatives aux requêtes http pour les posts
@RequestMapping("/") 
//attribut de session après une connexion valide: l'utilisateur connecté et enregistré 
@SessionAttributes("user") 
public class PostController {

	
	//Logger 
	private Logger logger = Logger.getLogger(PostController.class.getName()); 

	
	@Autowired
	private PostService postService;
	
	/**
	 * <br>
	 * Méthode qui permet de récupérer la liste des posts liés à un topic dont l'identitifiant est passé en paramètre dans la requête<br>
	 * @param id : identifiant du topic<br>
	 * @param model : model contenant les atrtibuts de la requête<br>
	 * @return "posts" : une chaîne de caractères (String) correspondantà la vue (i.e. page html vers laquelle l'utilisateur sera redirigé)<br>
	 * et qui sera chargée après le succès de la requête<br>
	 * <br>
	 * TODO: gestion d'une exception si 'id' ne correspond pas à un topic valide<br>
	 */
	
	@GetMapping(value= "/posts/{id}")
	public String getPosts(@PathVariable("id") int id, Model model) {
		
		logger.log(Level.INFO, "in PostController.getPosts: method: GET: topic id: " + id);
		
		
		//@TODO: gestion d'une exception si la liste est vide ou id du topic non valide
		List<Post> posts =  postService.findAllActivePostsByTopicId(id);
		
		logger.log(Level.INFO, "in PostController.getPost:  posts" + posts);

		Topic topic =  postService.findTopicById(id);
		
		model.addAttribute("posts", posts);
		model.addAttribute("topic", topic);
		
		return "posts";

	}

	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * <br>
	 * Méthode qui permet d'enregistrer un nouveau commentaire associé à un topic<br>
	 * <br>
	 * Utilisation de la méthode POST<br>
	 * <br>
	 * @param topicId : l'identifiant du topic<br>
	 * @param comment : le nouveau commentaire à enregistrer<br>
	 * @param model : le model de la requête<br>
	 * @return : ResponseEntity paramétré avec un objet de type JSONResponse qui détermine si la réponse est valide ou non<br>
	 * 		Le résultat est géré sous forme d'objet JSON par jQuery sur la page html et affiche une message d'erreur si échec<br>
	 *<br>
	 */
	
	@PostMapping(value= "/posts/{id}/{comment}")//, produces = "application/json;charset=UTF-8")
	public ResponseEntity<JSONResponse> addPost(@PathVariable("id") int topicId, @PathVariable("comment") String comment, Model model) {
		
		logger.log(Level.INFO, "in PostController.addPost: method: POST: topicId - changed ...: " + topicId + "; comment: " + comment );
		
		//Résultat qui sera renvoyé intégré dans un objet de type ResponseEntity
		JSONResponse result = new JSONResponse();
		
		Map<String, Object> attributes =  model.asMap();
		User user = (User)attributes.get("user");
		
		Pair<Topic, Post> pair =  postService.addPost(comment, topicId, user);
		
		logger.log(Level.INFO, "in PostController.addPost: method: POST saved pair: " + pair);		
		
		if(pair != null && (pair.getFirst() != null && pair.getSecond() != null)) {
			result.setStatus("ok");

		} else {
			result.setStatus("error");
		}
		return ResponseEntity.ok(result);
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet d'enregistrer un commentaire modifié par l'utilisateur<br>
	 * 
	 * Utilisation de la méthode PUT<br>
	 * <br>
	 * @param id : l'identifiant du post<br>
	 * @param comment : le nouveau commentaire modifié à enregistrer<br>
	 * @return : ResponseEntity paramétré avec un objet de type JSONReponse qui détermine si la réponse est valide ou non<br>
	 * 		Le résultat est géré sous forme d'objet JSON par jQuery sur la page html et affiche une message d'erreur si échec<br>
	 * <br>
	 */
	@PutMapping(value= "/posts/{id}/{comment}")//, produces = "application/json;charset=UTF-8")
	public ResponseEntity<JSONResponse> updatePost(@PathVariable("id") int id, @PathVariable("comment") String comment) {
		
		logger.log(Level.INFO, "in PostController.updatePost: method: PUT ... changed ...: post id: " + id + "; comment: " + comment);
		
		
		//Résultat qui sera renvoyé intégré dans un objet de type ResponseEntity
		JSONResponse result = new JSONResponse();
				
		//enregistrement dans la BD
		//Post savedPost =  postService.updatePost(id, comment);
		
		Pair<Topic, Post> pair =  postService.updatePost(id, comment);
		
		logger.log(Level.INFO, "in PostController.updatePost: method: PUT: savedPost : " + pair);
		
		if(pair != null) {
			//succès
			result.setStatus("ok");

		} else {
			//erreur
			result.setStatus("error");
		}
		return ResponseEntity.ok(result);
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet d'effacer un commentaire par l'utilisateur<br>
	 * <br>
	 * Utilisation de la méthode DELETE<br>
	 * <br>
	 * On n'efface pas le post de la bd; on ne fait que le désactivé. Ainsi il ne sera plus apparent dans la liste des posts
	 * <br>
	 * <br>
	 * @param id : l'identifiant du post<br>
	 * @return : ResponseEntity paramétré avec un objet de type JSONReponse qui détermine si la réponse est valide ou non<br>
	 * 		Le résultat est géré sous forme d'objet JSON par jQuery sur la page html et affiche une message d'erreur si échec<br>
	 * <br>
	 */
	@DeleteMapping(value= "/posts/{id}")//, produces = "application/json;charset=UTF-8")
	public ResponseEntity<JSONResponse>  deletePost(@PathVariable("id") int id) {
		
		logger.log(Level.INFO, "in PostController.updatePost: method: DELETE ... changed ...: topic id: " + id);
		
		JSONResponse result = new JSONResponse();
		
		//effacement dans la BD
		Post savedPost =  postService.deletePost(id);
		
		logger.log(Level.INFO, "in PostController.updatePost: method: DELETE ... changed ...: savedPost : " + savedPost);
		
		if(savedPost != null) {
			//réussite
			result.setStatus("ok");

		} else {
			//échec
			result.setStatus("error");
		}
		return ResponseEntity.ok(result);
	}
	
}
