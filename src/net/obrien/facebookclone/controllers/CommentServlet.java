package net.obrien.facebookclone.controllers;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.obrien.facebookclone.dao.CommentDAO;
import net.obrien.facebookclone.model.Comment;
import net.obrien.facebookclone.model.User;
import net.obrien.facebookclone.utils.DataBaseResponse;

/**
 * Servlet implementation class CommentServlet
 */
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	 private CommentDAO commentDAO;
	 
	    public void init() {
	        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
	        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
	        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
	        String jdbcDatabase = getServletContext().getInitParameter("jdbcDatabase");
	        commentDAO = new CommentDAO(jdbcURL, jdbcUsername, jdbcPassword, jdbcDatabase);
	 
	    }
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fullPath = request.getRequestURI().substring(request.getContextPath().length());
		
		try {
			switch(fullPath) {
			case "/comments/comment":
				makeComment(request, response);
				break;
			case "/comments/edit":
				updateComment(request, response);
				break;
			case "/comments/delete":
				deleteComment(request, response);
				break;
			case "/comments/like":
				likeComment(request, response);
				break;
			case "/comments/unlike":
				unlikeComment(request, response);
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	protected void makeComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String text = request.getParameter("comment");
		int post_id = Integer.parseInt(request.getParameter("post_id"));
		int user_id = (int) request.getSession().getAttribute("user_id");
		User user =new User.UserBuilder().setId(user_id).build();
		Comment comment = new Comment.CommentBuilder().setPost_id(post_id).setUser(user).setComment(text).build();
		DataBaseResponse resData =   commentDAO.createNewComment(comment);
		response.sendRedirect(request.getHeader("referer"));
		
	}
	
protected void updateComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String text = request.getParameter("comment");
		int comment_id = Integer.parseInt(request.getParameter("comment_id"));
		Comment comment = new Comment.CommentBuilder().setId(comment_id).setComment(text).build();
		DataBaseResponse resData =   commentDAO.updateComment(comment);
		response.sendRedirect(request.getHeader("referer"));
		
	}


protected void deleteComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	int post_id = Integer.parseInt(request.getParameter("post_id"));
	int comment_id = Integer.parseInt(request.getParameter("comment_id"));
	
	DataBaseResponse resData =   commentDAO.deleteComment(comment_id, post_id);
	response.sendRedirect(request.getHeader("referer"));
	
}

protected void likeComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	ArrayList<Integer> userCommentsLikes = (ArrayList<Integer>)  request.getSession().getAttribute("userCommentsLikes");
	
	int comment_id = Integer.parseInt(request.getParameter("comment_id"));
	int user_id = (int) request.getSession().getAttribute("user_id");
	userCommentsLikes.add((Integer)  comment_id);
	DataBaseResponse resData =   commentDAO.likeComment(comment_id, user_id);
	response.sendRedirect(request.getHeader("referer"));
	
}

protected void unlikeComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	ArrayList<Integer> userCommentsLikes = (ArrayList<Integer>)  request.getSession().getAttribute("userCommentsLikes");
	
	int comment_id = Integer.parseInt(request.getParameter("comment_id"));
	int user_id = (int) request.getSession().getAttribute("user_id");
	userCommentsLikes.remove((Integer)  comment_id);
	DataBaseResponse resData =   commentDAO.unlikeComment(comment_id, user_id);
	response.sendRedirect(request.getHeader("referer"));
	
}
}
