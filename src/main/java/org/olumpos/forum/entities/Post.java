package org.olumpos.forum.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



/**
 * The persistent class for the post database table.
 * 
 * Créée par JPA tool de Eclipse
 * Modifié par: Donald Michon
 * 
 * Description:
 * 
 * Classe 'Entity' associée à la table 'post' du schema 'forum_springboot' 
 * 
 * Le champs représentent ceux de la table
 * 
 * Les méthodes ('getter' et 'setter') sont utilisées pour initialiser les valeurs et les récupérer lorsque nécessaire
 *
 * Lorsque le nom des champs n'est pas le même que celui du champ correspondant de la table, 
 * l'annotation @Column est placée devant la déclaration avec le nom approprié de la table
 * 
 * Les champs annotés avec @Transient signifient qu'ils ne sont pas associés à un champ de la table
 * 
 * Les champs creationDate et updateDate sont de type LocalDateTime ce qui est conforme au type timestamp dans la bd
 * 
 * Chaque post de la base de données possèdent deux clés étrangères: 
 * 		1. une première, 'user_id', reliant le topic à son créateur par la clé primaire (id) de la table 'user'
 * 		2.une seconde, 'topic_id' reliant le post à un topic par la clé primaire de la table topic
 * 
 * Il s'agit des noms des tables de la base de données. Ces noms ici on été redéfinis par userId et topicId
 * 
 * Ainsi chaque post ne peut avoir qu'un seul créateur er ne peut appartenir qu'à un seul topic
 * 
 * Les annotations @ManytoOne permettent de relier un post à son créateur et à son topic, étant donnée qu'un utilisateur peut publier plusieurs posts 
 * et un topic peut contenir plusieurs posts
 * 
 * Lorsqu'on recherhe les posts on obtient du même coup l'utilisateur et le topic par leur identifiant et 
 * des objets de type User et Topic sont créés automatiquement alors que les champs sont initialisés avec ces objets
 * 
 * 
 */

@Entity(name = "Post")  //nom de l'entité, facultatif si le nom correspond au nom de la classe 
@Table(name="post") //nom de la table de la bd

public class Post implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	
//	@Column(nullable=false)
//	@NotEmpty(message = "Le titre ne peut être vide")
//	private String title;
	
	@Lob
	@Column(nullable = false, columnDefinition = "TEXT")
	@NotEmpty(message = "Le commentaire ne peut être vide")
	private String comment;

	//@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creation_date", updatable = false)
	private LocalDateTime creationDate;

//	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date", updatable = true)
	private LocalDateTime updateDate;
	
	@Column(name="is_active")
	private byte isActive;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private User userFK;
	
	@Transient
//	@Column(name="user_id", nullable = false)
	private Integer userId;
	
	@Transient
	private String username;
	
	@Transient
//	@Column(name="topic_id", nullable = false)
	private Integer topicId;

	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="topic_id", referencedColumnName="id")
	private Topic topicFK;
	
//	@Transient
//	private String message;
	
//	@Transient
//	private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Transient
	private final
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	public Post() {
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

//	public String getTitle() {
//		return this.title;
//	}
//
//	public void setTitle(String title) {
//		this.title = title;
//	}

	public Integer getTopicId() {
		if(this.topicFK != null) {
			return this.topicFK.getId();
		}
		return this.topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public Integer getUserId() {
		if(this.userFK != null) {
			return this.userFK.getId();
		}
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId =  userId;
	}

	public String getUsername() {
		if(this.userFK != null) {
			return this.userFK.getUsername();
		}
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationDate() {
		return formatter.format(creationDate);
	}
	
	public LocalDateTime getNonFormattedCreationDate() {
		return creationDate;
	}
	
	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateDate() {
		return formatter.format(updateDate);
	}

	public LocalDateTime getNonFormattedUpdateDate() {
		return updateDate;
	}
		
	public User getUserFK() {
		return userFK;
	}

	public void setUserFK(User userFK) {
		this.userFK = userFK;
	}

	public Topic getTopicFK() {
		return topicFK;
	}

	public void setTopicFK(Topic topicFK) {
		this.topicFK = topicFK;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nPost id: " + id);
		sb.append("\nComment: " + comment);
		sb.append("\nCreator id: "+ getUserId() + "\nCreator name: " + getUsername() + "\nTopic id: " + getTopicId());
		sb.append("\nCreation date: " + creationDate  + "\nupdate date: " + updateDate);
		sb.append("\nisActive "+ isActive);// + "\nmessage: " + message);
		
		return sb.toString();
	}
	

}