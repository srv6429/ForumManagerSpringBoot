package org.olumpos.forum.security;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.olumpos.forum.entities.User;
import org.olumpos.forum.repositories.UserRepository;
import org.olumpos.forum.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>
 * @author daristote<br>
 * <br>
 * Utilisation d'une classe personnalisée en tant que @Service pour authentifier un utilisateur<br>
 *<br>
 */

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

	Logger logger = Logger.getLogger(CustomUserDetailsService.class.getName());
	
	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

			User user =  userService.findByUsernameOrEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username/Email "+ username + "not found" ));

			logger.log(Level.INFO, "user found: " + user);
			
			//details used for login
			//We can use user.getEmail() or user.getUsernam() as identifier on the web site
			//return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user));
			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	/**
	 * <br>
	 * Méthode qui permet d'obtneir une liste de tous les rôles de l'utilisateur<br>
	 * <br>
	 * On applique la fonction sur chacun des objets de type Role, i.e. en récupérant le nom<br>
	 * 
	 * On convertit le stream en tableau de String contenant tous les rôle octroyés à l'utilisateur<br>
	 * <br>
	 * @param user : l'utilisateur<br>
	 * @return : une liste des rôles de l'utilisateur<br>
	 * <br>
	 */
	private Collection<? extends GrantedAuthority> getAuthorities(User user) {
		

		String[] userRoles = user.getRoles().stream().map(role -> role.getName()).toArray((String[]::new));

		//On retourne une liste de type 'GrantedAuthorities'
		Collection <GrantedAuthority> authorities =  AuthorityUtils.createAuthorityList(userRoles);
		return authorities;
	}

}
