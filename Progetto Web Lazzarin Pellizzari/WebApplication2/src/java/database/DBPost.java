package database;

import beans.Post;
import beans.Posts;
import beans.User;
import beans.upload.MyFile;
import com.mysql.jdbc.Statement;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class DBPost extends Mysql {

    public boolean addPost(Post p) {
        boolean out = true;

        return out;
    }

    public boolean addPost(String testo, int id_user, int id_group) {
        boolean out = true;

        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("INSERT INTO discussions (id_group, id_user, discussion_text, discussion_type) values (?,?,?,0)");
            stmt.setInt(1, id_group);
            stmt.setInt(2, id_user);
            stmt.setString(3, testo);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            out = false;
            Logger.getLogger(DBPost.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return out;
        }
    }

    public boolean setPosts(Posts posts) {
        boolean out = true;
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpSession session = httpServletRequest.getSession(false);
            User u = (User) session.getAttribute("user");
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("SELECT title FROM groups WHERE id_group = ?");
            stmt.setInt(1, posts.getPostId());

            ResultSet res = stmt.executeQuery();
            res.next();
            posts.setGroupName(res.getString("title"));

            PreparedStatement stmt1 = Mysql.getInstance().prepareStatement("SELECT * FROM discussions inner join users on id_user = id WHERE id_group = ? AND discussions.cancelled = 0 AND discussion_type = 0 ORDER BY publication_data DESC");
            stmt1.setInt(1, posts.getPostId());
            res = stmt1.executeQuery();
            ArrayList<Post> plist = new ArrayList();
            while (res.next()) {
                Post p = new Post();
                //qui verifichiamo la presenza di eventuali $$name$$ e li sostituiamo se del caso
                p.setTesto(checkFileIncludeRequest(res.getString("discussion_text"), posts.getPostId()));
                p.setId_user(res.getInt("id_user"));
                p.setPubDate(res.getTimestamp("publication_data"));
                p.setUsername(res.getString("username"));
                p.setLocalita(res.getString("citta") + "," + res.getString("provincia") + "," + res.getString("stato"));
                plist.add(p);
            }
            posts.setPosts(plist);

            PreparedStatement stmt2 = Mysql.getInstance().prepareStatement("SELECT * FROM discussions d INNER JOIN files f ON d.id_file = f.id_file INNER JOIN users u ON d.id_user = u.id WHERE id_group = ? AND d.cancelled = 0 ORDER BY publication_data DESC");
            stmt2.setInt(1, posts.getPostId());
            res = stmt2.executeQuery();
            ArrayList<MyFile> flist = new ArrayList();
            while (res.next()) {
                MyFile f = new MyFile();
                f.setName(res.getString("name"));
                f.setOwner(res.getString("username"));
                f.setId(res.getInt("f.id_file"));
                f.setId_owner(res.getInt("id_user"));

                flist.add(f);
            }
            posts.setFiles(flist);

            PreparedStatement stmt3 = Mysql.getInstance().prepareStatement("DELETE FROM last_access_infos WHERE id_user = ? AND id_group = ?");
            stmt3.setInt(1, u.getId());
            stmt3.setInt(2, posts.getCurrentIdGroup());

            stmt3.execute();

            stmt3 = Mysql.getInstance().prepareStatement("INSERT INTO last_access_infos (id_user, id_group, last_access) VALUES (?, ?, CURRENT_TIMESTAMP)");
            stmt3.setInt(1, u.getId());
            stmt3.setInt(2, posts.getCurrentIdGroup());

            stmt3.execute();




        } catch (SQLException ex) {
            out = false;
            Logger.getLogger(DBPost.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return out;
        }

    }

    private String checkFileIncludeRequest(String testo, int group_id) {
        StringBuilder s = new StringBuilder(testo);
        int index1 = 0, index2 = 0, from = 0;

        index1 = s.indexOf("$$", from);
        index2 = s.indexOf("$$", index1 + 1);
        
        while ((index1 != -1) && (index2 != -1)) {

            if ((index1 + 2) < index2) {
                String file = s.substring(index1 + 2, index2);
                File f = new File(beans.upload.FileUpload.filePathSave + group_id + "/" + file);
                if (f.exists()) {
                    s.replace(index1, index2 + 2, "<img align=\"center\"src=\"../../imgs/document.png\"><a style=\"color: black;\"href=\"" + beans.upload.FileUpload.filePathFind + group_id + "/" + file + "\">" + file + "</a>");
                } else {
                    s.replace(index1, index2 + 2, "<img align=\"center\"src=\"../../imgs/document_not_found.png\">" + file);
                }
            }

            index1 = s.indexOf("$$", from);
            index2 = s.indexOf("$$", index1 + 1);
            from = index2 + 2;

        }
        return s.toString();
    }

    public boolean addAllegato(String name, int iduser, int gruppo, String uri) {
        try {
            PreparedStatement stmt = Mysql.getInstance().prepareStatement("INSERT INTO files (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.executeUpdate();

            ResultSet res = stmt.getGeneratedKeys();
            res.next();
            int idFile = res.getInt(1);

            stmt = Mysql.getInstance().prepareStatement("INSERT INTO discussions (discussion_type, id_file, id_user, id_group) VALUES (2, ?, ?, ?)");
            stmt.setInt(1, idFile);
            stmt.setInt(2, iduser);
            stmt.setInt(3, gruppo);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBPost.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
