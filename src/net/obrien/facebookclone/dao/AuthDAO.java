package net.obrien.facebookclone.dao;

import net.obrien.facebookclone.model.User;
import net.obrien.facebookclone.model.UserSignIn;
import net.obrien.facebookclone.utils.DataBaseResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This DAO class provides CRUD database operations for t UserSignUp
 * @author O'Brien
 *
 */
public class AuthDAO extends DataBaseConnector {

    public AuthDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        super(jdbcURL, jdbcUsername, jdbcPassword);
    }

    public DataBaseResponse registerNewUser(User newUser)  {
        PreparedStatement statement = null;
            try{
                connect();
                if(checkIfUserEmailAlreadyExists(newUser.getEmail()))
                    return new DataBaseResponse(false, "Email Already Exists", 202);

                if(checkIfUserMobileAlreadyExists(newUser.getMobile()))
                    return new DataBaseResponse(false, "Mobile Number Already Exists", 202);

                String INSERT_USER = "insert into users (birthdate, email, firstname, lastname, mobile, password_hash, sex)" +
                        "values(?, ?, ?, ?, ? , ?,?)";

                System.out.println(newUser.getPassword());
                
                String PASSWORD_HASH = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
                statement = jdbcConnection.prepareStatement(INSERT_USER);
                statement.setString(1, newUser.getBirthdate());
                statement.setString(2, newUser.getEmail());
                statement.setString(3, newUser.getFirstname());
                statement.setString(4, newUser.getLastname());
                statement.setString(5, newUser.getMobile());
                statement.setString(6, PASSWORD_HASH);
                statement.setString(7, newUser.getSex());
                boolean rowInserted = statement.executeUpdate() > 0;

                if(rowInserted){
                    return new DataBaseResponse(true, "Success New Account Created", 200);
                }else
                    return new DataBaseResponse(false, "Server Error, Please Try Again", 202);

            }catch (SQLException e){
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
    };


    private boolean checkIfUserEmailAlreadyExists(String email) throws SQLException {
        String CHECK_USER_EXISTS = "select count(*) as count from users where email = ?;";
        PreparedStatement statement = jdbcConnection.prepareStatement(CHECK_USER_EXISTS);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        int count = 0;
        if (resultSet.next()) {
        	count = resultSet.getInt("count");
        }
        return count == 1 ? true : false;
    }

    private boolean checkIfUserMobileAlreadyExists(String mobile) throws SQLException {
        String CHECK_USER_EXISTS = "select count(*) as count from users where mobile = ? ;";
        PreparedStatement statement = jdbcConnection.prepareStatement(CHECK_USER_EXISTS);
        statement.setString(1, mobile);
        ResultSet resultSet = statement.executeQuery();
        int count = 0;
        if (resultSet.next()) {
        	count = resultSet.getInt("count");
        }
        return count == 1 ? true : false;
    }

    
    public DataBaseResponse userLogin(UserSignIn userSignIn) {
    	
    	PreparedStatement statement = null;
    	User user;
        try{
            connect();
           
            String GET_USER = "select about, birthdate, email, firstname, lastname, mobile, password_hash, sex from users where email = ? ;";
            
            statement = jdbcConnection.prepareStatement(GET_USER);
            statement.setString(1, userSignIn.getEmail());
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                String about = resultSet.getString("about");
                String email = resultSet.getString("email");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String birthdate = resultSet.getString("birthdate");
                String mobile = resultSet.getString("mobile");
                String password_hash = resultSet.getString("password_hash");
                String sex = resultSet.getString("sex");
                
                user = new User(about, birthdate, email, firstname, lastname, mobile, password_hash, sex);
            }else {
            	
            	return new DataBaseResponse(false, "Invalid Email", 202);
            }
                  
            
            if(null == user.getPassword() || !user.getPassword().startsWith("$2a$"))
    			throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
            
            if(!BCrypt.checkpw(userSignIn.getPassword(), user.getPassword() )) {
            	return new DataBaseResponse(false, "Invalid Password", 202);
            }
            
             return new DataBaseResponse(true, "Success", 200);
       

        }catch (SQLException e){
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














