package net.obrien.facebookclone.dao;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import net.obrien.facebookclone.model.Comment;
import net.obrien.facebookclone.model.Post;
import net.obrien.facebookclone.model.User;

/**
 * This class contains tests for the Comments DAO class
 * @author decagon
 *
 */
public class TestCommentDAOTest {
	AuthDAO authDAO = null;
	PostsDAO postsDAO = null;
	CommentDAO commentDAO = null;
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
			commentDAO = new CommentDAO(jdbcURL,jdbcUsername, jdbcPassword,  jdbcDatabase);
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
			commentDAO.connect();
			commentDAO.tearDownDB();
			authDAO.disconnect();
			postsDAO.disconnect();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	@DisplayName("Test Create Comment Method")
	  void createNewComment() {
		
		try {
		//Create new comment for post with id of 1 
		comment = new Comment.CommentBuilder().setPost_id(1).setUser(user)
				.setComment("A simple comment").build();
		commentDAO.createNewComment(comment);
		
		//Query database for posts number of comments
		commentDAO.connect();
		statement = commentDAO.jdbcConnection.createStatement();
		resultSet = statement.executeQuery("SELECT comments FROM posts WHERE id = 1;");
		int actual = -1;
		while(resultSet.next()) {
			 actual = resultSet.getInt(1);
		}
		//Query database for comments record in comments table
		assertEquals(1, actual, "Method should increase the number of comments by one");
		statement.close();
		statement = commentDAO.jdbcConnection.createStatement();
		resultSet = statement.executeQuery("SELECT count(*) FROM comments where post_id = 1 AND user_id = 1 ;");
		while(resultSet.next()) {
			actual = resultSet.getInt(1);
		}
		assertEquals(1, actual, "Method should add record to the post_likes table ;");
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
		@Test
		@DisplayName("Test Update Comment Method")
		void updateComment(){
			try {
				//Create new comment for post with id of 1 and add to database
				comment = new Comment.CommentBuilder().setPost_id(1).setUser(user)
						.setComment("A simple comment").build();
				commentDAO.createNewComment(comment);
				//Edit Comment
				Comment updatedComment = new Comment.CommentBuilder()
						.setComment(comment).setComment("An Updated Comment").setId(1).build();
				commentDAO.updateComment(updatedComment);
				commentDAO.connect();
				statement = commentDAO.jdbcConnection.createStatement();
				
				resultSet = statement.executeQuery("SELECT comment_text FROM comments WHERE post_id = 1 and id = 1;");
				String actual = "";
				if(resultSet.next()) {
					actual = resultSet.getString(1);
				}
				//Query database for comments record in comments table
				assertEquals("An Updated Comment", actual, "Method should should update the comment");
				statement.close();	
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		
		@Test
		@DisplayName("Test Delete Comment Method")
		void deleteComment() {
			try {
				//Create new comment for post with id of 1 and add to database
				comment = new Comment.CommentBuilder().setPost_id(1).setUser(user)
						.setComment("A simple comment").build();
				commentDAO.createNewComment(comment);
				//Delete comment
				commentDAO.deleteComment(1, 1);
				
				commentDAO.connect();
				statement = commentDAO.jdbcConnection.createStatement();
				
				resultSet = statement.executeQuery("SELECT COUNT(*) FROM comments WHERE post_id = 1 and id = 1;");
				int actual = -1;
				if(resultSet.next()) {
					actual = resultSet.getInt(1);
				}
				//Query database for comments record in comments table
				assertEquals(0, actual, "Method should delete the specified comment");
				statement.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		

		
}
