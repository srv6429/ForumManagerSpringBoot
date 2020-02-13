/**
 *
 *
 * Fonctions javascript utilisées pour répondre aux actions prises sur la page posts.html
 * envoie des requête ajax au moyens de méthodes: get, post, put et delete
 * 
 */


jQuery(function(){
        
	
	//clic sur le bouton "Modifier"
	jQuery(".update_post_button").click(function(e){
		//on ne soumet par le formulaire
		e.preventDefault();

		//on récupère l'idenitifiant du post sélectionné
		var postId = jQuery(this).attr("id");

		//console.log("click update post id: " + postId);
		console.log("click update post id: " + postId);

		//on remplace le contenu du post par le formulaire d'éition
		jQuery(".comment").each(function(i, o){
			
			if(jQuery(this).attr("id") == postId){
				jQuery(this).hide();
			}
		});
		
		jQuery(".post_form").each(function(i, o){
			if(jQuery(this).attr("id") == postId){
				jQuery(this).show();
			}
		});
		
		
		//désactiver le bouton "Ajouter" en haut à droite 
		jQuery("#new_post_button").prop("disabled", true);
		jQuery(".update_post_button").prop("disabled", true);
		jQuery(".delete_post_button").prop("disabled", true);

	});


	//*******************************************************************************

	jQuery(".cancel_update_button").click(function(event){
	//Clic sur le boton "Annuler"


		console.log("click cancel");
		//empêcher la soumission du formulaire
		event.preventDefault();

		jQuery(".post_form").hide();
		
		jQuery(".comment").show();
		//réactiver les boutons "Ajouter"

		jQuery("#new_post_button").prop("disabled", false);
		jQuery(".update_post_button").prop("disabled", false);
		jQuery(".delete_post_button").prop("disabled", false);
	});
	
	

	//***************************************************************************************************************
	
	//Clic sur "Enregistrer" les modifications apportées au post
	jQuery(".send_update_button").click(function(e){

		//empêcher la soumission du formulaire
		e.preventDefault();
		
		//récupérer l'identifiant du post
		var postId = jQuery(this).attr("id"); //getting the post id
		
		console.log("postId: " + postId);

		console.log(jQuery(this).parent().siblings("form").find("textarea").val());
		
		//récupérer le titre et le commentaires modifié(s) 

		var postComment = jQuery(this).parent().siblings("form").find("textarea").val();
//
//		//encoder le commentaire i.e. caracètres spéciaux pour les passer dans une url
		var postCommentEncoded =  postComment.replace(/\n/g, "<br />");
		postCommentEncoded = encodeURIComponent(postCommentEncoded);
		console.log(postCommentEncoded);
		console.log(postCommentEncoded);
		
		var pathname = window.location.pathname;
		var origin   = window.location.origin;  
		var index = pathname.indexOf("/posts");
		var contextPath =  origin + pathname.substr(0, index);		
		
		console.log("contextPath(in posts):" + contextPath);

		var url = contextPath + "/posts/"+postId+"/"+postCommentEncoded;
		console.log("url: " + url);
		console.log("url: " + url);
		
		var method =  "put";
		
		var errorMessage = "Une erreur s'est produite lors de la tentative de mise à jour";
		
		//envoi de la requête ajax avec la méthdoe put
		sendAJAXRequest(url, method, errorMessage);

	});	
		//***************************************************************************************************************
		
		
		
		//Clic sur "Retirer" un post
		jQuery(".delete_post_button").click(function(event){

			//empêcher la soumission du formulaire
			event.preventDefault();

			var response = confirm("Désirez-vous vraiment retirer ce commentaire?");

			if(response == false){
				return;	
			}
			
			//récupérer l'identifiant du post sélectionné
			var postId = jQuery(this).attr("id");
			
			console.log("click delete post id: " + postId);	

			var pathname = window.location.pathname;
			var origin   = window.location.origin;  
			var index = pathname.indexOf("/posts");
			var contextPath =  origin + pathname.substr(0, index);			
			
			console.log("contextPath(in posts):" + contextPath);
			
			var url = contextPath + "/posts/"+postId;
			console.log("url: " + url);
			
			console.log("url: " + url);
			
			var method =  "delete";
			
			var errorMessage = "Une erreur s'est produite lors de la tentative de mise à jour";
			
			//envoi de la reuête ajax
			sendAJAXRequest(url, method, errorMessage);
			
		});

	//***************************************************************************************************************
		
		//Clic sur bouton "Ajouter" un nouveau post
		jQuery("#new_post_button").click(function(event){
			//empêcher la soumission du formulaire
			event.preventDefault();

			var topicId = jQuery("#new_post_form").find("#topicId").val();
			
			console.log("click add new post: topicId: " + topicId);
			console.log("click add post: topicId: " + topicId);
		
			//afficher le formulaire d'ajout
			jQuery("#new_post_form").show();
			
			//désactiver le bout on d'ajout
			jQuery("#add_post_button").prop("disabled", true);
		
		});

		//***************************************************************************************************************
		
		//Clic sur le bouton "Annuler" ajout d'un nouveau post
		jQuery("#new_cancel_button").click(function(event){
			
			//empêcher la soumission du formulaire
			event.preventDefault();

			console.log("Annulation d'un nouveau commentaire");
		
			//Cacher le formulaire d'ajout
			jQuery("#new_post_form").hide();
			//réactiver le bouton "Ajouter"
			jQuery("#add_post_button").prop("disabled", false);
		});
	
	//***************************************************************************************************************
	
	//Clic sur le bouton "Enregistrer" le nouveua Post
	jQuery("#new_send_button").click(function(event){

	//empêcher la soumission du formulaire
		event.preventDefault();
	
		var topicId = jQuery("#topicId").val();

		console.log("click new send topic id: " + topicId);


		//récupérer le commentaire
		var  postComment = jQuery("#new_post_comment").val();

		console.log("postComment: " + postComment);
	
		//vérifier que les chaînes ne sont pas vides
		if((!postComment || postComment.length == 0)){

			var error = jQuery("#error_send_new");
		
			if(error.length == 0){
				jQuery("#result")
						.append(jQuery("<div>")
						.attr("id", "error_send_new")
						.addClass("error")
						.css({"font-size": "11pt", "margin":"0px 10px"})
						.html("Le titre et/ou le texte ne peuvent être vides"));
		}
		} else{
			jQuery("#error_send_new").remove();
		

			//encoder le titre et le commentaire pour la transmission dans une url				
			var postCommentEncoded =  postComment.replace(/\n/g, "<br />");
			postCommentEncoded = encodeURIComponent(postCommentEncoded);

			console.log("postCommentEncoded: " + postCommentEncoded);
		
			var pathname = window.location.pathname;
			var origin   = window.location.origin;  
			var index = pathname.indexOf("/posts");
			var contextPath =  origin + pathname.substr(0, index);			
			
			console.log("contextPath(in posts):" + contextPath);
			
			var url = contextPath+ "/posts/"+topicId + "/"+postCommentEncoded
			
			console.log("url: " + url);
			console.log("url: " + url);
			
			var method =  "post";
			
			var errorMessage = "Une erreur s'est produite lors de la tentative d'enregistrement du commentaire";

			
			//envoi de la requête ajax avec la méthode post
			sendAJAXRequest(url, method, errorMessage);
		
		}
		
	});
});