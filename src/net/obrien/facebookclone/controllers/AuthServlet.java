package net.obrien.facebookclone.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.obrien.facebookclone.dao.AuthDAO;
import net.obrien.facebookclone.model.User;
import net.obrien.facebookclone.model.UserSignIn;
import net.obrien.facebookclone.utils.DataBaseResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Handles all authentication routes, 
 * login, sign up and logout
 */
public class AuthServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6684153287404523162L;
	 private AuthDAO authDAO;
	 
	    public void init() {
	        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
	        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
	        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
	        String jdbcDatabase = getServletContext().getInitParameter("jdbcDatabase");
	        authDAO = new AuthDAO(jdbcURL, jdbcUsername, jdbcPassword, jdbcDatabase);
	 
	    }
	    
	  /**
	   * Checks the path calls the approprate method to handle each request
	   */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {
    		String action = request.getServletPath();
    		if("/logout".equals(action)) {
        		signOut(request, response);
        	}else {
        		RequestDispatcher dispatcher = request.getRequestDispatcher("views/auth.jsp");
                dispatcher.forward(request, response);
        	}
        	
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	
    }
    
    /**
	   * Checks the path calls the approprate method to handle each request
	   */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	        String action = request.getServletPath();
    	        try {
    	            switch (action) {
    	            case "/login":
    	            	login(request, response);
    	                break;
    	            case "/signup":
    	               signIn(request, response);
    	                break;
    	            case"/logout":
    	            	
    	            }
    	        } catch (SQLException ex) {
    	            throw new ServletException(ex);
    	        }
    }
    
    
    /**
     * Method clears user session on signout
     * @param request
     * @param response
     * @throws SQLException
     * @throws IOException
     * @throws ServletException
     */
    private void signOut(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException{
    	 HttpSession session = request.getSession();
    	 session.invalidate();
    	 response.sendRedirect("login");
    }
    
    
    /**
     * Checks for user login details, validates and sets user session
     * if credentials are valid or redirects back to the login page otherwise
     * @param request
     * @param response
     * @throws SQLException
     * @throws IOException
     * @throws ServletException
     */
    private void login(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException{
    	
     DataBaseResponse reqStatus = authDAO.userLogin(new UserSignIn(request.getParameter("login-email"), request.getParameter("login-password")));
     HttpSession session = request.getSession();
     if(!reqStatus.isStatus()) {
    	
    	 request.setAttribute("reqStatus", reqStatus);
    	 RequestDispatcher dispatcher = request.getRequestDispatcher("views/auth.jsp");
         dispatcher.forward(request, response);
     }else {
    	 User user =(User) reqStatus.getData();
    	 session.setAttribute("user", String.format("%s %s", user.getLastname(), user.getFirstname()));
    	 session.setAttribute("user_id", user.getId());
    	 session.setMaxInactiveInterval(60 * 60 * 60);
    	 response.sendRedirect("home");
     }
    	
    }
    
    
    
    /**
     * Handles user sign up
     * @param request
     * @param response
     * @throws SQLException
     * @throws IOException
     * @throws ServletException
     */
    private void signIn(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException{
    	String birthdate = request.getParameter("birthdate-reg");
    	String email = request.getParameter("email-reg");
    	String firstname = request.getParameter("firstname-reg");
    	String lastname = request.getParameter("lastname-reg");
    	String mobile = request.getParameter("mobile-reg");
    	String password = request.getParameter("password-reg");
    	String sex = request.getParameter("sex-reg");
    	
    	User newUser = new User(birthdate, email, firstname, lastname, mobile, password, sex);
    	DataBaseResponse reqStatus = authDAO.registerNewUser(newUser);
    
    	if(reqStatus.isStatus()) {
    		String message = "Hello " + firstname + " " + lastname + ", Welcome to facebook. Login for a unique experience";
    		 reqStatus.setMessage(message);
    	}
    	request.setAttribute("reqStatus", reqStatus);
    	RequestDispatcher dispatcher = request.getRequestDispatcher("views/auth.jsp");
        dispatcher.forward(request, response);
    }
}
