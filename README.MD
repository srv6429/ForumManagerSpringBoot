Nom du projet: ForumManagerSpringBoot
Créateur: Donald Michon
Date: 13 févirer 2020


Testé avec:

Système d'exploitation: Linux (x86_64) Debian 9.0
JVM java 8: Oracle jdk-8u-241 pour Linux (x86_64): version java 1.8.0_241
Serveurs d'application JavaEE: Tomcat (v. 9.0.30), Wildfly (v. 18.0.1) 
Serveurs de base de données: MySQL  Version 5.7.29, pour Linux (x86_64)
Navigateur web: Firefox Quantum 68.5.0 esr (64 bits)

Frameworks et librairies utilisés: Srping Boot, JPA (Java Persistence API), MySQL JDBC connector,  Thymeleaf, Bootstrap, Javascript (ECMAScript 6, jQuery) 
 
Description:

Le projet a été créé comme un projet 'maven' et utilise le framework Spring Boot version 2.2.4 

Il s’agit d’un projet web basé sur java qui peut être déployé sur un serveur d’application java tel que Tomcat, ou Wildfly

Il a été testé avec succès sur un serveur local Tomcat (v. 9.0.30) et Wildfly (v. 18.0.1) sur un systême Linux (x86_64) Debian 9.0

--------------------------------------------

I. Base de données:

Le projet fonctionne avec une base de données relationnelle et a été testé avec le serveur MySQL 5.7.29 sur un systême Linux (x86_64) Debian 9.9

Le fichier ‹forum.sql› situé à la racin du projet, peut être exécuté pour générer le shcema 'forum_springboot', les tables requises ainsi que quelques données à insérer

On peut ex.cuter le fichier sql en ligne de commande ou utiliser un IDE (ex. MySQL Workbench)

Procédure en ligne de commande:

1. Ouvrir un terminal et se connecter à un serveur MySQL en tant qu'utilisateur ayant les droits de créer schemas et tables

	> mysql -u <nom_utilisateur> -p <mot_de_passe>

2. Une fois connecté au serveur MySQL, exécuter la commande 'source' avec en paramètre le chemin absolu du fichier 
ou si on a ouvert la connexion au serveur mysql à partir du répertoire où se trouve le fichier, on n'a qu'à indiquer le nom du fichier 

Par exemple: 

Avec Linux: /home/user/Documents/mysql/forum_springboot.sql
Avec Windows: c:\path_to_file\forum_springboot.sql

i) En indiquant le chemin absolu:
	mysq > source /home/user/Documents/mysql/forum_springboot.sql

ii) à partir du répertoire du fichier:
	mysq > source forum_springboot.sql

Si le fichier est exécuté correctement, on aura alors créé le schema 'forum_springboot' contenant les tables: user, topic, post, role, user_role, persistent_logins

Chaque table contiendra également un échantillon de données 


Par la suite on doit avoir un utilisteur autorié à utiliser le schéma

Pour créer un utilisateur dont le nom est par exemple 'springuser' et le mot de passe 'spring123' on peut utiliser la commande:

	mysql > CREATE USER 'springuser'@'localhost' IDENTIFIED BY 'spring123';

Par la suite on doit accorder les droits sur le schéma:

	mysql > GRANT ALL PRIVILEGES ON `forum_springboot`.* TO 'springuser'@'localhost' WITH GRANT OPTION;
	
	
Ici on accorde tous les droits (création, modification, effacement, etc.).

Mais on peut choisir de limiter les privilèges.

Voir: https://dev.mysql.com/doc/refman/5.7/en/grant.html



Une fois l'utisateur créé, on doit préciser les paramètres de la connexion, i.e. le pseudonyme, le mot de passe 
ainsi que l'URI de la connexion dans le fichier "application.properties" situé à la racine du projet
Par exemple:

----- Dans application.properties ----- 
...

spring.datasource.url=jdbc:mysql://localhost:3306/forum_springboot
spring.datasource.username=springuser
spring.datasource.password=spring123

...
---------------------------------------
Ces valeurs peuvent être modifiées selon les besoins, pour autant qu'elles correspondent à une URI de connexion à un serveur MySQL valide et autorisée,
et un nom d'utilisteur et un mot de passe valides qvec les autorisations nécessaires. 

------------------------------------------

II. Compilation du projet:

Prérequis:

- Java JVM version 8
- maven 
- connection Internet pour les paquets de dépendance
- serveur MySQL en fonction avec la base de données créées, ainsi que le nom d'utilisteur et mot de passe 

Pour compiler le projet en ligne de commande, on doit le faire à partir du répertoire de base contenant le fichier pom.xml

