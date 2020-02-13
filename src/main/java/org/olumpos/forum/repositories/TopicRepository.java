package org.olumpos.forum.repositories;

import java.util.List;

import org.olumpos.forum.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * @author daristote
 * 
 * Interface qui étend l'interface générique 'JpaRepository', permettant l'utilisation de plusieurs fonctions définies 
 * On paramétrise la classe avec le type de l'entité (objet java associé à une table de la BD) qui est ici Topic
 * ainsi que le type de la clé primaire de la table, i.e. Integer
 *
 * Une fonction on été ajoutée. 
 * 
 * L'annotation @Query permet de définir la requête HQL (Hibernate Query Language) utilisée pour obtenir le résultat désiré
 *
 */

public interface TopicRepository extends JpaRepository<Topic, Integer> {

	//Obtention de tous les topics ouverts
	@Query("select t from Topic t where t.isOpen= 1 order by t.updateDate desc")
	public List<Topic> findAllOpen();
	
	
}
