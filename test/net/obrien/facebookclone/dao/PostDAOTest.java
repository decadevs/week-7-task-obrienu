package net.obrien.facebookclone.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.*;

import net.obrien.facebookclone.model.Post;
import net.obrien.facebookclone.model.User;
import net.obrien.facebookclone.utils.DataBaseResponse;

public class PostDAOTest {

	AuthDAO authDAO;
	PostsDAO postsDAO;
	String jdbcURL = "jdbc:mysql://localhost:3306/";
    String jdbcUsername = "root";
    String jdbcPassword = "ulelek10";
    String jdbcDatabase = "test_facebook";
    User user;
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
    
    
    @BeforeEach
	void init() {
		try {
			//instantiate postDAO and authDAO with test database
			postsDAO = new PostsDAO( jdbcURL ,jdbcUsername, jdbcPassword,  jdbcDatabase);
			authDAO = new AuthDAO(jdbcURL,jdbcUsername, jdbcPassword,  jdbcDatabase);
			//create new user, register the user 
			user = new User.UserBuilder().setFirstname(firstname).setLastname(lastname)
					.setBirthdate(bdt).setEmail(email).setPassword(password)
					.setMobile(mobile).setSex(sex).setId(1).build();
			authDAO.connect();
			authDAO.registerNewUser(user);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterEach
	void tearDownDB(){
		try {
			postsDAO.tearDownDB();
			authDAO.disconnect();
			postsDAO.disconnect();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	@DisplayName("Test the create post method")
	void createPost() {
		try {
			newPost = new Post.PostBuilder().setPost("Hello there").setUser(user).build();
			postsDAO.connect();
			postsDAO.createNewPost(newPost);
			postsDAO.connect();
			Statement statement = postsDAO.jdbcConnection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM POSTS;");
			while(resultSet.next()) {
				assertEquals(1, resultSet.getInt(1), "Method should add post to the database");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Test the Get Posts Method")
	void getPosts() {
		try {
			//Create two posts and add posts to the database
			newPost = new Post.PostBuilder().setPost("Hello there").setUser(user).build();
			Post newPost2 = new Post.PostBuilder().setPost("Hello there").setUser(user).build();
			postsDAO.connect();
			postsDAO.createNewPost(newPost);
			postsDAO.connect();
			postsDAO.createNewPost(newPost2);
			//make request call to the data base for all posts 
			postsDAO.connect();
			DataBaseResponse databaseResponse = postsDAO.getPosts(1, 1, null);
			@SuppressWarnings("unchecked")
			ArrayList<Post> returnedPosts = (ArrayList<Post>) databaseResponse.getData();
			assertEquals(2, returnedPosts.size(), "Method should return a total of two posts");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Test the Update Post Method")
	void updatePosts() {
		try {
			//Create two posts and add posts to the database
			newPost = new Post.PostBuilder().setPost("Hello there").setUser(user).build();
			postsDAO.connect();
			postsDAO.createNewPost(newPost);
			Post updatedPost = new Post.PostBuilder().setPost(newPost)
					.setId(1)
					.setPost("This is a new update").build();
			//make update call to the data base for all posts 
			postsDAO.connect();
			postsDAO.updatePost(updatedPost);
			postsDAO.connect();
			//Query database for update
			Statement statement = postsDAO.jdbcConnection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT post FROM posts WHERE id = 1 ;");
			while(resultSet.next()) {
				assertEquals(updatedPost.getPost(), resultSet.getString(1), "Method should update the post with new input");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	@DisplayName("Test the Delete Post Method")
	void deletePosts() {
		try {
			//Create a post and add post to the database
			newPost = new Post.PostBuilder().setPost("Hello there").setUser(user).build();
			postsDAO.connect();
			postsDAO.createNewPost(newPost);
			
			//make call to delete post from database by passing the post id
			postsDAO.connect();
			postsDAO.deletePost(1);
			postsDAO.connect();
			//Query database for post
			Statement statement = postsDAO.jdbcConnection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM posts ;");
			while(resultSet.next()) {
				assertEquals(0, resultSet.getInt(1), "Method should delete the post from the database");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Test the like Post Method")
	void likePosts() {
		try {
			//Create a post and add post to the database
			newPost = new Post.PostBuilder().setPost("Hello there").setUser(user).build();
			postsDAO.connect();
			postsDAO.createNewPost(newPost);
			
			//make call to like post  by passing the post id and user id
			postsDAO.connect();
			postsDAO.likePost(1, user.getId());
			
			//Query database for postnumber of likes
			postsDAO.connect();
			Statement statement = postsDAO.jdbcConnection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT likes FROM posts WHERE id = 1;");
			int actual = -1;
			while(resultSet.next()) {
				actual = resultSet.getInt(1);
			}
			assertEquals(1, actual, "Method should increase the number of likes by one");
			statement.close();
			statement = postsDAO.jdbcConnection.createStatement();
			resultSet = statement.executeQuery("SELECT count(*) FROM post_likes where post_id = 1 AND user_id = 1 ;");
			while(resultSet.next()) {
				actual = resultSet.getInt(1) ;
			}
			assertEquals(1, actual, "Method should add record to the post_likes table ;");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Test the like Post Method")
	void unlikePosts() {
		try {
			//Create a post and add post to the database
			newPost = new Post.PostBuilder().setPost("Hello there").setUser(user).build();
			postsDAO.connect();
			postsDAO.createNewPost(newPost);
			
			//make call to like post  by passing the post id and user id
			postsDAO.connect();
			postsDAO.likePost(1, user.getId());
			
			//make call to unlike post 
			postsDAO.connect();
			postsDAO.unlikePost(1, user.getId());
			
			//Query database for postnumber of likes
			postsDAO.connect();
			Statement statement = postsDAO.jdbcConnection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT likes FROM posts WHERE id = 1;");
			while(resultSet.next()) {
				assertEquals(0, resultSet.getInt(1), "Method should reduce the number of likes by one");
			}
			statement.close();
			statement = postsDAO.jdbcConnection.createStatement();
			resultSet = statement.executeQuery("SELECT count(*) FROM post_likes where post_id = 1 AND user_id = 1 ;");
			while(resultSet.next()) {
				assertEquals(0, resultSet.getInt(1), "Method should add record to the post_likes table ;");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
