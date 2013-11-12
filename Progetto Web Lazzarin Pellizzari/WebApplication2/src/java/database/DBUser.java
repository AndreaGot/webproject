package database;

import beans.User;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBUser extends Mysql {

    public boolean saveUser(String username, String password, String email, String indirizzo, String citta, String provincia, String stato, java.util.Date nascita) {
        boolean out = false;
        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("INSERT INTO users(username, password, email, indirizzo, citta, provincia, stato, nascita) VALUES (?, MD5(?), ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, indirizzo);
            stmt.setString(5, citta);
            stmt.setString(6, provincia);
            stmt.setString(7, stato);
            stmt.setDate(8, new Date(nascita.getTime()));
            int ret = stmt.executeUpdate();
            if (ret > 0) {
                out = true;
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return out;
        }
    }

    public boolean saveUser(User u) {
        return saveUser(u.getUsername(), u.getPasswd(), u.getEmail(), u.getIndirizzo(), u.getCitta(), u.getProvincia(), u.getStato(), u.getNascita());
    }

    public boolean login(User u) {
        boolean out = false;
        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("SELECT * FROM users WHERE username=? AND password=MD5(?) AND cancellato=0");
            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPasswd());
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                u.setStato(res.getString("stato"));
                u.setProvincia(res.getString("provincia"));
                u.setCitta(res.getString("citta"));
                u.setIndirizzo(res.getString("indirizzo"));
                u.setNascita(res.getDate("nascita"));
                u.setEmail(res.getString("email"));
                u.setId(res.getInt("id"));
                u.setLast_login(res.getTimestamp("last_login"));
                u.setLast_logout(res.getTimestamp("last_logout"));
                DBGroup g = new DBGroup();
                g.loadGroup(u);
                out = true;
                stmt = Mysql.getInstance().prepareStatement("UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE username = ?");
                stmt.setString(1, u.getUsername());
                stmt.execute();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out;
    }

    public boolean logout(int id) {
        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("UPDATE users set last_logout = CURRENT_TIMESTAMP where id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean uniqueUsername(String username) {
        PreparedStatement stmt;
        ResultSet res;
        try {
            stmt = Mysql.getInstance().prepareStatement("SELECT * FROM users WHERE username=? AND cancellato=0");
            stmt.setString(1, username);
            res = stmt.executeQuery();
            while (res.next()) {
                res.close();
                stmt.close();
                return false;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public boolean isAdminLevelDue(int id_group, User u) {
        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("SELECT * FROM group_admin WHERE id_group = ? AND id_user = ?");
            stmt.setInt(1, id_group);
            stmt.setInt(2, u.getId());
            ResultSet res = stmt.executeQuery();
            res.last();
            if (res.getRow() != 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;




    }

    public boolean deleteFile(int id_file) {
        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("UPDATE discussions SET cancelled = 1 WHERE id_file = ?");
            stmt.setInt(1, id_file);

            return (stmt.executeUpdate() > 0);
        } catch (Exception e) {
            return false;
        }
    }
}
