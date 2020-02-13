package org.olumpos.forum.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

/**
 * 
 * @author daristote
 * 	Configuration de l'application WebMvc: 
 * 	1. vues pour les pages html
 *  2. Validator pour les messages et internationalisation (messages.properties)
 */

@Configuration
public class WebConfig implements WebMvcConfigurer{

	@Autowired
	MessageSource messageSource;

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/register").setViewName("register");
		registry.addViewController("/profile").setViewName("profile");
		registry.addViewController("/home").setViewName("userhome");
		registry.addViewController("/admin/home").setViewName("adminhome");

		registry.addViewController("/topics").setViewName("topics");
		registry.addViewController("/posts").setViewName("posts");
		registry.addViewController("/users").setViewName("users");

		registry.addViewController("/403").setViewName("403");
		
		//ajouté: non utilisé
		registry.addViewController("/topic").setViewName("topic");
		
		//ajouté
		//registry.addViewController("/accessDenied").setViewName("403");
	}

	/**
	 * Fonction qui permet d'utiliser les fichies messages_*.properties pour ls messages locaux (Localization)
	 * et les erreurs de validation
	 * 
	 */
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(messageSource);
        return factory;
    }
	
    /**
     * Configuration des requêtes cross-domain (CORS)
     * Donner les autorisations pour les méthodes des requêtes http
     */
    @Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowedMethods("GET","POST","PUT","DELETE","HEAD","OPTIONS")
			//.allowedHeaders("header1", "header2", "header3")
			//.exposedHeaders("header1", "header2")
			.allowCredentials(true)
			.maxAge(3600); //cache 60 minutes
	}
	
    /**
     * 
     * @return SpringSecurityDialect
     */
    
    @Bean
	public SpringSecurityDialect securityDialect() {
	    return new SpringSecurityDialect();
	}

	
}
