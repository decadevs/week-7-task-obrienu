package net.obrien.facebookclone.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.obrien.facebookclone.dao.PostsDAO;
import net.obrien.facebookclone.model.Post;
import net.obrien.facebookclone.model.User;
import net.obrien.facebookclone.utils.DataBaseResponse;

/**
 * Servlet implementation class PostServelet
 */
public class PostServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private PostsDAO postDAO;
	 
	    public void init() {
	        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
	        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
	        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
	        String jdbcDatabase = getServletContext().getInitParameter("jdbcDatabase");
	        postDAO = new PostsDAO(jdbcURL, jdbcUsername, jdbcPassword, jdbcDatabase);
	 
	    }
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostServelet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String fullPath = request.getRequestURI().substring(request.getContextPath().length());
	
		try {
			switch(fullPath) {
			case "/posts/post":
				makePost(request, response);
				break;
			case "/posts/edit":
				editPost(request, response);
				break;
			case "/posts/delete":
				deletePost(request, response);
				break;
			case "/posts/like":
				likePost(request, response);
				break;
			case "/posts/unlike":
				unlikePost(request, response);
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
protected void deletePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		int post_id = Integer.parseInt(request.getParameter("post_id"));

		DataBaseResponse resData =   postDAO.deletePost(post_id);
		response.sendRedirect(request.getHeader("referer"));
		
	}
	
protected void editPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String text = request.getParameter("post");
		int post_id = Integer.parseInt(request.getParameter("post_id"));
	
		Post post = new Post.PostBuilder().setPost(text)
				.setId(post_id)
				.build();
		DataBaseResponse resData =   postDAO.updatePost(post);
		response.sendRedirect(request.getHeader("referer"));
		
	}
	
protected void makePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String text = request.getParameter("post");
		int user_id = (int) request.getSession().getAttribute("user_id");
		User user =new User.UserBuilder().setId(user_id).build();
		Post post = new Post.PostBuilder().setPost(text).setUser(user).build();
		DataBaseResponse resData =   postDAO.createNewPost(post);
		response.sendRedirect(request.getHeader("referer"));
		
	}


protected void likePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	ArrayList<Integer> userLikes = (ArrayList<Integer>)  request.getSession().getAttribute("userLikes");
	
	int post_id = Integer.parseInt(request.getParameter("post_id"));
	int user_id = (int) request.getSession().getAttribute("user_id");
	userLikes.add((Integer)  post_id);
	DataBaseResponse resData =   postDAO.likePost(post_id, user_id);
	response.sendRedirect(request.getHeader("referer"));
	
}

protected void unlikePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	ArrayList<Integer> userLikes = (ArrayList<Integer>)  request.getSession().getAttribute("userLikes");
	int post_id = Integer.parseInt(request.getParameter("post_id"));
	int user_id = (int) request.getSession().getAttribute("user_id");
	
	DataBaseResponse resData =   postDAO.unlikePost(post_id, user_id);
	userLikes.remove((Integer) post_id);
	response.sendRedirect(request.getHeader("referer"));
	
}

}
