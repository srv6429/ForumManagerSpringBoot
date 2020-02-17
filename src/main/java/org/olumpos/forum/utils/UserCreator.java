package org.olumpos.forum.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 
 * @author daristote
 *
 * Classe utilistaire pour créer une requête SQL afin d'insérer des utilisteurs dans la bd
 *
 */

public class UserCreator {
	
	private String[] usernames = {	"admin", "zeus", "hera", "ares", "apollon", "hades", "hestia", "athena", 
									"poseidon", "aphrodite", "artemis", "hephaïstos", "pegase", "icare", "dummy"};

	
	//Résultat:
	//Chaque utilisateur se voit attibuer une mot de passe encrypté qui correspond à son pseudonyme auquel on y annexe le suffixe '123' 
	/*
	insert into user (username, password, email, role, is_active) values
		('admin', '$2a$10$GIG4T5Gz9RoYHwfHEIdNreMr3IvRZZIU9hSPnP0qsLfnCGERZOb0.', 'admin@olumpos.org', 1),
		('zeus', '$2a$10$fDhqIFSjFbHZ9wOqfjwNNO3LjjsHwAJDynbV3sIG3Xt5xc2HbtVIS', 'zeus@olumpos.org', 1),
		('hera', '$2a$10$cSo5uA6.RW7SxZgxVnyZ7egBMtAeas/boxgv0xELNKxW7P0ej4GNK', 'hera@olumpos.org', 1),
		('ares', '$2a$10$hWUuUrqVB03TpE/JCauE..Fmhen8rCgqasvKqNRjuupgFg2CAWCFG', 'ares@olumpos.org', 1),
		('apollon', '$2a$10$7RGMSmDXl.uSv6BfHsWPjuHl90usdNUz2VQqenA.tfaXeZPBxKl6W', 'apollon@olumpos.org', 1),
		('hades', '$2a$10$nLIqJiV9ANE4.bPtKlIwVe2airhljRzv72AFqHmTlF/GBEMfCY0dW', 'hades@olumpos.org', 1),
		('hestia', '$2a$10$CCJLLoTG8wwUeVuo2XY0TOjPRBTJZRmxYbEGO/URrlE6yeSElLYi2', 'hestia@olumpos.org', 1),
		('athena', '$2a$10$CCuiYkfENKGLu9IOYoZEj.fPZaWkhg/SIsMv4o2a6MNh7QL4bhmj.', 'athena@olumpos.org', 1),
		('poseidon', '$2a$10$Qil5Vo/8Xmp.W3sswSrOGO9tH9INIh6okkFl6dTbNPFvc4ZdAw4fm', 'poseidon@olumpos.org', 1),
		('aphrodite', '$2a$10$WIDL3RSQwzE.erz8RMKNyO5X/77dh3VD7ElT4ipcQRwOUjrSyAcdO', 'aphrodite@olumpos.org', 1),
		('artemis', '$2a$10$0Hd64jr7bCoLdiOxlFjlv.3VIJEy7wI34tzOPrfOozuZnuWKn959S', 'artemis@olumpos.org', 1),
		('hephaïstos', '$2a$10$L7IjP6L2A1xRwI2zWp9Yq.Eiy5sOf.TB.wcnChZ4b/QHOChp4vAwi', 'hephaïstos@olumpos.org', 1),
		('pegase', '$2a$10$2RhYCORRvzbN6XjCUnrt6.SkgfbbdysVReHCvN5FBQ9.4TTmccHgq', 'pegase@olumpos.org', 1),
		('icare', '$2a$10$KhO445dy6FetZPO0BeMNJ.EWe6wvBXTc.Sq2xB3zIyVnG7sxjRSQq', 'icare@olumpos.org', 1),
		('dummy', '$2a$10$zG2.c.mY/QIvVCzZttfas.6pJTuuGp6xIGeAB8qDWzL8xOEXEi3vu', 'dummy@olumpos.org', 1);
*/
	
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	private void createInsertQuery() {
		
		BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();
		
		StringBuilder sb =  new StringBuilder("insert into user (username, password, email, is_active)\nvalues\n");
		
		for (int i = 0; i < usernames.length; i++) {
		
			String cryptedPassword = encoder.encode(usernames[i] + "123");
		
			sb.append("('" + usernames[i] + "', '" + cryptedPassword + "', '" + usernames[i] + "@olumpos.org', 1)");
			
			if(i < usernames.length - 1) {
				sb.append(",\n");
			}
		}
		
		sb.append(";\n");
	
		
		System.out.println(sb.toString());
		
		for (int i = 0; i < 10; i++) {
			
			String encodedPassword = encoder.encode("admin123");
			System.out.println(encodedPassword);
			
			System.out.println(encoder.matches("admin123", encodedPassword));
		}
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	public static void main(String[] args) {
		UserCreator creator =  new UserCreator();
		
		creator.createInsertQuery();
		
		


	}

}
