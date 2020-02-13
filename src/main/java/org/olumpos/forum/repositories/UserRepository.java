package org.olumpos.forum.repositories;

import java.util.Optional;

import org.olumpos.forum.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *  @author daristote
 * 
 * Interface qui étend l'interface générique 'JpaRepository', permettant l'utilisation de plusieurs fonctions définies 
 * On paramétrise la classe avec le type de l'entité (objet java associé à une table de la BD) qui est ici User
 * ainsi que le type de la clé primaire de la table, i.e. Integer
 *
 * Certaines fonctions on été ajoutées. 
 * 
 * L'annotation @Query permet de définir la requête HQL (Hibernate Query Language) utilisée pour obtenir le résultat désiré
 *
 */

public interface UserRepository extends JpaRepository<User, Integer> {

	//Obtention d'un utilisateur par son pseudonyme (username) 
	public Optional<User> findByUsername(String username);

	//Obtention d'un utilisateur par son courriel (email) 
	public Optional<User> findByEmail(String email);
	
	//Obtention d'un utilisateur par son pseudonyme (username) ou courriel (email) et qui est actif 
	@Query("select u from User u where (u.username= ?1 or u.email = ?1) and u.isActive = 1 ")
	public Optional <User> findByUsernameOrEmail(String usernameOrEmail);
	
	//Obtention d'un utilisateur par son pseudonyme (username) ou courriel (email) et mot de passe et qui est actif 
	@Query("select u from User u where (u.username= ?1 or u.email = ?1) and u.password = ?2 and u.isActive = 1")
	public Optional <User> findByUsernameOrEmailAndPassword(String usernameOrEmail, String password);
	
}
