package net.obrien.facebookclone.dao;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.obrien.facebookclone.model.Post;
import net.obrien.facebookclone.model.User;
import net.obrien.facebookclone.utils.DataBaseResponse;

/**
 * Class handles CRUD operation for all user posts
 * @author Obrien
 *
 */
public class PostsDAO extends DataBaseConnector  {
	 
	 public PostsDAO(String jdbcURL, String jdbcUsername, String jdbcPassword, String jdbcDatabase) {
			super(jdbcURL, jdbcUsername, jdbcPassword, jdbcDatabase);
			
		}
	
	final private int TOTAL_POST_PER_PAGE = 10;
	
	private String GET_POSTS = "select \n" + 
			"	posts.id as id, \n" + 
			"    posts.post as post,\n" + 
			"    users.id as user_id,\n" + 
			"    users.firstname as firstname,\n" + 
			"    users.lastname as lastname,\n" + 
			"    posts.likes as likes,\n" + 
			"    posts.comments as comments,\n" + 
			"	case\n" + 
			"		when TIMESTAMPDIFF(YEAR,posts.created_at, now()) > 1 then concat_ws(\" \", TIMESTAMPDIFF(YEAR,posts.created_at, now()), \"years ago\" )\n" + 
			"		when TIMESTAMPDIFF(YEAR ,posts.created_at, now()) = 1 then \"A year ago\"\n" + 
			"        when TIMESTAMPDIFF(MONTH, posts.created_at, now()) > 1 then concat_ws(\" \", TIMESTAMPDIFF(MONTH,posts.created_at, now()), \"months ago\" )\n" + 
			"        when TIMESTAMPDIFF(MONTH, posts.created_at, now()) = 1 then \"A month ago\"\n" + 
			"        when TIMESTAMPDIFF(DAY, posts.created_at, now()) > 1 then concat_ws(\" \", TIMESTAMPDIFF(DAY,posts.created_at, now()), \"days ago\" )\n" + 
			"        when TIMESTAMPDIFF(DAY ,posts.created_at, now()) = 1 then \"A day ago\"\n" + 
			"        when TIMESTAMPDIFF(HOUR ,posts.created_at, now()) > 1 then concat_ws(\" \", TIMESTAMPDIFF(HOUR,posts.created_at, now()), \"hours ago\" )\n" + 
			"        when TIMESTAMPDIFF(HOUR, posts.created_at, now()) = 1 then \"An hour ago\"\n" + 
			"        when TIMESTAMPDIFF(MINUTE,posts.created_at, now()) > 1 then concat_ws(\" \", TIMESTAMPDIFF(MINUTE ,posts.created_at, now()), \"minutes ago\" )\n" + 
			"		else \"A minute ago\"\n" + 
			"	end as created_at\n" + 
			"    from posts join users\n" + 
			"    on users.id = posts.user_id\n" + 
			"    order by posts.created_at desc\n" + 
			"    limit ?, ?; ";
	
	
	private String UPDATE_POST = "UPDATE posts SET post = ? where id = ? ;";
	
	
	private String CREATE_POST = "INSERT INTO posts(post, user_id) VALUES(?, ?);";
	
	
	private String DELETE_POST = "DELETE FROM posts WHERE id = ? ;";
	
	
	private String GET_ONE_POST = "SELECT posts.id as post_id, post, user_id, likes, comments, firstname, lastname,\n" + 
			"		case \n" + 
			"			when TIMESTAMPDIFF(YEAR,posts.created_at, now()) > 1 then concat_ws(\" \", TIMESTAMPDIFF(YEAR,posts.created_at, now()), \"years ago\" )\n" + 
			"			when TIMESTAMPDIFF(YEAR ,posts.created_at, now()) = 1 then \"A year ago\" \n" + 
			"			when TIMESTAMPDIFF(MONTH, posts.created_at, now()) > 1 then concat_ws(\" \", TIMESTAMPDIFF(MONTH,posts.created_at, now()), \"months ago\" )\n" + 
			"			when TIMESTAMPDIFF(MONTH, posts.created_at, now()) = 1 then \"A month ago\" \n" + 
			"			when TIMESTAMPDIFF(DAY, posts.created_at, now()) > 1 then concat_ws(\" \", TIMESTAMPDIFF(DAY,posts.created_at, now()), \"days ago\" )\n" + 
			"			when TIMESTAMPDIFF(DAY ,posts.created_at, now()) = 1 then \"A day ago\"\n" + 
			"			when TIMESTAMPDIFF(HOUR ,posts.created_at, now()) > 1 then concat_ws(\" \", TIMESTAMPDIFF(HOUR,posts.created_at, now()), \"hours ago\" )\n" + 
			"			when TIMESTAMPDIFF(HOUR, posts.created_at, now()) = 1 then \"An hour ago\" \n" + 
			"			when TIMESTAMPDIFF(MINUTE,posts.created_at, now()) > 1 then concat_ws(\" \", TIMESTAMPDIFF(MINUTE ,posts.created_at, now()), \"minutes ago\" ) \n" + 
			"			else \"A minute ago \"\n" + 
			"		end as created_at\n" + 
			"		FROM posts join users\n" + 
			"        on posts.user_id = users.id\n" + 
			"        WHERE posts.id = ? ;";
	
