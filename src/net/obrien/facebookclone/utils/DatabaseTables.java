package net.obrien.facebookclone.utils;

public enum DatabaseTables {
	
	 USERS("CREATE TABLE `users` (\n" + 
	 		"  `id` int NOT NULL AUTO_INCREMENT,\n" + 
	 		"  `email` varchar(50) NOT NULL,\n" + 
	 		"  `mobile` varchar(15) NOT NULL,\n" + 
	 		"  `password_hash` char(60) NOT NULL,\n" + 
	 		"  `firstname` varchar(100) NOT NULL,\n" + 
	 		"  `lastname` varchar(100) NOT NULL,\n" + 
	 		"  `birthdate` date NOT NULL,\n" + 
	 		"  `sex` varchar(10) NOT NULL,\n" + 
	 		"  `about` varchar(300) DEFAULT NULL,\n" + 
	 		"  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" + 
	 		"  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" + 
	 		"  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" + 
	 		"  PRIMARY KEY (`id`),\n" + 
	 		"  UNIQUE KEY `email` (`email`),\n" + 
	 		"  UNIQUE KEY `mobile` (`mobile`)\n" + 
	 		");"),
	
	
	POSTS("CREATE TABLE `posts` (\n" + 
			"  `id` int NOT NULL AUTO_INCREMENT,\n" + 
			"  `post` varchar(250) NOT NULL,\n" + 
			"  `user_id` int NOT NULL,\n" + 
			"  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" + 
			"  `comments` int NOT NULL DEFAULT '0',\n" + 
			"  `likes` int NOT NULL DEFAULT '0',\n" + 
			"  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" + 
			"  PRIMARY KEY (`id`),\n" + 
			"  KEY `user_id` (`user_id`),\n" + 
			"  CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE\n" + 
			");"),
	
	
	 POST_LIKES("CREATE TABLE `post_likes` (\n" + 
	 		"  `user_id` int NOT NULL,\n" + 
	 		"  `post_id` int NOT NULL,\n" + 
	 		"  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" + 
	 		"  PRIMARY KEY (`user_id`,`post_id`),\n" + 
	 		"  KEY `post_id` (`post_id`),\n" + 
	 		"  CONSTRAINT `post_likes_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,\n" + 
	 		"  CONSTRAINT `post_likes_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE CASCADE\n" + 
	 		") ;"),
	 
	 COMMENTS("CREATE TABLE `comments` (\n" + 
	 		"  `id` int NOT NULL AUTO_INCREMENT,\n" + 
	 		"  `comment_text` varchar(250) NOT NULL,\n" + 
	 		"  `user_id` int NOT NULL,\n" + 
	 		"  `post_id` int NOT NULL,\n" + 
	 		"  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" + 
	 		"  `likes` int NOT NULL DEFAULT '0',\n" + 
	 		"  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" + 
	 		"  PRIMARY KEY (`id`),\n" + 
	 		"  KEY `user_id` (`user_id`),\n" + 
	 		"  KEY `post_id` (`post_id`),\n" + 
	 		"  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,\n" + 
	 		"  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE CASCADE\n" + 
	 		");"),
	 
	 COMMENTS_LIKES("CREATE TABLE `comment_likes` (\n" + 
	 		"  `user_id` int NOT NULL,\n" + 
	 		"  `comment_id` int NOT NULL,\n" + 
	 		"  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" + 
	 		"  PRIMARY KEY (`user_id`,`comment_id`),\n" + 
	 		"  KEY `comment_id` (`comment_id`),\n" + 
	 		"  CONSTRAINT `comment_likes_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,\n" + 
	 		"  CONSTRAINT `comment_likes_ibfk_2` FOREIGN KEY (`comment_id`) REFERENCES `comments` (`id`) ON DELETE CASCADE\n" + 
	 		");");
	 
	 public final String str;
	
	 DatabaseTables(String str){
		 this.str = str;
	 }
	 
	 public String get() {
		 return this.str;
	 }
	 
	 
	 
	 
	 
	 
	 
	
}
