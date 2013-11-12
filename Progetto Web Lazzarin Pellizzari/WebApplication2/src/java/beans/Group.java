package beans;

import database.DBGroup;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class Group {

    private int id_gruppo;
    private String titolo;
    private boolean cancellato;
    private int id_admin;
    private String description = "";
    private int new_files = 0;
    private int new_posts = 0;

    public ArrayList<User> invitable_users(int id_group) {
        DBGroup db = new DBGroup();
        return db.getInvitableUsers(id_group);

    }

    public boolean isCancellato() {
        return cancellato;
    }

    public void setCancellato(boolean cancellato) {
        this.cancellato = cancellato;
    }

    public int getId_admin() {
        return id_admin;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    public int getId_gruppo() {
        return id_gruppo;
    }

    public void setId_gruppo(int id_gruppo) {
        this.id_gruppo = id_gruppo;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNew_files() {
        return new_files;
    }

    public void setNew_files(int new_files) {
        this.new_files = new_files;
    }

    public int getNew_posts() {
        return new_posts;
    }

    public void setNew_posts(int new_posts) {
        this.new_posts = new_posts;
    }

    public boolean applyChanges() {
        DBGroup g = new DBGroup();
        if (g.editGroup(this)) {
            return true;
        } else {
            return false;
        }
    }

    public LinkedList<User> users(int id_group) {
        DBGroup db = new DBGroup();
        LinkedList<User> a = db.associatedUsers(id_group);
        return a;
    }

    public String invite_user(int id_group, User u) {
        //recupero l'ip del server tomcat
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ip = httpServletRequest.getServerName();
        ip += ":" + httpServletRequest.getServerPort();

        //procedo ad invitare l'utente
        DBGroup db = new DBGroup();
        db.invite_user(id_group, u, ip);

        return "ok";
    }
}