1. Compiler en effectuant les tests: s'assurer que la connexion à la bd est valide

	> mvn clean install
	
 	(ou si on ne veut pas effectuer les tests) 
	
	> mvn clean install -Dmaven.test.skip=true


On peut également utiliser la commande:

	> mvn clean package
	
	(ou sans les tests)
	
	> mvn clean package -Dmaven.test.skip=true
	
	
Cette commande permettra de créer deux paquets dans le répertoire 'target' du projet: forum-manager-2.0.jar et forum-manager-2.0.war

i) forum-manager-2.0.jar : 

Ce paquet permet de tester l'application localement et peut être utilisé comme une application autonome. Puisqu'un serrver Tomcat est intégré dans le paquet
on peut alors voir la page d'accueil en utilisant un navigateur

On peut lancé l'application avec la commande java: 

	> java -jar forum-manager-2.0.jar
	
On peut alors accéder à la page d'accueil à l'adresse locale: http://localhost:8080/

On peut dès lors se connecter avec un des 15 utilisateurs insérés dans la bd (voir plus bas)


ii)	forum-manager-2.0.war

Ce paquet peut être déployé sur un serveur d'application java, ex.: Tomcat, Wildfly etc.

Pour ce faire il s'agit de copier le fichier archive 'war' dans le répertoire du serveur.

a) Avec Tomcat: copier le fichier 'forum-manager-2.0.war' dans le répertoire 'webapps' du serveur: 

	> cp forum-manager-2.0.war <path_to_apache-tomcat-server>/webapps/
	
Si le serveur est déjà démarré, l'application sera déployée automatiquement, sinon elle le sera lors du démarrage

	
b) Avec Wildfly : il existe deux méthodes

	i)  copier le fichier 'forum-manager-2.0.war' dans le répertoire 'deployments' du serveur: s
	
		> cp forum-manager-2.0.war <path_to_wildfly_server/standalone/deployments/

Comme pour Tomcat, l'application sera automatiquement déployée.
	

	ii) En utilisant la commande maven et le plugin 'wildfly-maven-plugin' qui a été ajouté dans le fichier pom.xml du projet
	
		1. Démarrer le serveur wildfly à partir sdu répetoire bin/ du serveur
	
	 		> ./standalone -c stanalon-full.xml
	 
		2. En se plaçant à la racine du projet i.e. où se trouve le fichier pom.xml, déployer avec la commande maven:
	 
	 		> mvn wildfly:deploy
	
	
Le projet sera alors compilé après avoir effectué les tests et si tout se déroule normalement, l'emplacement du serveur 
démarré sera automatiquement détecté et l'applcation y sera déployée.
	

Contriarement à l'application locale (.jar), lorsque le projet est déployé sur un serveur externe, on doit alors spécifier le nom du projet dans l'url

Ex.: http://loalhost:8080/forum-manager-2.0/


---------------------------------------------------

Fonctionnement:

Pour exécuter le projet on utilise l’url du contexte de base
Par exemple sur un serveur local: 
http://localhost:8080/ ou http://127.0.0.1:8080/

Pour un serveur externe
http://localhost:8080/forum-manager-2.0/ ou http://127.0.0.1:8080/forum-manager-2.0/


On est alors sur la page de de connexion (login).

On peut utiliser le nom et le mot de passe d’un des utilisateurs de la base de données

15 utilisateurs ont été insérés comme échantillon dans la bd: parmi ceux-ci, seulement deux ont le statut d'adminitrateur 
alors que 13 sont de simples utilisateurs.

Les utitisateurs peuvent créer un compte, lire les topics et posts, créer un topic et un post, modifer ou effacer les topics et
posts dont ils sont les auteurs et modifier leur profil

Les administrateurs ont les mêmes privilège avec en plus la possibilité d'effacer des posts et des topics de tous les utilisateurs
 et d'activer/désactiver les simples utilisateurs

Tous les utilisateurs ont un pseudonyme, une adresse courriel et un mot de passe
Les adresses courriels et les mots de passe ont tous été créés en suivant le même schème: 

pseudonyme: <pseudonyme>
courriel: <pseudonyme>@olumpos.org 
mot de passe: <pseudonyme>123

Par exemple:

Utilisateur régulier:
usename: dummy
password: dummy123
courriel: dummy@olumpos.org

 
Pour un adminstrateur:
pseudonyme: admin
mot de pasee: admin123
courriel: admin@olumpos.org


Les mots de passe sont toutefois encodés avec l'algorithme BCrypt dans la base de données.

On peut ainsi se connecter en tant qu'administrateur (admin, admin123) et voir les autres utilisateurs à la page 'admin/users/' 
accessible par le menu en haut de page


------------------------------------------------------------------------------------------------------

