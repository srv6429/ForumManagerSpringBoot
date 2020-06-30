package org.olumpos.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 
 * @author daristote
 * 
 * Classe contenant la fonction main pour lancer l'application 
 * Ajout d'un commentaire additionnel
 *
 */

@SpringBootApplication
public class ForumManagerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ForumManagerApplication.class, args);
	}
	/**
	 * Pour déployer sur un serveur externe (ex.: Wildfly, Tomcat)
	 * @param builder
	 * @return SpringApplicationBuilder
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ForumManagerApplication.class);
	}
	
}
