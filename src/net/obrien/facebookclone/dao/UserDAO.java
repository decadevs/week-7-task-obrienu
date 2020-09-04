package net.obrien.facebookclone.dao;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.obrien.facebookclone.model.Post;
import net.obrien.facebookclone.model.User;
import net.obrien.facebookclone.utils.DataBaseResponse;

public class UserDAO extends DataBaseConnector  {

	 public UserDAO(String jdbcURL, String jdbcUsername, String jdbcPassword, String jdbcDatabase) {
			super(jdbcURL, jdbcUsername, jdbcPassword, jdbcDatabase);
		}
	
	String GET_USER_POSTS = "select \n" + 
			"	id, \n" + 
			"    post,\n" + 
			"    likes,\n" + 
			"    comments,\n" + 
			"    user_id,\n" + 
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
			"    from posts \n" + 
			"    where user_id = ?\n" + 
			"    order by posts.created_at desc\n" + 
			"    limit 0, 20; ";
	
	
	
	String GET_USER = "SELECT * FROM users WHERE id = ?";
	
	
	
	public DataBaseResponse userDetails(int requestedUserId) {
		
		 PreparedStatement statement = null;
		 Post post;
		 User user ;
		 ArrayList<Post> posts = new ArrayList<>();
	     try{
	         connect();
	         statement = jdbcConnection.prepareStatement(GET_USER);
	         statement.setInt(1, requestedUserId);
	         
		       
	         ResultSet resultSet = statement.executeQuery();
	         if(resultSet.next()) {
	        	  user = new User.UserBuilder()
	        			  .setFirstname(resultSet.getString("firstname"))
	        			  .setLastname(resultSet.getString("lastname"))
	        			  .setId(resultSet.getInt("id"))
	        			  .setAbout(resultSet.getString("about"))
	        			  .setEmail(resultSet.getString("email"))
	        			  .setBirthdate(resultSet.getString("birthdate"))
	        			  .setMobile(resultSet.getString("mobile"))
	        			  .setSex(resultSet.getString("sex"))
	            		 .build();
	        	
	         }else {
	        	 return new DataBaseResponse(false, "No User Found", 404);
	         }
	         
	        
	         statement.close();
	         statement = jdbcConnection.prepareStatement(GET_USER_POSTS);
	         statement.setInt(1, requestedUserId);
	         resultSet = statement.executeQuery();
        
	        while (resultSet.next()) {
	        	 
	             int id = resultSet.getInt("id");
	             String post_text = resultSet.getString("post");
	             String created_at = resultSet.getString("created_at");
	             int likes = resultSet.getInt("likes");
	             int comments = resultSet.getInt("comments");

	             post = new Post.PostBuilder().setCreated_at(created_at).setId(id).setPost(post_text).setUser(user)
	            		 .setNumberOfComments(comments).setNumberOfLikes(likes).build();
	            
	             posts.add(post);
	            
	         }
	        
	        Map<String, Object> userData = new HashMap<>();
	        userData.put("user", user);
	        userData.put("posts", posts);
	        
	        if(posts.size() > 0) {
	        	 return new DataBaseResponse(true, "Success", 200, userData);
	        }

	        return new DataBaseResponse(true, "No Post Found For User", 200, userData);
	        
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
