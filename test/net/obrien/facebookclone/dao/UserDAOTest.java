package net.obrien.facebookclone.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.obrien.facebookclone.model.Comment;
import net.obrien.facebookclone.model.Post;
import net.obrien.facebookclone.model.User;
import net.obrien.facebookclone.utils.DataBaseResponse;

public class UserDAOTest {

	AuthDAO authDAO = null;
	PostsDAO postsDAO = null;
	UserDAO userDAO = null;
	String jdbcURL = "jdbc:mysql://localhost:3306/";
    String jdbcUsername = "root";
    String jdbcPassword = "ulelek10";
    String jdbcDatabase = "test_facebook";
    
    String bdt = "28/12/1900";
    String firstname = "Obrien";
    String lastname = "Longe";
    String sex = "Male";
    String email = "hello@hello";
    String password = "hello123";
    String mobile = "09098989898";
    Statement statement;
    PreparedStatement prepStatement;
    ResultSet resultSet;
    Post newPost;
    Comment comment;
    User user;
    
  
    
    @BeforeEach
	void init() {
		try {
			
			//instantiate postDAO and authDAO with test database
			postsDAO = new PostsDAO( jdbcURL ,jdbcUsername, jdbcPassword,  jdbcDatabase);
			authDAO = new AuthDAO(jdbcURL,jdbcUsername, jdbcPassword,  jdbcDatabase);
			userDAO = new UserDAO(jdbcURL,jdbcUsername, jdbcPassword,  jdbcDatabase);
			//create new user, register the user 
			user = new User.UserBuilder().setFirstname(firstname).setLastname(lastname)
					.setBirthdate(bdt).setEmail(email).setPassword(password)
					.setMobile(mobile).setSex(sex).setId(1).build();
			authDAO.connect();
			authDAO.registerNewUser(user);
			
			//Create and add post to the database 
			newPost = new Post.PostBuilder().setPost("Hello there").setId(1).setUser(user).build();
			postsDAO.connect();
			postsDAO.createNewPost(newPost);
		}catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	@AfterEach
	  void tearDownDB(){
		try {
			userDAO.connect();
			userDAO.tearDownDB();
			authDAO.disconnect();
			postsDAO.disconnect();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Test the Get User Details Method")
	void userDetails() {
		try {
			DataBaseResponse databaseResponse =  userDAO.userDetails(user.getId());
			@SuppressWarnings("unchecked")
			Map<String, Object> userDetails = (Map<String, Object>)databaseResponse.getData();
			User returnedUser = (User) userDetails.get("user");
			@SuppressWarnings("unchecked")
			ArrayList<Post> userPosts = (ArrayList<Post> ) userDetails.get("posts");
			
			assertEquals(user.getFirstname(), returnedUser.getFirstname());
			assertEquals(user.getLastname(), returnedUser.getLastname());
			assertEquals(user.getBirthdate(), returnedUser.getBirthdate());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
