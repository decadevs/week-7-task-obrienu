package net.obrien.facebookclone.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.*;

import net.obrien.facebookclone.model.User;
import net.obrien.facebookclone.model.UserSignIn;
import net.obrien.facebookclone.utils.DataBaseResponse;

/**
 * This Class contains tests for the Auth DAO Class
 * @author obrien
 *
 */
public class AuthDAOTest {
	AuthDAO authDAO;
	String jdbcURL = "jdbc:mysql://localhost:3306/";
    String jdbcUsername = "root";
    String jdbcPassword = "ulelek10";
    String jdbcDatabase = "test_facebook";
    User user;
    String bdt = "28/12/1900";
    String firstname = "Obrien";
    String lastname = "Longe";
    String sex = "Male";
    String email = "hello@hello";
    String password = "hello123";
    String mobile = "09098989898";
    Statement statement;
    PreparedStatement prepStatement;
    ResultSet resultSet;
	
	@BeforeEach
	void init() {
		
		try {
			authDAO = new AuthDAO(jdbcURL,jdbcUsername, jdbcPassword,  jdbcDatabase);
			user = new User.UserBuilder().setFirstname(firstname).setLastname(lastname)
					.setBirthdate(bdt).setEmail(email).setPassword(password).setMobile(mobile).setSex(sex).build();
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	@AfterEach
	void tearDownDB(){
		try {
			
			authDAO.tearDownDB();
			authDAO.disconnect();
		}catch(Exception e) {
			e.getMessage();
			
		}
		
	}
	
	@Test
	@DisplayName("Test registerNewUser Method")
	void registerNewUser(){
		
		try {
			authDAO.registerNewUser(user);
			authDAO.connect();
			prepStatement = authDAO.jdbcConnection.prepareStatement("SELECT email, firstname from users where email = ? ;");
			prepStatement.setNString(1, user.getEmail());
			resultSet = prepStatement.executeQuery();
			while(resultSet.next()) {
				Assertions.assertEquals(user.getEmail(),resultSet.getString(1), "Method should add new user to database" );
				Assertions.assertEquals(user.getFirstname(),resultSet.getString(2), "Method should add new user to database");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	@DisplayName("Test registerNewUser Method For Duplicate User Registration")
	void testRegisterNewUserForMultipleUserRegistration(){
		
		try {
			authDAO.registerNewUser(user);
			authDAO.connect();
			authDAO.registerNewUser(user);
			authDAO.connect();
			prepStatement = authDAO.jdbcConnection.prepareStatement("SELECT count(*) from users ;");
			
			resultSet = prepStatement.executeQuery();
			while(resultSet.next()) {
				Assertions.assertEquals(1,resultSet.getInt(1), "Only one user should exist in the database" );
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	@DisplayName("Test User Login")
	void testUserLogin(){
		
		try {
			authDAO.registerNewUser(user);
			authDAO.connect();
			UserSignIn userSignIn = new UserSignIn(user.getEmail(), user.getPassword());
			DataBaseResponse databaseResponse = authDAO.userLogin(userSignIn);
			User returnedUser = (User) databaseResponse.getData();
			
				Assertions.assertTrue(databaseResponse.isStatus(), "Login Status should be true");
				Assertions.assertEquals(user.getEmail(), returnedUser.getEmail(), "Login should return same user");
				Assertions.assertEquals(user.getMobile(), returnedUser.getMobile(), "Login should return same user");
				Assertions.assertEquals(user.getFirstname(), returnedUser.getFirstname(), "Login should return same user");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Test User Login With Wrong Login Details")
	void testUserLoginWithWrongDetails(){
		
		try {
			authDAO.registerNewUser(user);
			authDAO.connect();
			UserSignIn userSignIn = new UserSignIn(user.getEmail(), "Hellonjcbhh");
			DataBaseResponse databaseResponse = authDAO.userLogin(userSignIn);
	
				Assertions.assertFalse(databaseResponse.isStatus(), "Login Status should be true");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
