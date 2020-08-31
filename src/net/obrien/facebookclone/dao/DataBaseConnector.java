package net.obrien.facebookclone.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DataBaseConnector {

    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    protected Connection jdbcConnection;

    public DataBaseConnector(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }


    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
        	     	
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
       
            }
            catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            
            jdbcConnection = DriverManager.getConnection(
                    jdbcURL, jdbcUsername, jdbcPassword);
        }
    }


    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

}
