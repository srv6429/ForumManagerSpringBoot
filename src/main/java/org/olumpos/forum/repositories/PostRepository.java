package org.olumpos.forum.repositories;

import java.util.List;

import org.olumpos.forum.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * <br>
 *  @author daristote<br>
 * <br>
 * Interface qui étend l'interface générique 'JpaRepository', permettant l'utilisation de plusieurs fonctions définies<br> 
 * On paramétrise la classe avec le type de l'entité (objet java associé à une table de la BD) qui est ici Post<br>
 * ainsi que le type de la clé primaire de la table, i.e. Integer<br>
 *<br>
 * Certaines fonctions on été ajoutées.<br> 
 * <br>
 * L'annotation @Query permet de définir la requête HQL (Hibernate Query Language) utilisée pour obtenir le résultat désiré<br>
 *<br>
 *<br>
 */

public interface PostRepository extends JpaRepository<Post, Integer> {

	
	//Obtention de tous les posts acitfs
	@Query("select p from Post p where p.isActive= ?1 order by p.creationDate asc")
	public List<Post> findAllActive(byte isActive);

	//Obtention de tous les posts d'un topic (paramètre ?1) ouvert et qui sont actifs  
	@Query("select p from Post p join fetch p.userFK where p.topicFK.id= ?1  and p.isActive= 1 and p.topicFK.isOpen= 1 order by p.creationDate asc")
	public List<Post> findAllActiveByTopicId(int topicId);
	
	//Obtention de tous les posts d'un topic (paramètre ?1)
	@Query("select p from Post p join fetch p.userFK where p.topicFK.id= ?1  and p.topicFK.isOpen= 1 order by p.creationDate asc")
	public List<Post> findAllByTopicId(int topicId);
	
}
