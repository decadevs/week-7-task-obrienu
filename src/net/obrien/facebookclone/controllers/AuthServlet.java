package net.obrien.facebookclone.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.obrien.facebookclone.dao.AuthDAO;
import net.obrien.facebookclone.model.User;
import net.obrien.facebookclone.model.UserSignIn;
import net.obrien.facebookclone.utils.DataBaseResponse;

import java.io.IOException;
import java.sql.SQLException;


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
	 
	        authDAO = new AuthDAO(jdbcURL, jdbcUsername, jdbcPassword);
	 
	    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("views/auth.jsp");
        dispatcher.forward(request, response);
    }
    
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
  
    	            }
    	        } catch (SQLException ex) {
    	            throw new ServletException(ex);
    	        }
    }
    
    private void login(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException{
    	
     DataBaseResponse reqStatus = authDAO.userLogin(new UserSignIn(request.getParameter("login-email"), request.getParameter("login-password")));
     
     if(!reqStatus.isStatus()) {
    	 System.out.println("Failed Login");
    	 request.setAttribute("reqStatus", reqStatus);
    	 RequestDispatcher dispatcher = request.getRequestDispatcher("views/auth.jsp");
         dispatcher.forward(request, response);
     }else {
    	 System.out.println("Success login");
    	 request.setAttribute("reqStatus", reqStatus);
    	 RequestDispatcher dispatcher = request.getRequestDispatcher("views/auth.jsp");
         dispatcher.forward(request, response);
     }
    	
    }
    
    
    private void signIn(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException{
    	String birthdate = request.getParameter("birthdate-reg");
    	String email = request.getParameter("email-reg");
    	String firstname = request.getParameter("firstname-reg");
    	String lastname = request.getParameter("lastname-reg");
    	String mobile = request.getParameter("mobile-reg");
    	String password = request.getParameter("password-reg");
    	String sex = request.getParameter("sex-reg");
    	System.out.println(password);
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
