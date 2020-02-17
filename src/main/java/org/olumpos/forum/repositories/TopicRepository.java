package org.olumpos.forum.repositories;

import java.util.List;

import org.olumpos.forum.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * <br>
 * @author daristote<br>
 * <br>
 * Interface qui étend l'interface générique 'JpaRepository', permettant l'utilisation de plusieurs fonctions définies<br> 
 * On paramétrise la classe avec le type de l'entité (objet java associé à une table de la BD) qui est ici Topic<br>
 * ainsi que le type de la clé primaire de la table, i.e. Integer<br>
 *<br>
 * Une fonction on été ajoutée.<br> 
 * <br>
 * L'annotation @Query permet de définir la requête HQL (Hibernate Query Language) utilisée pour obtenir le résultat désiré<br>
 * <br>
 *<br>
 */

public interface TopicRepository extends JpaRepository<Topic, Integer> {

	//Obtention de tous les topics ouverts
	@Query("select t from Topic t where t.isOpen= 1 order by t.updateDate desc")
	public List<Topic> findAllOpen();
	
	
}
