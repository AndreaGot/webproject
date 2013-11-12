package database;

import beans.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Mailer;

public class DBGroup extends Mysql {

    public boolean createGroup(User user) {
        return createGroup(user.getGrouptitle(), user.getId(), user.getDescription());
    }

    public boolean createGroup(String titolo, int id_admin, String descrizione) {
        boolean out = true;
        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("INSERT INTO groups (title, id_admin, descrizione) VALUES (?,?,?)");
            stmt.setString(1, titolo);
            stmt.setInt(2, id_admin);
            stmt.setString(3, (descrizione == null ? "" : descrizione));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            out = false;
            Logger.getLogger(DBGroup.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return out;
        }
    }

    public boolean deleteGroup(Group gruppo) {
        return deleteGroup(gruppo.getId_gruppo());
    }

    public boolean deleteGroup(int id_gruppo) {
        boolean out = true;
        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("UPDATE groups SET cancellato = 1 WHERE id_group = ?");
            stmt.setInt(1, id_gruppo);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            out = false;
            Logger.getLogger(DBGroup.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return out;
        }
    }

    public boolean changeTitle(int id_gruppo, String titolo) {
        boolean out = true;
        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("UPDATE groups SET title = ? WHERE id_group = ?");
            stmt.setString(1, titolo);
            stmt.setInt(2, id_gruppo);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            out = false;
            Logger.getLogger(DBGroup.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return out;
        }
    }

