/**
 *
 *
 * Fonctions javascript utilisées pour répondre aux actions prises sur la page users.html
 * envoie des requête ajax au moyens de méthodes: get, post, put et delete
 * 
 */

jQuery(function(){


	//Clic sur le bouton "Activer/Désactiver"
	jQuery(".user_status_button").click(function(event){

		//Empêcher la soumission du formualire
		event.preventDefault();

		console.log("click: user_status_button");
		
		//récupérer l'identifiant de l'utilisateur sélectionné
		var userId = jQuery(this).attr("id");
		

		console.log("click toggle user id: " + userId);	

		
		var pathname = window.location.pathname;
		var origin   = window.location.origin;  
		var index = pathname.indexOf("/admin/users");
		var contextPath =  origin + pathname.substr(0, index);		
		
		console.log("contextPath(in topics):" + contextPath);
		
		var url = contextPath+"/admin/users/"+ userId
		console.log("url: " + url);
		
		var method =  "put";
		
		var errorMessage = "Une erreur s'est produite lors de la tentative de mise à jour";

		//envoi de la requête AJAX avec la méthode put
		sendAJAXRequest(url, method, errorMessage);

		
	});
});