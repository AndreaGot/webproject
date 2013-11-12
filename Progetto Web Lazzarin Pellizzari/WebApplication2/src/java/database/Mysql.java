package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mysql {

    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String DBNAME = "forum";
    private static final String USERDB = "root";
    private static final String PASSWORDDB = "verbatim";
    private static Connection con = null;
    private static int active_connections = 0;

    public Mysql() {
    }

    public static Connection getInstance() {
        try {
            if ((con == null) || (con.isClosed())) {
                con = connect(HOST, PORT, DBNAME, USERDB, PASSWORDDB);

            }
            active_connections++;
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return con;
        }

    }

    private static Connection connect(String host, int port, String dbName, String user, String passwd) {
        Connection dbConnection = null;
        String dbString = null;
        Properties p;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            dbString = "jdbc:mysql://" + host + "/" + dbName;
            p = new Properties();
            p.setProperty("user", user);
            p.setProperty("password", passwd);
            p.setProperty("autoReconnect", "true");
            //dbConnection = DriverManager.getConnection(dbString, user, passwd);
            dbConnection = DriverManager.getConnection(dbString, p);
        } catch (Exception e) {
            System.err.println("Failed to connect with the DB");
            e.printStackTrace();
        }
        return dbConnection;
    }

    public void close() {
        if (active_connections <= 1) {
            try {
                con.close();
                con = null;
            } catch (SQLException ex) {
                Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        active_connections--;


    }
}
