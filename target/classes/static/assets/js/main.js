/**
 * Main file included with all html templates
 * 
 * Contient sulement n fonction qui permet d'envoyer des requêtes ajax pour un accès à la bd
 * Le fonctions définies dans les classes de contrôleurs retournent une ResponseEntity avec en paramètre un objet JSON
 *  
 */


var sendAJAXRequest = function(requestUrl, method, errorMessage){
	
	var request = jQuery.ajax({
		  url: requestUrl,
		  type: method,
		  dataType: "text"
	});
	
	//Succès: réponse du serveur
	request.done(function(data) {
		console.log("Done: " + data);
		var json = jQuery.parseJSON(data);
		console.log("json: " + json);
		//succès de la mise à jour
		if(json.status && json.status == "ok"){
			jQuery("#result").append(jQuery("<div>").addClass("success").html("Success! with method " + this.type));
            console.log("Success");
            //rafraîchir la page
           // document.location.href = window.location.href;
            location.reload(true);
				
		} else{
			jQuery("#result").append(jQuery("<div>").addClass("error").html(errorMessage));
		}


	});
	

	//Échec: réponse du serveur
	request.fail(function(jqXHR, textStatus) {
	  console.log( "Request failed: " + textStatus );
		jQuery("#result").append(jQuery("<div>").addClass("error").html(errorMessage));
	});
	
};