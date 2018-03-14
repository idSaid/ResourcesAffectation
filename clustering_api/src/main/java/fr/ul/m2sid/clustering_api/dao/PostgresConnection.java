package fr.ul.m2sid.clustering_api.dao;

import java.sql.*;

public final class PostgresConnection {

    public Connection conn;
    private Statement statement;
    public static PostgresConnection db;

    private PostgresConnection() {
        String url= "jdbc:postgresql://92.222.86.67:5432/";
        //String url= "jdbc:postgresql://127.0.0.1:5432/";
        String dbName = "personnesagees";
        //String dbName = "test";
        String driver = "org.postgresql.Driver";
        String userName = "postgres";
        String password = "toJIN";
        try {
            Class.forName(driver).newInstance();
            this.conn = (Connection) DriverManager.getConnection(url+dbName,userName,password);
        }
        catch (Exception sqle) {
            sqle.printStackTrace();
        }
    }
    /**
     *
     * @return PostgresConnexion Converter connection object
     */
    public static synchronized PostgresConnection getDbCon() {
        if ( db == null ) {
            db = new PostgresConnection();
        }
        return db;
    }
    /**
     *
     * @param query String The query to be executed
     * @return a ResultSet object containing the results or null if not available
     * @throws SQLException
     */
    public ResultSet query(String query) throws SQLException{
        statement = db.conn.createStatement();
        ResultSet res = statement.executeQuery(query);
        return res;
    }

    /**
     * @desc Method to insert data to a table
     * @param insertQuery String The Insert query
     * @return boolean
     * @throws SQLException
     */
    public int insert(String insertQuery) throws SQLException {
        statement = db.conn.createStatement();
        int result = statement.executeUpdate(insertQuery);
        return result;
    }

}
