package net.obrien.facebookclone.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.obrien.facebookclone.model.Comment;
import net.obrien.facebookclone.model.Post;
import net.obrien.facebookclone.model.User;
import net.obrien.facebookclone.utils.DataBaseResponse;

public class CommentDAO extends DataBaseConnector {
	
	 public CommentDAO(String jdbcURL, String jdbcUsername, String jdbcPassword, String jdbcDatabase) {
		super(jdbcURL, jdbcUsername, jdbcPassword, jdbcDatabase);
	
	}


	final private int TOTAL_POST_PER_PAGE = 10;
	

		
	private String GET_COMMENTS = "select " + 
			"	comments.id as id,  " + 
			"    comments.comment_text as comment_text, " + 
			"    users.id as user_id, " + 
			"    users.firstname as firstname, " + 
			"    users.lastname as lastname, " + 
			"    comments.likes as likes, " + 
			"	case " + 
			"		when TIMESTAMPDIFF(YEAR,comments.created_at, now()) > 1 then concat_ws(\" \", TIMESTAMPDIFF(YEAR,comments.created_at, now()), \"years ago\" ) " + 
			"		when TIMESTAMPDIFF(YEAR ,comments.created_at, now()) = 1 then \"A year ago\"\n" + 
			"        when TIMESTAMPDIFF(MONTH, comments.created_at, now()) > 1 then concat_ws(\" \", TIMESTAMPDIFF(MONTH,comments.created_at, now()), \"months ago\" ) " + 
			"        when TIMESTAMPDIFF(MONTH, comments.created_at, now()) = 1 then \"A month ago\"\n" + 
			"        when TIMESTAMPDIFF(DAY, comments.created_at, now()) > 1 then concat_ws(\" \", TIMESTAMPDIFF(DAY,comments.created_at, now()), \"days ago\" ) " + 
			"        when TIMESTAMPDIFF(DAY ,comments.created_at, now()) = 1 then \"A day ago\"\n" + 
			"        when TIMESTAMPDIFF(HOUR ,comments.created_at, now()) > 1 then concat_ws(\" \", TIMESTAMPDIFF(HOUR,comments.created_at, now()), \"hours ago\" ) " + 
			"        when TIMESTAMPDIFF(HOUR, comments.created_at, now()) = 1 then \"An hour ago\"\n" + 
			"        when TIMESTAMPDIFF(MINUTE,comments.created_at, now()) > 1 then concat_ws(\" \", TIMESTAMPDIFF(MINUTE ,comments.created_at, now()), \"minutes ago\" ) " + 
			"		else \"A minute ago \" " + 
			"	end as created_at " + 
			"    from comments join users " + 
			"    on comments.user_id = users.id " + 
			"    join posts on posts.id = comments.post_id " + 
			"    where posts.id = ? " + 
			"    order by comments.created_at desc\n" + 
			"    limit ?, ?; ";
	
	private String UPDATE_COMMENT = "UPDATE comments SET comment_text = ?  " + " WHERE id = ? ;";
	
	private String POST_COMMENT = "INSERT INTO comments(comment_text, user_id, post_id) VALUES (?, ?, ?) ;";
	
	private String DELETE_COMMENT = "DELETE FROM comments WHERE id = ? ";
	
	private String GET_ONE_COMMENT = "SELECT id, comment_text FROM comments WHERE id = ? ;";
	
	private String GET_CURRENT_USER_COMMENT_LIKES = "select comment_id from comment_likes where user_id = ?; ";
	 
	 

	// COMMENTS SECTION

