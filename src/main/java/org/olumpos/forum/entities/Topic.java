package org.olumpos.forum.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 
 * 
 * @author daristote
 * 
 * Classe 'Entity' associée à la table 'topic' du schema 'forum_springboot' 
 * 
 * Les champs représentent ceux de la table
 * 
 * Les méthodes ('getter' et 'setter') sont utilisées pour initialiser les valeurs et les récupérer lorsque nécessaire
 *
 * Lorsque le nom des champs n'est pas le même que celui du champ correspondant de la table, 
 * l'annotation @Column est placée devant la déclaration avec le nom approprié de la table
 * 
 * Les champs annotés avec @Transient signifient qu'ils ne sont pas associés à un champ de la table
 * 
 * Les champs creationDate et updateDate sont de type LocalDateTime ce qui est conforme au type timestamp dans la bd

 * Chaque topic de la base de données possèdent une clé étrangère: 
 * 		1. une première, 'user_id', reliant le topic à son créateur par la clé primaire (id) de la table 'user'
 * 
 * Il s'agit du nom défini dans la table de la base de données qui a été redéfini ici par userId
 * 
 * Ainsi chaque topic ne peut avoir qu'un seul créateur
 * 
 * L'annotation @ManytoOne permet de relier un topic à son créateur étant donnée qu'un utilisateur peut créer plusieurs topics 
 * 
 * Ainsi lorsqu'on recherhe les topics on obtient du même coup l'utilisateur qui l'a créé par son identifiant et 
 * un objet de type User est créé automatiquement alors que les champs appropriés sont initialisés avec cet objet
 * 
 * On peut ainsi obtenir toutes les données concernant les utilisateurs concernés
 * Heureusement, pour des questions de sécurité les mots de passe sont encodés.
 * 
 * 
 * L'annotation @OneToMany devant List (Post) posts illustre que le topic est référencé par tous les posts associés avec le champ 'topicFK' de la classe Post
 * C'est ainsi qu'on peut relier tous les posts au topic auquel ils sont associés
 * 
 */

@Entity(name = "Topic") //nom de l'entité, facultatif si le nom correspond au nom de la classe
@Table(name="topic") //nom de la table associée dans la bd
public class Topic implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="title")
	@NotEmpty
	private String title;

	//@Temporal(TemporalType.TIMESTAMP)
	@Column(name="creation_date", updatable = false)
	private LocalDateTime creationDate;

//	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date", updatable = true)
	private LocalDateTime updateDate;
	
	@Column(name="is_open")
	private byte isOpen;

	//bi-directional many-to-one association to Topic
	@OneToMany(mappedBy="topicFK", fetch=FetchType.LAZY)
	private List<Post> posts;
	
	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="creator_id", referencedColumnName="id")
	private User creatorFK;
	
	@Transient
	private Integer creatorId;
	
	@Transient
	private String creatorName;
	
	@Transient
	private String message;
	
	@Transient
	private final
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	
	public Topic() {
		
		this.id = 0;
		this.title = "";
		this.creationDate = LocalDateTime.now();
		this.updateDate =  LocalDateTime.now();
		this.creatorFK =  null;
		this.creatorId = 1;
		this.creatorName = "admin";
		
	}
	
	public Topic(String title, LocalDateTime creationDate, LocalDateTime updateDate, byte isOpen, User creatorFK) {
		this(null, title, creationDate, updateDate, isOpen, creatorFK);
	}
	
	public Topic(Integer id, String title, LocalDateTime creationDate, LocalDateTime updateDate, byte isOpen, User creatorFK) {

		this.id = id;
		this.title = title;
		this.creationDate =  creationDate;
		this.updateDate =  updateDate;
		this.isOpen = isOpen;
		this.creatorFK = creatorFK;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public byte getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(byte isOpen) {
		this.isOpen = isOpen;
	}
	
	public User getCreatorFK() {
		return creatorFK;
	}

	public void setCreatorFK(User creatorFK) {
		this.creatorFK = creatorFK;
	}

	public String getCreatorName() {
		if(creatorFK!= null) {
			return creatorFK.getUsername();
		}
		return this.creatorName;
	}

	public void setCreatorName(String creatorName) {
		if(creatorFK != null) {
			this.creatorName = creatorFK.getUsername();
		}
		else {
			this.creatorName= creatorName;
		}
	}

	public Integer getCreatorId() {
		if(creatorFK != null) {
			return creatorFK.getId();
		}
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	
	public Post addPost(Post post) {
		getPosts().add(post);
		post.setTopicFK(this);
		return post;
	}

	public Post removePost(Post post) {
		getPosts().remove(post);
		post.setTopicFK(null);
		return post;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	public boolean messageExists() {
		return message != null && message.trim().length() > 0;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nTopic id: " + id + "\ntitle: " + title);
		sb.append("\nCreator id: "+ creatorId + "\nCreator name: " + creatorName + "\nCreation date: " + creationDate  + "\nupdate date: " + updateDate);
		sb.append("\nIsOpen: "+ isOpen + "\nmessage: " + message);
		
		return sb.toString();
	}

}