package org.olumpos.forum.entities;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * <br>
 * @author daristote<br>
 * <br>
 * Classe Entity qui est associée à la table role dans la BD<br>
 * Permet de définir plusieurs rôles pour un utilisteur<br>
 *<br>
 *<br>
 */

@Entity
@Table(name = "role")
public class Role {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable=false, unique=true)
	@NotEmpty
	private String name;
		
	//Chaque utilisateur peut avoir plusieurs rôles
	@ManyToMany(mappedBy="roles")
	private List<User> users;

	public Role() {}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "" + name;
	}
	
	
	
}