	public DataBaseResponse getComments(int page, int post_id, int currentUserId, HttpServletRequest request) {
		
		
		HttpSession session = request.getSession();
		
		 ArrayList<Integer> userCommentsLikes = new ArrayList<>();
		 
		ArrayList<Comment> comments = new ArrayList<>(); 
		 int OFFSET = TOTAL_POST_PER_PAGE * (page - 1);
		 PreparedStatement statement = null;
		 User user;
		 Comment comment;
		
		try{
			connect();
	         statement = jdbcConnection.prepareStatement(GET_CURRENT_USER_COMMENT_LIKES);
	         statement.setInt(1, currentUserId );
	         //id, post, user_id
	         ResultSet resultSet = statement.executeQuery(); 
	         
	         while(resultSet.next()) {
	        	 int comment_Id = resultSet.getInt("comment_Id");
	        	 userCommentsLikes.add(comment_Id);
	         }
	         resultSet.close();
	         session.setAttribute("userCommentsLikes", userCommentsLikes);
	     
	         statement.close();
			
	         statement = jdbcConnection.prepareStatement(GET_COMMENTS);
	         statement.setInt(1, post_id);
	         statement.setInt(2, OFFSET);
	         statement.setInt(3, TOTAL_POST_PER_PAGE);
	         
	         
	         resultSet = statement.executeQuery();
	         
	         while(resultSet.next()) {
	             int id = resultSet.getInt("id");
	             String comment_text = resultSet.getString("comment_text");
	             int user_id = resultSet.getInt("user_id");
	             String created_at = resultSet.getString("created_at");
	             String firstname = resultSet.getString("firstname");
	             String lastname = resultSet.getString("lastname");
	             int likes = resultSet.getInt("likes");

	             user = new User.UserBuilder().setFirstname(firstname)
	            		 .setLastname(lastname).setId(user_id)
	            		 .build();  
	             comment = new Comment.CommentBuilder().setComment(comment_text)
	            		 .setLiked(userCommentsLikes.contains(id))
	            		 .setId(id).setCreated_at(created_at)
	            		 .setUser(user)
	            		 .setNumberOfLikes(likes).build();
	            
	             comments.add(comment);
	 
	         }
	         
	         if(comments.size() > 0) {
	        	 return new DataBaseResponse(true, "Success", 200 , comments);
	         }else {
	        	 return new DataBaseResponse(false, "No Posts Yet, Be The First To Comment", 202);
	         }
	         
	     }catch(SQLException e) {
	    	 e.printStackTrace();
	         return new DataBaseResponse(false, "Server Error, Please Try Again", 202);
	     }finally {
	    	 try {
	     		 if(statement != null){
	                  statement.close();
	              }
	              disconnect();
	     	}catch(Exception e) {}
	     }
		 
		
		
	}
	
	
	
	public DataBaseResponse createNewComment(Comment comment) {
		String UPDATE_POST_COMMENTS = "UPDATE posts SET comments = comments + 1 WHERE id = ? ;";
		 PreparedStatement statement = null;
	     try{
	         connect();
	         statement = jdbcConnection.prepareStatement(POST_COMMENT);
	         statement.setString(1, comment.getComment());
	         statement.setInt(2, comment.getUser().getId());
	         statement.setInt(3, comment.getPost_id());
	         
	         boolean rowInserted = statement.executeUpdate() > 0;
	         
	         if(rowInserted) {
	        	 
	        	 statement.close();
	        	 statement = jdbcConnection.prepareStatement(UPDATE_POST_COMMENTS);
	        
	        	 statement.setInt(1, comment.getPost_id());
	        	 statement.executeUpdate();
	        	 return new DataBaseResponse(true, "Success", 200);
	         }
	         
	         return new DataBaseResponse(false, "An Error Occured Try Again Later", 202);
	         
	     }catch(SQLException e) {
	    	 e.printStackTrace();
	         return new DataBaseResponse(false, "Server Error, Please Try Again", 202);
	     }finally {
	    	 try {
	     		 if(statement != null){
	                  statement.close();
	              }
	              disconnect();
	     	}catch(Exception e) {}
	     }
		 
	 }
	 
	 
	 public DataBaseResponse updateComment(Comment comment) {
		 
		 PreparedStatement statement = null;
	     try{
	         connect();
	         statement = jdbcConnection.prepareStatement(UPDATE_COMMENT);
	         
	         statement.setString(1, comment.getComment());
	         statement.setInt(2, comment.getId());
	         
	         boolean rowUpdated = statement.executeUpdate() > 0;
	         
	         if(rowUpdated) {
	        	 
	        	 return new DataBaseResponse(true, "Success", 200);
	         }
	         
	         return new DataBaseResponse(false, "An Error Occured, Try Again Later", 202);
	         
	     }catch(SQLException e) {
	    	 e.printStackTrace();
	         return new DataBaseResponse(false, "Server Error, Please Try Again", 202);
	     }finally {
	    	 try {
	     		 if(statement != null){
	                  statement.close();
	              }
	              disconnect();
	     	}catch(Exception e) {}
	     }
		 
	 }
	 