    public boolean loadGroup(User u) {
        boolean out = false;
        ArrayList<Group> gruppi = new ArrayList();
        Group g;
        try {
            //PreparedStatement stmt = Mysql.getInstance().prepareStatement("SELECT DISTINCT g.id_group, g.title, g.id_admin, g.descrizione FROM groups g LEFT JOIN member_of m ON g.id_group = m.id_group WHERE (id_user = ? OR id_admin = ?) AND cancellato = 0 AND deleted = 0");
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("(SELECT id_group, title, id_admin, descrizione FROM groups WHERE id_admin = ? AND cancellato = 0) UNION (SELECT g.id_group, title, g.id_admin, descrizione FROM groups g INNER JOIN member_of m ON g.id_group = m.id_group WHERE id_user = ? AND cancellato = 0 AND deleted = 0 AND accepted = 1)");
            stmt.setInt(1, u.getId());
            stmt.setInt(2, u.getId());
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                g = new Group();
                //System.out.println(" <<< " + res.getInt("id_group"));
                g.setId_gruppo(res.getInt("id_group"));
                g.setId_admin(res.getInt("id_admin"));
                g.setTitolo(res.getString("title"));
                g.setDescription(res.getString("descrizione"));

                //ricavo quanti post e quanti file sono stati aggiunti rispetto all'ultimo logout...
                PreparedStatement stmt1 = Mysql.getInstance().prepareStatement("SELECT count(discussion_type) AS conteggio, discussion_type FROM discussions WHERE id_group = ? AND publication_data >= (SELECT MAX(last_access) FROM (SELECT last_access FROM last_access_infos WHERE id_user = ? AND id_group = ? UNION SELECT \"2008-01-01 00:00:00\") AS t) AND cancelled = 0 GROUP BY discussion_type");
                stmt1.setInt(1, g.getId_gruppo());
                stmt1.setInt(2, u.getId());
                stmt1.setInt(3, g.getId_gruppo());

                ResultSet res1 = stmt1.executeQuery();

                while (res1.next()) {
                    if (res1.getInt("discussion_type") == 0) {
                        g.setNew_posts(res1.getInt("conteggio"));
                    } else if (res1.getInt("discussion_type") == 2) {
                        g.setNew_files(res1.getInt("conteggio"));
                    }
                }

                gruppi.add(g);
            }

            u.setGruppi(gruppi);

        } catch (SQLException ex) {
            Logger.getLogger(DBGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out;
    }

    public boolean isInGroup(int id_user, int id_group) {

        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("SELECT * FROM users_group WHERE id_user = ? AND id_group = ?");
            stmt.setInt(1, id_user);
            stmt.setInt(2, id_group);
            ResultSet res = stmt.executeQuery();

            res.last();
            return res.getRow() != 0;

        } catch (SQLException ex) {
            Logger.getLogger(DBGroup.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean isAdminLevel2(User u, int id_group) {
        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("SELECT * FROM group_admin WHERE id_user = ? AND id_group = ?");
            stmt.setInt(1, u.getId());
            stmt.setInt(2, id_group);
            ResultSet res = stmt.executeQuery();

            res.last();

            if (res.getRow() == 0) {
                return false;
            } else {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBGroup.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public String promoteAdminL2(User u, int id_group) {
        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("INSERT INTO group_admin (id_user, id_group) VALUES (?, ?)");
            stmt.setInt(1, u.getId());
            stmt.setInt(2, id_group);
            stmt.execute();
            System.out.println(" >>>> 1");
            return "promosso";
        } catch (Exception ex) {
            System.out.println(" >>>> 2");
            Logger.getLogger(DBGroup.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return "nonpromosso";
        }

    }

    public boolean dePromoteAdminL2(User u, int id_group) {
        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("DELETE FROM group_admin WHERE id_user = ? AND id_group = ?");
            stmt.setInt(1, u.getId());
            stmt.setInt(2, id_group);
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBGroup.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }


    }

    public ArrayList<User> getInvitableUsers(int id_gruppo) {
        ArrayList<User> invitables = new ArrayList();

        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("SELECT * FROM users WHERE users.id NOT IN (SELECT id_user FROM member_of WHERE id_group = ? AND deleted = 0) AND users.id NOT IN (SELECT groups.id_admin FROM groups WHERE id_group = ?) ORDER BY username");
            stmt.setInt(1, id_gruppo);
            stmt.setInt(2, id_gruppo);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                User u = new User();
                u.setId(res.getInt("id"));
                u.setUsername(res.getString("username"));
                u.setEmail(res.getString("email"));
                invitables.add(u);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBGroup.class.getName()).log(Level.SEVERE, null, ex);
        }

        return invitables;
    }

    public boolean editGroup(Group g) {

        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("UPDATE groups SET title=?, descrizione=? WHERE id_group=?");
            stmt.setString(1, g.getTitolo());
            stmt.setString(2, g.getDescription());
            stmt.setInt(3, g.getId_gruppo());
            //System.out.println(g.getTitolo() + " - " + g.getDescription() + " - " + g.getId_gruppo());
            int out = stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public LinkedList<User> associatedUsers(int id_group) {
        LinkedList<User> out = new LinkedList();

        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("SELECT * FROM (SELECT id_user, id_group, 0 AS admin FROM member_of WHERE id_group = ? AND deleted = 0 UNION (SELECT id_admin AS id_user, id_group, 1 AS admin FROM groups WHERE id_group = ?)) u INNER JOIN users ON u.id_user = users.id ORDER BY username;");

            stmt.setInt(1, id_group);
            stmt.setInt(2, id_group);

            //System.out.println(stmt.toString());

            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                User u = new User();
                u.setId(res.getInt("id"));
                u.setUsername(res.getString("username"));
                u.setAdmin_group(res.getInt("admin") == 0 ? false : true);
                out.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return out;
    }

    public void deinviteUser(int id_user, int id_group) {
        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("UPDATE member_of SET deleted = 1, accepted = 0 WHERE id_user = ? AND id_group = ?;");
            stmt.setInt(1, id_user);
            stmt.setInt(2, id_group);
            stmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//TODO: qui si dovrà gestire l'invio delle mail con hash calcolato e salvato nel database :)
    public void invite_user(int id_group, User u, String ip) {
        try {

            //invio la mail... se la stringa ritornata è != da null allora la mail è stata inviata
            String hash = Mailer.sendMail(u.getEmail(), ip);

            PreparedStatement stmt = Mysql.getInstance().prepareStatement("UPDATE member_of SET deleted = 0, hash = ?, accepted = 0 WHERE id_user = ? AND id_group = ? ;");
            stmt.setString(1, hash);
            stmt.setInt(2, u.getId());
            stmt.setInt(3, id_group);
            int updated = stmt.executeUpdate();

            if (updated == 0) {
                PreparedStatement stmt1 = Mysql.getInstance().prepareStatement("INSERT INTO member_of (id_user, id_group, hash) values (?,?,?);");
                stmt1.setInt(1, u.getId());
                stmt1.setInt(2, id_group);
                stmt1.setString(3, hash);
                stmt1.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
