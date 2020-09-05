package net.obrien.facebookclone.dao;

import net.obrien.facebookclone.model.User;
import net.obrien.facebookclone.model.User.UserBuilder;
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
 * This DAO class provides CRUD database operations for User sign 
 * up and authentication. it extends the DataBaseConnector class which 
 * has methods that enables database connection and setup
 * @author O'Brien
 *
 */
public class AuthDAO extends DataBaseConnector {

    public AuthDAO(String jdbcURL, String jdbcUsername, String jdbcPassword, String jdbcDatabase) {
        super(jdbcURL, jdbcUsername, jdbcPassword, jdbcDatabase);
    }

    /**
     * A create operation, validates user informtion and adds new users to the database
     * Method first checks if the user is unique by checking if their email and pone number
     * already exists in the database, It also hashes the password before saving the dails to
     * the datbase
     * @param newUser
     * @return DataBaseResponse object
     */
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


    /**
     * Checks if user provided email already exists in the database
     * @param email 
     * @return a boolean 
     * @throws SQLException
     */
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

    /**
     * Checks if user provided mobile number already exists in the database
     * @param mobile
     * @return boolean
     * @throws SQLException
     */
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

    /**
     * Takes in user login credentials and compares it to database records
     * Method also compares the hashed password in the database to the user 
     * provided password
     * @param userSignIn
     * @return DataBaseResponse object
     */
    public DataBaseResponse userLogin(UserSignIn userSignIn) {
    	
    	PreparedStatement statement = null;
    	User user;
        try{
            connect();
           
            String GET_USER = "select id, about, birthdate, email, firstname, lastname, mobile, password_hash, sex from users where email = ? ;";
            
            statement = jdbcConnection.prepareStatement(GET_USER);
            statement.setString(1, userSignIn.getEmail());
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
            	int id = resultSet.getInt("id");
                String about = resultSet.getString("about");
                String email = resultSet.getString("email");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String birthdate = resultSet.getString("birthdate");
                String mobile = resultSet.getString("mobile");
                String password_hash = resultSet.getString("password_hash");
                String sex = resultSet.getString("sex");
              
                user = new  User.UserBuilder()
                		.setId(id).setAbout(about).setEmail(email).setFirstname(firstname).setPassword(password_hash)
                		.setLastname(lastname).setBirthdate(birthdate).setMobile(mobile).setSex(sex).build();
                
            }else {
            	
            	return new DataBaseResponse(false, "Invalid Email", 202);
            }
                  
            
            if(null == user.getPassword() || !user.getPassword().startsWith("$2a$"))
    			throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
            
            if(!BCrypt.checkpw(userSignIn.getPassword(), user.getPassword() )) {
            	return new DataBaseResponse(false, "Invalid Password", 202);
            }
            
             return new DataBaseResponse(true, "Success", 200, user);
       

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