	public DataBaseResponse deleteComment(int comment_id, int post_id) {
				
		
		String UPDATE_POST_COMMENTS = "UPDATE posts SET comments = comments - 1 WHERE id = ? ;";
		
		 PreparedStatement statement = null;
	     try{
	         connect();
	         statement = jdbcConnection.prepareStatement(DELETE_COMMENT);
	         statement.setInt(1, comment_id);
	         
	         boolean rowDeleted = statement.executeUpdate() > 0;
	         
	         if(rowDeleted) {
	        	 statement.close();
	        	 statement = jdbcConnection.prepareStatement(UPDATE_POST_COMMENTS);
	        	 statement.setInt(1, post_id);
	        	 statement.executeUpdate();
	        	 return new DataBaseResponse(true, "Success", 200);
	         }
	         
	         return new DataBaseResponse(false, "An Error Occured, Try Again Later", 202);
	         
	     }catch(SQLException e) {
	    	 e.printStackTrace();
	         return new DataBaseResponse(false, "Server Error, Please Try Again", 202);
	     }finally {
	    	 try {
	     		 if(statement != null){
	                  statement.close();
	              }
	              disconnect();
	     	}catch(Exception e) {}
	     }
		 
	}



		public DataBaseResponse likeComment(int comment_id, int user_id) {
		
		
			String INSERT_COMMENT_LIKES = "INSERT INTO comment_likes (comment_id, user_id ) VALUES (?, ?)";
			
			String UPDATE_COMMENT_LIKES = "UPDATE comments SET likes = likes + 1 WHERE id = ? ;";
			
			PreparedStatement statement = null;
			 try{
			     connect();
			     statement = jdbcConnection.prepareStatement(INSERT_COMMENT_LIKES);
			     statement.setInt(1, comment_id);
			     statement.setInt(2, user_id);
			     
			     boolean rowInserted = statement.executeUpdate() > 0;
			     
			     if(rowInserted) {
			    	 
			    	 statement.close();
			    	 statement = jdbcConnection.prepareStatement(UPDATE_COMMENT_LIKES);
			    	
			    	 statement.setInt(1, comment_id);
			    	 rowInserted = statement.executeUpdate() > 0;
			    	 return new DataBaseResponse(true, "Success", 200);
			    	 
			     }
			     
			     return new DataBaseResponse(false, "An Error Occured Try Again Later", 202);
			     
			 }catch(SQLException e) {
				 e.printStackTrace();
			     return new DataBaseResponse(false, "Server Error, Please Try Again", 202);
			 }finally {
				 try {
			 		 if(statement != null){
			              statement.close();
			          }
			          disconnect();
			 	}catch(Exception e) {}
			 }
 

		}
		
		

		public DataBaseResponse unlikeComment(int comment_id, int user_id) {
			
			String UPDATE_COMMENT_LIKES = "UPDATE comments SET likes = likes - 1 WHERE id = ? ;";
			
			
			String DELETE_COMMENT_LIKE = "DELETE FROM comment_likes WHERE comment_id = ? AND user_id = ? ;";
			
			
			PreparedStatement statement = null;
			
			 try{
			     connect();
			     statement = jdbcConnection.prepareStatement(DELETE_COMMENT_LIKE);
			     statement.setInt(1, comment_id);
			     statement.setInt(2, user_id);
			    
			     boolean rowDeleted = statement.executeUpdate() > 0;
			     
			     if(rowDeleted) {
			    	 statement.close();
			    	 statement = jdbcConnection.prepareStatement(UPDATE_COMMENT_LIKES);
			    
			    	 statement.setInt(1, comment_id);
			    	 boolean rowUpdated = statement.executeUpdate() > 0;
			    	 return new DataBaseResponse(true, "Success", 200);
			     }
			     
			     return new DataBaseResponse(false, "An Error Occured Try Again Later", 202);
			     
			 }catch(SQLException e) {
				 e.printStackTrace();
			     return new DataBaseResponse(false, "Server Error, Please Try Again", 202);
			 }finally {
				 try {
			 		 if(statement != null){
			              statement.close();
			          }
			          disconnect();
			 	}catch(Exception e) {}
			 }
		 
		
		}



}
