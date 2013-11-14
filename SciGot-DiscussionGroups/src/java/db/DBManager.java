/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ANDre1
 */
public class DBManager implements Serializable {

    // transient == non viene serializzato
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement stm = null;
    private ResultSet resultSet = null;

    public DBManager() throws SQLException, Exception {
        //String driver = "org.apache.derby.jdbc.EmbeddedDriver";


        String url = "jdbc:mysql://localhost:3306/scigot";
        String userpass = "root";
        try {
            System.out.println("Loading driver...");
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
        try {
            // This will load the MySQL driver, each DB has its own driver
            // Setup the connection with the DB
            connect = DriverManager.getConnection(url, userpass, userpass);
            System.out.println("#################################################################################################################################################################################################################################################################################################################################################");
            // Statements allow to issue SQL queries to the database


        } catch (Exception e) {
            throw e;
        }


    }

    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
        }
    }

    public static void shutdown() {

        try {
            DriverManager.getConnection("jdbc:mysql://localhost:8889/SciGot;shutdown=true");
        } catch (SQLException ex) {
            Logger.getLogger(db.DBManager.class.getName()).info(ex.getMessage());
        }

    }

    /**
     * Autentica un utente in base a un nome utente e a una password
     *
     * @param username il nome utente
     * @param password la password
     * @return null se l'utente non è autenticato, un oggetto User se l'utente
     * esiste ed è autenticato
     */
    public User authenticate(String username, String password) throws SQLException {

        // usare SEMPRE i PreparedStatement, anche per query banali. 
        // *** MAI E POI MAI COSTRUIRE LE QUERY CONCATENANDO STRINGHE !!!! 

        stm = connect.prepareStatement("SELECT * FROM scigot.utente WHERE Username = ? AND password = ?");

        try {
            stm.setString(1, username);
            stm.setString(2, password);
            ResultSet rs = stm.executeQuery();

            try {
                if (rs.next()) {
                    User user = new User();
                    user.setUserName(username);
                    user.setName(rs.getString("Nome_completo"));
                    user.setId(rs.getString("Id_utente"));
                    return user;
                } else {
                    return null;
                }
            } finally {
                // ricordarsi SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }

        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

    }

    /**
     * Visualizza i gruppi a cui un utente appartiene..
     *
     * @param req l'id dell'utente
     */
    public List<Group> trovaGruppo(HttpServletRequest req) throws SQLException {

        HttpSession session = req.getSession(true);
        stm = connect.prepareStatement("SELECT * FROM (scigot.gruppo G INNER JOIN scigot.gruppo_utente GU ON G.Id_gruppo=GU.Id_gruppo) WHERE GU.Id_utente = ?");
        List<Group> groups = new ArrayList<Group>();
        try {
            stm.setString(1, (session.getAttribute("userid").toString()));

            ResultSet rs = stm.executeQuery();

            try {
                while (rs.next()) {
                    Group g = new Group();
                    g.setName(rs.getString("Nome"));
                    g.setProprietario(rs.getString("Id_proprietario"));
                    g.setId(rs.getString("Id_gruppo"));
                    groups.add(g);
                }
            } finally {
                // ricordarsi SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }

        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return groups;
    }
    
    public List<Invito> trovaInvito(HttpServletRequest req) throws SQLException { 
    
    HttpSession session = req.getSession(true);
        stm = connect.prepareStatement("SELECT * FROM ((scigot.inviti I INNER JOIN scigot.gruppo G on I.Id_gruppo=G.Id_gruppo)INNER JOIN scigot.utente U ON G.Id_proprietario = U.Id_utente) WHERE I.Id_utente = ?");
        List<Invito> inviti = new ArrayList<Invito>();
        try {
            stm.setString(1, (session.getAttribute("userid").toString()));

            ResultSet rs = stm.executeQuery();

            try {
                while (rs.next()) {
                    Invito i = new Invito();
                    i.setNomeGruppo(rs.getString("Nome"));
                    i.setOwner(rs.getString("Nome_completo"));
                    i.setAccettato(rs.getString("Accettato"));
                    inviti.add(i);
                }
            } finally {
                // ricordarsi SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }

        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return inviti;
    
    
    }
}