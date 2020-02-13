/**
 *
 *
 * Fonctions javascript utilisées pour répondre aux actions prises sur la page topics.html
 * Envoient des requêtes ajax au moyens de méthodes: get, post, put et delete
 * 
 */

jQuery(function(){
	
	//************************************************************************************************************************
	
	//Clic sur "Ajouter" un topic
	jQuery("#new-topic-button").click(function(){

		
		console.log("click add topic ");
	
		//afficher le formulaire
		jQuery("#new_topic_wrapper").show();

		//désactiver le bouton "Ajouter"
		jQuery("#add_topic").prop("disabled", true);

	
	});
	
	//************************************************************************************************************************
	
	//Clic sur "Annuler" ajout de topic
	jQuery("#new-topic-cancel").click(function(){

		console.log("click cancel new topic");
	
		//Cacher le formulaire
		jQuery("#new_topic_wrapper").hide();

		//réactiver le bouton "Ajouter"
		jQuery("#add_topic").prop("disabled", false);
		
	});
	
	//************************************************************************************************************************
	
	jQuery("#new-topic-send").click(function(event){
		
		//stop submitting form, we will submit manually
		event.preventDefault();
		console.log("Not submitting");
		
		//fire_ajax_submit();
		//récupérer les valeurs de schamps
		var topicTitle = jQuery("#new-topic-title").val();
	//	var postTitle = jQuery("#new_post_title").val();
		var postComment = jQuery("#new-post-comment").val();
		
		console.log("title: " + topicTitle + "; comment: " + postComment);
		
		//encoder le titre du topic
		var topicTitleEncoded =  topicTitle.replace(/\n/, "<br />");
		topicTitleEncoded =  encodeURIComponent(topicTitleEncoded);
		console.log("topic title encoded: " + topicTitleEncoded);
		
		console.log("topic title encoded: " + topicTitleEncoded);
		
		//encoder le commentaire du post
		var postCommentEncoded =  postComment.replace(/\n/, "<br />");
		postCommentEncoded =  encodeURIComponent(postCommentEncoded);
		console.log("postComment encoded: " + postCommentEncoded);
		

		var pathname = window.location.pathname;
		var origin   = window.location.origin;  
		var index = pathname.indexOf("/topics");
		var contextPath =  origin + pathname.substr(0, index);		
		
		console.log("contextPath(in topics):" + contextPath);
		
		
		var url = contextPath+ "/topics/"+topicTitleEncoded+"/"+postCommentEncoded;
		
		console.log("url: " + url);
		
		var method =  "post";
		var errorMessage = "Une erreur est survenue lors de la tentative d'enregisrer le nouveau thème.";
		
		//envoi de la requête ajax avec pa méthode ost
		sendAJAXRequest(url, method, errorMessage);
		
		
		
	});
	
	//************************************************************************************************************************
	
	//Clic sur le bouton "Modifier" un topic
	jQuery(".update_topic_button").click(function(event){

		event.preventDefault();
		console.log("Not submitting");
		
		//récupérer l'identifiant du topic sélectionné
		var topicId = jQuery(this).attr("id");

		console.log("update_button click: topicId: " + topicId);

		console.log("click update: id: " + topicId);
		
		//rendre invisibles les boutons d'édition
		//jQuery(this).parent().parent().removeClass("visible");
		
		console.log("id: " + jQuery(this).parent().attr("id"));
				
		//jQuery(this).parent().hide();
		jQuery(".update_topic_button").prop("disabled", true);
		jQuery(".delete_topic_button").prop("disabled", true);

		jQuery(".topic_editor_form").each(function(i, o){
			
			console.log("id: " + jQuery(this).attr("id"));
			if(jQuery(this).attr("id") == topicId){
				jQuery(this).show();
			}
			
		});

		//désactiver le bouton "Ajouter" un topic
		jQuery("#new_topic_button").prop("disabled",true);

	});
	
	//************************************************************************************************************************
	
	//Clic sur le bouton "Annuler" la modification
	jQuery(".cancel_update_topic").click(function(event){

		event.preventDefault();
		console.log("Not submitting");
		
		//récupérer l'identifiant du topic
		var topicId = jQuery(this).attr("id");

		console.log("click cancel post id: " + topicId);

		console.log("click cancel: id: " + topicId);
		
		
		jQuery(".topic_editor_form").hide();
		//réactiver les boutons
		jQuery(".update_topic_button").prop("disabled", false);
		jQuery(".delete_topic_button").prop("disabled", false);
		jQuery("#new_topic_button").prop("disabled", false);
	});
	
	
	//************************************************************************************************************************
	
	//Clic sur le bouton "Envoyer" le topic modifié
	jQuery(".send_update_topic").click(function(event){

		//empĉher la soumission du formulaire
		event.preventDefault();

		//récupérer l'identifiant du topic
		var topicId = jQuery(this).attr("id"); //getting the post id

		//récupérer le titre modifié		
		var topicTitle =  jQuery(this).siblings("input[id="+topicId+"]").val();

		console.log("topicId id: " + topicId + "; topicTitle: " + topicTitle);
		console.log("topicId id: " + topicId + "; topicTitle: " + topicTitle);
//

		//encoder le titre pour le transmettre dans l'url
		var topicTitleEncoded =  topicTitle.replace(/\n/, "<br />");
		topicTitleEncoded =  encodeURIComponent(topicTitleEncoded);

		console.log("title encoded: " + topicTitleEncoded);		
		console.log("title encoded: " + topicTitleEncoded);
		
		var pathname = window.location.pathname;
		var origin   = window.location.origin;  
		var index = pathname.indexOf("/topics");
		var contextPath =  origin + pathname.substr(0, index);		
		
		console.log("contextPath(in topics):" + contextPath);
		
		var url = contextPath + "/topics/"+topicId + "/"+topicTitleEncoded;
		console.log("url: " + url);
		
		var method =  "put";
		
		var errorMessage = "Une erreur s'est produite lors de la tentative de mise à jour";
		
		//envoi de la requête ajax avec la méthode put
		sendAJAXRequest(url, method, errorMessage);
						
	});
	
	//************************************************************************************************************************
	
	//Clic sur le bouton "Retirer" le topic
	jQuery(".delete_topic_button").click(function(event){
		
		//empĉher la soumission du formulaire
		event.preventDefault();

		var response = confirm("Désirez-vous vraiment retirer cette discussion?");

		if(response == false){
			return;	
		}

		//récupérer l'identifiant du topic
		var topicId = jQuery(this).attr("id"); //getting the post id

		console.log("click delete topic id: " + topicId );
		
		var pathname = window.location.pathname;
		var origin   = window.location.origin;  
		var index = pathname.indexOf("/topics");
		var contextPath =  origin + pathname.substr(0, index);		
		
		console.log("contextPath(in topics):" + contextPath);
		
		var url = contextPath+ "/topics/"+topicId;
		
		console.log("url: " + url);
		
		var method =  "delete";
		
		var errorMessage = "Une erreur s'est produite lors de la tentative de mise à jour";
		
		//envoi de la requête ajax avec la méthode delete
		sendAJAXRequest(url, method, errorMessage);
			
	});

	
});



