package org.olumpos.forum.security;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.olumpos.forum.entities.User;
import org.olumpos.forum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <br>
 * @author daristote<br>
 * <br>
 * Composante qui permet de vérifier la connexion d'un utilisateur, i.e. son login/email et mot de passe<br> 
 * correspondent à une entrée dans la BD<br>
 *<br>
 *<br>
 */

@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	Logger logger =  Logger.getLogger(AuthenticationSuccessHandler.class.getName());
	
    private final ObjectMapper mapper;
    
    @Autowired
    UserRepository userRepository;

    /**
     * 
     * @param messageConverter : messageConverter
     */
    @Autowired
    public AuthenticationSuccessHandler(MappingJackson2HttpMessageConverter messageConverter) {
        this.mapper = messageConverter.getObjectMapper();
    }
	
    /**
     * <br>
     * Méthode appelée suite à une authetification réussie de l'utilisateur qui se connecte
     * <br>
     * Elle permet de placer comme attribut de session ue instance de la classe User afin de pouvoir utiliser<br>
     * les informations au besoin sur les pages html et pour les reuqêtes REST.<br>
     * <br>
     */
    
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
       
		
		response.setStatus(HttpServletResponse.SC_OK);

        HttpSession session =  request.getSession();
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        logger.log(Level.INFO, "==========> Authentication success: " +  userDetails.getUsername());
        
        
        String username =  userDetails.getUsername();

        //Récupérer une instance de la classe User avec tous les détails del'utilisateurs
        User user =  userRepository.findByUsername(username).orElse(null);
        
        //enregistrement de l'utilisateur comme attribut de session
        session.setAttribute("user", user);
        
        logger.log(Level.INFO, "=========> user: " + user);
     
        //redirection vers la page 'topics' si l'authentification est valide
        response.sendRedirect("topics");
        
	}

	
	
	
}
