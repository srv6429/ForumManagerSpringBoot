package org.olumpos.forum.config;

//Pour Remember-me dans la BD 
import javax.sql.DataSource;

import org.olumpos.forum.security.AuthenticationSuccessHandler;
import org.olumpos.forum.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 
 * @author daristote
 *	Configuration de la sécurité de l'application
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	//POur l'utilisation de Remeber-me dans la BD
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	

	/**
	 * Encoder pour crypter les mots de passe
	 * Utilisation de l'algorithme BCrypt
	 * 
	 * Préférable d'Avoir une fonction. Si l'encodeur est utilisé à plusieurs endroits
	 * Si on change l'algorithme, on ne le change qu'ici
	 * 
	 * @return: BCrypt encoder
	 */
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/*
	 * Utilisation d'un service de sécurité  pour l'authentification des utilisateurs: CustomUserDetailsService
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		auth
			.userDetailsService(customUserDetailsService)
			.passwordEncoder(passwordEncoder())
			
			//for testing
		//	.and()
		//	.inMemoryAuthentication()
		//	.withUser("user").password("user123").roles("USER").and()
		//	.withUser("admin").password("admin123").roles("USER", "ADMIN")
			;
	}

	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * Configuration de la sécurité pour les pages html et les fichiers utilisés par l'application 
	 */
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.csrf().disable() //important!!!!! POST/PUT/DELETE requests will be denied
			.authorizeRequests()
			.antMatchers("/resources/**", "/assets/**", "/webjars/**").permitAll()
			.antMatchers("/", "/register", "/login", "/topic").permitAll()
			.antMatchers("/admin/**").hasRole("ADMIN")
			//.antMatchers("/users").hasRole("ADMIN")
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login").permitAll()
				//.loginProcessingUrl("/login")
				.successHandler(authenticationSuccessHandler)
				//.defaultSuccessUrl("/topics")
				.failureUrl("/login?error")
				.permitAll()
			.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout")
				.deleteCookies("remember-me")
				.permitAll()
				.and()
				//default: save cookie with name: "remember-me"
			.rememberMe()
																																																																																			//customize values
             																																																																											//.key("my-secure-key")
 //            	.rememberMeCookieName("my-remember-me-cookie")
			
			//décommenter pour utiliser la bd
   //          	.tokenRepository(persistentTokenRepository()) //use persistent repository
   //          	.tokenValiditySeconds(24 * 60 * 60) // durée: 24 heures// default: 2 weeks (24 * 60 * 60 * 14)
         .and()

			.exceptionHandling()
				.accessDeniedPage("/accessDenied");
			
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	/**
	 * Si l'on veut utiliser la BD pour enregistrer le cookie de session: 'Remember-me'
	 * Le cookie 'remember-me' est enregistré dans une table de la BD au lieu d'un cookie de seesion du navigateur
	 * Le token est enregistré automatiquement dans la table : 'persisten_logins'
	 * 
	 * @return jdbcTokenRepositoryImpl : une instance de l'interface PersistentTokenRepository
	 */
		
    public PersistentTokenRepository persistentTokenRepository(){
    	JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
    	jdbcTokenRepositoryImpl.setDataSource(dataSource);
    	return jdbcTokenRepositoryImpl;
    }
	
}
