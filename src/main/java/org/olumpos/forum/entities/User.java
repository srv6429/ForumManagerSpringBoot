package org.olumpos.forum.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <br>
 * @author daristote<br>
 *<br>
 * Classe 'Entity' associée à la table 'user' du schema 'forum_springboot'<br> 
 *<br>
 * Les méthodes ('getter' et 'setter') sont utilisées pour initialiser les valeurs et les récupérer lorsque nécessaire<br>
 *<br>
 * Lorsque le nom des champs n'est pas le même que celui du champ correspondant de la table,<br> 
 * l'annotation @Column est placée devant la déclaration avec le nom approprié de la table<br>
 * <br>
 * Les champs annotés avec @Transient signifient qu'ils ne sont pas associés à un champ de la table<br>
 * <br>
 * Les champs creationDate et updateDate sont de type LocalDateTime ce qui est conforme au type timestamp dans la bd<br>
 * <br>
 * L'annotation @ManytoMany pour la List(Role) roles permet de relier un utilisteur à plusieurs rêles (ex.: 'USER' 'ADMIN')<br>
 * <br>
 * <br>
 */

@Entity
@Table(name = "user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
//	@NotEmpty(message = "{errors.not_empty}")
	@Size(min = 4, max = 20, message = "{errors.username_size}")
	private String username;

	@Column(nullable = false, unique = true)
	@NotEmpty(message = "{errors.not_empty}")
	@Email(message = "{errors.invalid_email}")
	private String email;

	@Column(nullable = false)
//	@NotEmpty(message = "{errors.not_empty}")
	@Size(min = 7, max = 100, message = "{errors.password_size}")
	private String password;

	@Column
	private byte isActive;

//	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date", updatable = false)
	private LocalDateTime creationDate;

	// @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date", updatable = true)
	private LocalDateTime updateDate;


	//Ignore le champ qui n'a pas d'équivalent dans la table de la bd
	@Transient
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	//Un utilisateur peut avoir plusieurs rôles et inversement un rôle peut à associé à plusieurs utilisateurs
	//Donc Many to Many
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = {
			@JoinColumn(name = "USER_ID", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "ROLE_ID", referencedColumnName = "id") })
	private List<Role> roles;

	public boolean isAdmin() {

		if (roles == null || roles.isEmpty()) {
			return false;
		}
		for (Role role : roles) {
			if ("ROLE_ADMIN".equals(role.getName())) {
				return true;
			}
		}
		return false;
	}

	public Integer getId() {
		return id;
	}

	public final void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte getIsActive() {
		return isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		if (roles == null) {
			roles = new ArrayList<>();
		}
		roles.add(role);
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	@JsonIgnore
	public String getCreationDate() {
		return formatter.format(creationDate);
	}

	@JsonIgnore
	public LocalDateTime getNonFormattedCreationDate() {
		return creationDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	@JsonIgnore
	public String getUpdateDate() {
		return formatter.format(updateDate);
	}

	@JsonIgnore
	public LocalDateTime getNonFormattedUpdateDate() {
		return updateDate;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nUser id: " + id + "\nusername: " + username + "\nemail: " + email);
		sb.append("\npassword: " + password + "\nCreation date: " + creationDate + "\nupdate date: " + updateDate);
		sb.append("\nis active: " + isActive + "\n");

		return sb.toString();
	}

}
