package net.obrien.facebookclone.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.obrien.facebookclone.utils.DatabaseTables;

/**
 * Handles connection to database. the class also handles  database
 * set up on first initialisation of app. It also has database teardown functionality 
 * for testing purpose
 * @author Obrien
 *
 */
public abstract class DataBaseConnector {

    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    private String jdbcDatabaseName;
    private String timeZone = "?serverTimezone=UTC";
    protected Connection jdbcConnection;

    public DataBaseConnector(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
        
    }
    
    public DataBaseConnector(String jdbcURL, String jdbcUsername, String jdbcPassword, String database_name) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
        this.jdbcDatabaseName = database_name;
        handleSetUp();
    }
    
    /**
     * Method connects to mysql database and checks if the specified database 
     * name exists. if it doesn't it calls the database setup nethod whic sets 
     * up the database
     */
    protected void handleSetUp()  {
        	     	
            try {
            	if (jdbcConnection == null || jdbcConnection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                jdbcConnection = DriverManager.getConnection(jdbcURL + timeZone, jdbcUsername, jdbcPassword);
                //String CHECK_FOR_DATABASE = "SHOW DATABASES LIKE ? ;";
                
                ResultSet resultSet = jdbcConnection.getMetaData().getCatalogs();
                while (resultSet.next()) {

                	String databaseName = resultSet.getString(1);

                	if(databaseName.equals(this.jdbcDatabaseName)) {
                		jdbcConnection.close();
                		return;
                	}

                	}
                setUpDatabase();      
            	}
            }catch (Exception e) {
                e.printStackTrace();
            }
            
   
    }
    
    /**
     * Method sets up a new database with all the required fields and 
     * tanles
     */
    private void setUpDatabase() {
    	String USE_DATABASE = "USE " + this.jdbcDatabaseName +";";
    	String CREATE_DATABASE = "CREATE DATABASE " + this.jdbcDatabaseName + ";";
    	Statement statement = null;
    	try {
    		jdbcConnection.setAutoCommit(false);
    		statement = jdbcConnection.createStatement();
    		statement.addBatch(CREATE_DATABASE);
    		statement.addBatch(USE_DATABASE);
    		statement.addBatch(DatabaseTables.USERS.get());
    		statement.addBatch(DatabaseTables.POSTS.get());
    		statement.addBatch(DatabaseTables.POST_LIKES.get());
    		statement.addBatch(DatabaseTables.COMMENTS.get());
    		statement.addBatch(DatabaseTables.COMMENTS_LIKES.get());
    		statement.executeBatch();
    		jdbcConnection.commit();
    	}catch(Exception e) {
    		try{
    			jdbcConnection.rollback();
    		}catch(Exception ex) {
    			ex.printStackTrace();
    		}
    		
    		e.printStackTrace();
    	}finally {
    		try {
    			if(statement != null) {
        			statement.close();	
        		}
    			if(jdbcConnection != null) {
    				jdbcConnection.close();
    			}
    		}catch(Exception ee){}
    		
    	}
    }


    /**
     * handles connnection to the database via JDBC Drive
     * @throws SQLException
     */
    protected void connect() throws SQLException {
    	
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
        	     	
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
       
            }
            catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            
            jdbcConnection = DriverManager.getConnection(
                    jdbcURL + this.jdbcDatabaseName + timeZone, jdbcUsername, jdbcPassword);
         
        }
    }


    /**
     * Handles disconnection from the database
     * @throws SQLException
     */
    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }
    
    /**
     * Method handles the tearing down of the database, used mainly
     * for testing purpose
     * @throws SQLException
     */
    protected void tearDownDB() throws SQLException {
    	
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
        
            Statement statement = jdbcConnection.createStatement();
            statement.executeUpdate("DROP DATABASE " + this.jdbcDatabaseName + " ;");
            statement.close();
            jdbcConnection.close();
        }
       
    }

}
