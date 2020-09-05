package net.obrien.facebookclone.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.obrien.facebookclone.dao.CommentDAO;
import net.obrien.facebookclone.dao.PostsDAO;
import net.obrien.facebookclone.dao.UserDAO;
import net.obrien.facebookclone.model.Post;
import net.obrien.facebookclone.utils.DataBaseResponse;

/**
 * Servlet implementation class RouteServlet
 */
public class RouteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PostsDAO postsDAO ;
	private UserDAO userDAO;
	private CommentDAO commentDAO;
	
	
    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
        String jdbcDatabase = getServletContext().getInitParameter("jdbcDatabase");
        postsDAO = new PostsDAO(jdbcURL, jdbcUsername, jdbcPassword, jdbcDatabase);
        userDAO = new UserDAO(jdbcURL, jdbcUsername, jdbcPassword, jdbcDatabase);
        commentDAO = new CommentDAO(jdbcURL, jdbcUsername, jdbcPassword, jdbcDatabase);
    }
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RouteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		String action = request.getServletPath();
		  try {
			 HttpSession session = request.getSession();
			 if(session.getAttribute("user_id") == null) {
				response.sendRedirect("login");
			 }else {
				
	   	            switch (action) {
	   	            case "/":
	   	            	handleHome(request, response);
	   	                break;
	   	            case "/home":
	   	            	handleHome(request, response);
	   	                break;
	   	            case "/profile":
	   	              handleProfile(request, response);
	   	                break;
	   	            case "/post":
	   	            	handlePostPage(request, response);
	   	            	break;
	   	            case "error":
	   	            	//handleError(request, response);
	   	            	break;
	   	            default:
	   	            	//handleError(request, response);
	   	            }
	   	        }
		  }catch(ServletException | IOException e) {
				 e.printStackTrace();
	   	        }
	   	        catch (Exception e) {
	   	           e.printStackTrace();
	   	        }
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}
	
	private void handlePostPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession();
		int post_id = Integer.parseInt(request.getParameter("post_id"));
		int page = Integer.parseInt(request.getParameter("page"));
		DataBaseResponse postQueryData =  postsDAO.getPost(post_id);
		DataBaseResponse commentQueryData = commentDAO.getComments(page,post_id, (Integer) session.getAttribute("user_id"),  request);
		if( !postQueryData.isStatus()) {
			
			response.sendRedirect("error");
			return;
		}
	
		request.setAttribute("post", postQueryData.getData());
		request.setAttribute("comments", commentQueryData.getData());
		RequestDispatcher dispatcher = request.getRequestDispatcher("views/post.jsp");
        dispatcher.forward(request, response);
		
	}
	
	private void handleProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			int user_id = Integer.parseInt(request.getParameter("user_id"));
			
			DataBaseResponse queryData =  userDAO.userDetails(user_id);
			if(!queryData.isStatus()) {
				response.sendRedirect("error");
			}
			
			@SuppressWarnings("unchecked")
			Map<String, Object> userDetails = (Map<String, Object>) queryData.getData();
			request.setAttribute("user", userDetails.get("user"));
			request.setAttribute("posts", userDetails.get("posts"));
			RequestDispatcher dispatcher = request.getRequestDispatcher("views/profile.jsp");
			request.setAttribute("queryData", queryData);
	        dispatcher.forward(request, response);
			
		}
		
	
	private void handleHome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		DataBaseResponse queryData =  postsDAO.getPosts(1, (Integer) session.getAttribute("user_id"),  request);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("views/index.jsp");
		request.setAttribute("queryData", queryData);
		
        dispatcher.forward(request, response);
		
	}
	
	private void handleError(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("views/404.html");
		
        dispatcher.forward(request, response);
		
	}

}