		String GET_CURRENT_USER_LIKES = "select post_id from post_likes where user_id = ?; ";

	 

	 
	 /**
	  * Method handles posts read functionallity, it reads users posts from 
	  * the database in batches speciefied by the page parameter and the 
	  * 'TOTAL_POST_PER_PAGE' fields. it also sets the liked field of a post based
	  * off if the current users list of liked post
	  * @param page
	  * @param currentUserId
	  * @param request
	  * @return DataBaseResponse object
	  */
	 public DataBaseResponse getPosts(int page,int currentUserId, HttpServletRequest request) {
		 HttpSession session = null;
		 if(request != null ) {
			 session  = request.getSession();
		 }
		
		 ArrayList<Integer> userLikes = new ArrayList<>();
		 

		 int OFFSET = TOTAL_POST_PER_PAGE * (page - 1);
		 
		 PreparedStatement statement = null;
		 Post post;
		 User user;
	     try{
	    	 //gets users liked posts and saves it in a list which is saved in session
	         connect(); 
	         statement = jdbcConnection.prepareStatement(GET_CURRENT_USER_LIKES);
	         statement.setInt(1, currentUserId );
	        
	         ResultSet resultSet = statement.executeQuery(); 
	         
	         while(resultSet.next()) {
	        	 int post_id = resultSet.getInt("post_id");
	        	 userLikes.add(post_id);
	         }
	         if(request != null) {
	        	 session.setAttribute("userLikes", userLikes);
	         }
	         
	         //Posts from the database
	         ArrayList<Post> posts = new ArrayList<>();
	         statement.close();
	         statement = jdbcConnection.prepareStatement(GET_POSTS);
	         statement.setInt(1, OFFSET );
	         statement.setInt(2, TOTAL_POST_PER_PAGE );
	       
	         resultSet = statement.executeQuery();    
	         
	         while(resultSet.next()) {
	             int id = resultSet.getInt("id");
	             String post_text = resultSet.getString("post");
	             String created_at = resultSet.getString("created_at");
	             int user_id = resultSet.getInt("user_id");
	             int likes = resultSet.getInt("likes");
	             int comments = resultSet.getInt("comments");
	             String firstname = resultSet.getString("firstname");
	             String lastname = resultSet.getString("lastname");
	             
	             user = new User.UserBuilder().setFirstname(firstname).setLastname(lastname).setId(user_id)
	            		 .build();
	             
	             //Builds the posts and sets the liked fied based off the users liked list
	             post = new Post.PostBuilder().setCreated_at(created_at).setId(id).setPost(post_text).setUser(user)
	            		 .setNumberOfComments(comments)
	            		 .setLiked(userLikes.contains(id))
	            		 .setNumberOfLikes(likes).build();
	            
	             
	             posts.add(post);
	 
	         }
	         
	         resultSet.close();
	         
	         if(posts.size() > 0) {
	        	 return new DataBaseResponse(true, "Success", 200 , posts);
	         }else {
	        	 return new DataBaseResponse(false, "No Posts Yet, Be The First To Make A POST", 202);
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

	 /**
	  * Method handles create opreations, adds users posts to the database
	  * @param post
	  * @return
	  */
	 public DataBaseResponse createNewPost(Post post) {
		 
		 PreparedStatement statement = null;
	     try{
	         connect();
	         statement = jdbcConnection.prepareStatement(CREATE_POST);
	         statement.setString(1, post.getPost());
	         statement.setInt(2, post.getUser().getId());
	         boolean rowInserted = statement.executeUpdate() > 0;
	         
	         if(rowInserted) {
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
	 
	 /**
	  * Method post updates 
	  * @param post
	  * @return DataBaseResponse object
	  */
	 public DataBaseResponse updatePost(Post post) {
		 
		 PreparedStatement statement = null;
	     try{
	         connect();
	         statement = jdbcConnection.prepareStatement(UPDATE_POST);
	         statement.setString(1, post.getPost());
	         statement.setInt(2, post.getId());
	         
	         boolean rowInserted = statement.executeUpdate() > 0;
	         
	         if(rowInserted) {
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
	 

	 /**
	  * Method handles deletion of posts from the database record
	  * @param post_id
	  * @return DataBaseResponse object
	  */
	public DataBaseResponse deletePost(int post_id) {
		 PreparedStatement statement = null;
	     try{
	         connect();
	         statement = jdbcConnection.prepareStatement(DELETE_POST);
	         statement.setInt(1, post_id);
	         
	         boolean rowInserted = statement.executeUpdate() > 0;
	         
	         if(rowInserted) {
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
	
	
	/**
	 * Method fetches a post with specified id from the 
	 * database record.
	 * @param post_id
	 * @return DataBaseResponse object
	 */
	public DataBaseResponse getPost(int post_id) {
		
		 PreparedStatement statement = null;
		 Post post;
		 ResultSet resultSet = null;
	     try{
	         connect();
	         statement = jdbcConnection.prepareStatement(GET_ONE_POST);
	         statement.setInt(1, post_id);
	        
	         resultSet = statement.executeQuery();
	         
	         if (resultSet.next()) {
	             int id = resultSet.getInt("post_id");
	             String post_text = resultSet.getString("post");
	             String created_at = resultSet.getString("created_at");
	             int user_id = resultSet.getInt("user_id");
	             int likes = resultSet.getInt("likes");
	             int comments = resultSet.getInt("comments");
	             String firstname = resultSet.getString("firstname");
	             String lastname = resultSet.getString("lastname");
	             
	             
	             User user = new User.UserBuilder().setFirstname(firstname).setLastname(lastname).setId(user_id)
	            		 .build();
	             
	             
	             post = new Post.PostBuilder().setCreated_at(created_at).setId(id).setPost(post_text).setUser(user)
	            		 .setNumberOfComments(comments).setNumberOfLikes(likes).build();
	             return new DataBaseResponse(true, "Success", 200 , post);
	         }else {
	        	 return new DataBaseResponse(false, "Error Fetching Post", 202);
	         }
	         
         
	     }catch(SQLException e) {
	    	 e.printStackTrace();
	         return new DataBaseResponse(false, "Server Error, Please Try Again", 202);
	     }finally {
	    	 
	    	 try {
	    		 resultSet.close();
	     		 if(statement != null){
	                  statement.close();
	              }
	              disconnect();
	     	}catch(Exception e) {}
	     }
		 
	}
	
	/**
	 * Mehtod handles posts likes, it adds a like record to the database
	 * and update the likes field of the associated post 
	 * @param post_id
	 * @param user_id
	 * @return DataBaseResponse object
	 */
	public DataBaseResponse likePost(int post_id, int user_id) {
		
		String UPDATE_POST_LIKES = "UPDATE posts SET likes = likes + 1 WHERE id = ? ;";
		
	
		String INSERT_LIKES = "INSERT INTO post_likes (post_id, user_id ) VALUES (?, ?)";

		PreparedStatement statement = null;
	     try{
	         connect();
	         statement = jdbcConnection.prepareStatement(INSERT_LIKES);
	         statement.setInt(1, post_id);
	         statement.setInt(2, user_id);
	         
	         boolean rowInserted = statement.executeUpdate() > 0;
	         
	         if(rowInserted) {
	        	 
	        	 statement.close();
	        	 statement = jdbcConnection.prepareStatement(UPDATE_POST_LIKES);
	        	 statement.setInt(1, post_id);
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
	
	/**
	 * Method handles unlike operation removing like records from database 
	 * and decreasing the likes field of the associated post
	 * @param post_id
	 * @param user_id
	 * @return DataBaseResponse object
	 */
	public DataBaseResponse unlikePost(int post_id, int user_id) {
		
		String UPDATE_POST_LIKES = "UPDATE posts SET likes = likes - 1 WHERE id = ? ;";	
		String DELETE_POST_LIKES = "DELETE FROM post_likes WHERE post_id = ? AND user_id = ? ;";

		PreparedStatement statement = null;
		
	     try{
	         connect();
	         statement = jdbcConnection.prepareStatement(DELETE_POST_LIKES);
	         statement.setInt(1, post_id);
	         statement.setInt(2, user_id);
	         
	         boolean rowDeleted = statement.executeUpdate() > 0;
	         
	         if(rowDeleted) {
	        	 statement.close();
	        	 statement = jdbcConnection.prepareStatement(UPDATE_POST_LIKES);
	
	        	 statement.setInt(1, post_id);
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
