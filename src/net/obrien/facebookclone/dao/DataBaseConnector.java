package net.obrien.facebookclone.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import net.obrien.facebookclone.utils.DatabaseTables;

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
    
    protected void handleSetUp()  {
        
        	     	
            try {
            	if (jdbcConnection == null || jdbcConnection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                jdbcConnection = DriverManager.getConnection(jdbcURL + timeZone, jdbcUsername, jdbcPassword);
                String CHECK_FOR_DATABASE = "SHOW DATABASES LIKE ? ;";
                
                PreparedStatement statement = jdbcConnection.prepareStatement(CHECK_FOR_DATABASE);
                statement.setNString(1, this.jdbcDatabaseName);
                if(statement.executeUpdate() < 1) {
                	statement.close();
                	setUpDatabase();
                }else {
                	jdbcConnection.close();
                }
            	}
            }catch (Exception e) {
                e.printStackTrace();
            }
            
   
    }
    
    
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


    protected void connect() throws SQLException {
    	// String USE_DATABASE = "USE " + this.jdbcDatabaseName +";";
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
        	     	
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
       
            }
            catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            
            jdbcConnection = DriverManager.getConnection(
                    jdbcURL + this.jdbcDatabaseName + timeZone, jdbcUsername, jdbcPassword);
            // Statement statement = jdbcConnection.createStatement();
           // if(statement.execute(USE_DATABASE)) {
           // 	System.out.println("Connected to " + this.jdbcDatabaseName + " Database");
           // 	statement.close();
           // }
         //   System.out.println("Error Connected to " + this.jdbcDatabaseName + " Database");
        }
    }


    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

}
