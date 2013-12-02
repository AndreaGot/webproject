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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        stm = connect.prepareStatement("SELECT * FROM scigot.utente WHERE Username = ? AND (BINARY password = ?)");

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

        HttpSession session = req.getSession(false);
        stm = connect.prepareStatement("SELECT * FROM ((scigot.inviti I INNER JOIN scigot.gruppo G on I.Id_gruppo=G.Id_gruppo)INNER JOIN scigot.utente U ON G.Id_proprietario = U.Id_utente) WHERE I.Id_utente = ? AND I.Accettato = 0");
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
                    i.setIdGruppo(rs.getString("Id_gruppo"));
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

    public List<Post> trovaPost(HttpServletRequest req) throws SQLException {


        stm = connect.prepareStatement("SELECT * FROM (scigot.post P INNER JOIN scigot.utente U on U.Id_utente=P.Id_autore) WHERE P.Id_gruppo = ? ORDER BY P.Id_post;");
        List<Post> posts = new ArrayList<Post>();
        try {
            stm.setString(1, (req.getParameter("view").toString()));

            ResultSet rs = stm.executeQuery();

            try {
                while (rs.next()) {
                    Post p = new Post();
                    p.setId(rs.getString("Id_post"));
                    p.setAutore(rs.getString("Nome_completo"));
                    p.setContenuto(rs.getString("Contenuto"));
                    p.setData(rs.getDate("data"));
                    posts.add(p);

                }
            } finally {
                // ricordarsi SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }

        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return posts;


    }

    public Boolean settaInvito(HttpServletRequest req, String id, String risposta) throws SQLException {
        HttpSession session = req.getSession(true);

        stm = connect.prepareStatement("UPDATE `inviti` SET `Accettato`= ? WHERE `Id_gruppo` = ? AND `Id_utente` = ?");
        try {
            stm.setString(1, risposta);
            stm.setString(2, id);
            stm.setString(3, (session.getAttribute("userid").toString()));

            stm.executeUpdate();


        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return true;
    }

    public Boolean inserisciUtente(HttpServletRequest req, String id) throws SQLException {
        HttpSession session = req.getSession(false);

        stm = connect.prepareStatement("INSERT INTO gruppo_utente (`Id_gruppo`, `Id_utente`, `Ruolo`) VALUES (?,?,'User');");
        try {
            stm.setString(1, id);
            stm.setString(2, (session.getAttribute("userid").toString()));

            stm.executeUpdate();


        } catch (Exception e) {
            return false;
        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return true;
    }

    public Boolean settaNomeGruppo(HttpServletRequest req) throws SQLException {
        HttpSession session = req.getSession(false);

        stm = connect.prepareStatement("UPDATE `gruppo` SET `Nome`= ? WHERE `Id_gruppo` = ? AND `Id_proprietario` = ?");
        try {
            stm.setString(1, req.getParameter("nome"));
            stm.setString(2, req.getParameter("id"));
            stm.setString(3, (session.getAttribute("userid").toString()));

            stm.executeUpdate();


        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return true;
    }

    public String trovaIdDaNomeUtente(HttpServletRequest req) throws SQLException {
        stm = connect.prepareStatement("SELECT Id_utente FROM utente WHERE Username = ?");
        String ID;
        try {
            stm.setString(1, req.getParameter("nome"));

            ResultSet rs = stm.executeQuery();

            try {
                while (rs.next()) {
                    ID = (rs.getString("Id_utente"));
                    return ID;
                }
            } finally {
                // ricordarsi SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }

        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return "";
    }

    public String trovaIdDaGruppo(HttpServletRequest req) throws SQLException {
        stm = connect.prepareStatement("SELECT Id_gruppo FROM gruppo WHERE Nome = ?");
        String ID;
        try {
            stm.setString(1, req.getParameter("creaGruppoTextbox"));

            ResultSet rs = stm.executeQuery();

            try {
                while (rs.next()) {
                    ID = (rs.getString("Id_gruppo"));
                    return ID;
                }
            } finally {
                // ricordarsi SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }

        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return "";
    }

    public Boolean inserisciInvito(HttpServletRequest req, String id) throws SQLException {


        stm = connect.prepareStatement("INSERT INTO `inviti`(`Id_gruppo`, `Id_utente`, `Accettato`) VALUES (?,?,'0')");
        try {
            stm.setString(1, req.getParameter("idgruppo"));
            stm.setString(2, id);

            stm.executeUpdate();


        } catch (Exception e) {
            return false;
        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return true;
    }

    public Boolean inserisciGruppo(HttpServletRequest req) throws SQLException {
        HttpSession session = req.getSession(false);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();

        stm = connect.prepareStatement("INSERT INTO `gruppo`( `Nome`, `Id_proprietario`, `Data_creazione`) VALUES (?,?,?)");
        try {
            stm.setString(1, req.getParameter("creaGruppoTextbox"));
            stm.setString(2, session.getAttribute("userid").toString());
            stm.setString(3, dateFormat.format(date));

            stm.executeUpdate();


        } catch (Exception e) {
            return false;
        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return true;
    }

    public List<User> trovaUtente(HttpServletRequest request) throws SQLException {

        // usare SEMPRE i PreparedStatement, anche per query banali. 
        // *** MAI E POI MAI COSTRUIRE LE QUERY CONCATENANDO STRINGHE !!!! 

        stm = connect.prepareStatement("select U.username, U.Nome_completo, U.Id_utente from utente U INNER JOIN gruppo_utente GU ON U.Id_utente = GU.Id_utente WHERE GU.Id_gruppo = ? ");
        List<User> users = new ArrayList<User>();
        try {
            stm.setString(1, request.getParameter("id"));
            ResultSet rs = stm.executeQuery();

            try {
                while (rs.next()) {
                    User user = new User();
                    user.setName(rs.getString("Nome_completo"));
                    user.setUserName(rs.getString("username"));
                    user.setId(rs.getString("Id_utente"));
                    users.add(user);
                }
            } finally {
                // ricordarsi SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }

        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return users;
    }

    public String numeroPost(HttpServletRequest request,String id) throws SQLException {

        HttpSession session = request.getSession(false);
        
        stm = connect.prepareStatement("select count(*) as post from post where Id_autore = ? and Id_gruppo =?");
        try {
            stm.setString(1, id);
            stm.setString(2, session.getAttribute("idgruppo").toString());

            ResultSet rs = stm.executeQuery();

            try {
                if (rs.next()) {

                    return rs.getString("post");
                } else {
                    return null;
                }
            } finally {
                // ricordarsi SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }


        } catch (Exception e) {
            return null;
        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

    }
    
    
     public Boolean inserisciPost(HttpServletRequest req, String post) throws SQLException {
        HttpSession session = req.getSession(false);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();

        stm = connect.prepareStatement("INSERT INTO `post`( `Id_gruppo`, `Id_autore`,`contenuto` ,`Data`) VALUES (?,?,?,?)");
        try {
            stm.setString(1, req.getParameter("passaID"));
            stm.setString(2, session.getAttribute("userid").toString());
            stm.setString(3,post);
            stm.setString(4, dateFormat.format(date));

            stm.executeUpdate();


        } catch (SQLException e) {
            return false;
        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return true;
    }
    
    public Boolean inserisciFile(HttpServletRequest req, String path, String nome) throws SQLException {
        HttpSession session = req.getSession(false);

        stm = connect.prepareStatement("INSERT INTO `post_file`( `Id_gruppo`, `File`,`Nome`,`id_autore`) VALUES (?,?,?,?)");
        try {
            stm.setString(1, session.getAttribute("idgruppo").toString());
            stm.setString(2,path);
            stm.setString(3,nome);
            stm.setString(4, session.getAttribute("userid").toString());

            stm.executeUpdate();


        } catch (SQLException e) {
            return false;
        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }
        return true;
    }
    
    
    
    public String trovaFileLink(HttpServletRequest request,String id) throws SQLException {

        
        stm = connect.prepareStatement("SELECT `File` FROM `post_file` WHERE `Nome`= ? AND `Id_gruppo` = ?");
        try {
            stm.setString(1, id);
            stm.setString(2, request.getParameter("passaID").toString());

            ResultSet rs = stm.executeQuery();

            try {
                if (rs.next()) {

                    return rs.getString("File");
                } else {
                    return null;
                }
            } finally {
                // ricordarsi SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }


        } catch (Exception e) {
            return null;
        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

    }
    
    public String dataPost(HttpServletRequest request,String id) throws SQLException {

        HttpSession session = request.getSession(false);
        
        stm = connect.prepareStatement("SELECT `Data` FROM `post` WHERE `Id_autore` = ? and `Id_gruppo` =? ORDER BY `Data` DESC");
        try {
            stm.setString(1, id);
            stm.setString(2, session.getAttribute("idgruppo").toString());

            ResultSet rs = stm.executeQuery();

            try {
                if (rs.next()) {

                    return rs.getString("Data");
                } else {
                    return null;
                }
            } finally {
                // ricordarsi SEMPRE di chiudere i ResultSet in un blocco finally 
                rs.close();
            }


        } catch (Exception e) {
            return null;
        } finally {
            // ricordarsi SEMPRE di chiudere i PreparedStatement in un blocco finally 
            stm.close();
        }

    }

    
}
