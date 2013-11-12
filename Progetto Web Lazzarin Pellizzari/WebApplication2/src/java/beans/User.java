package beans;

import database.DBGroup;
import database.DBUser;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import javax.faces.application.ViewExpiredException;
import javax.faces.event.ActionEvent;

public class User {

    private int id;
    private String username;
    private String passwd;
    private String email;
    private Date nascita;
    private String indirizzo;
    private String citta;
    private String provincia;
    private String stato;
    private boolean cancellato;
    private Timestamp last_login;
    private Timestamp last_logout;
    private ArrayList<Group> gruppi;
    private String grouptitle = "";
    private String description = "";
    private int tmpidgroupdelete;
    private String aggiorna;
    private boolean admin_group;

    public boolean getAdmin_group() {
        return admin_group;
    }

    public void setAdmin_group(boolean admin_group) {
        this.admin_group = admin_group;
    }

    public ArrayList<Group> getGruppi() {
        DBGroup db = new DBGroup();
        db.loadGroup(this);
        return gruppi;
    }

    public void setGruppi(ArrayList<Group> gruppi) {
        this.gruppi = gruppi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAggiorna() {
        return aggiorna;
    }

    public void setAggiorna(String aggiorna) {
        this.aggiorna = aggiorna;
    }

    public boolean isCancellato() {
        return cancellato;
    }

    public void setCancellato(boolean cancellato) {
        this.cancellato = cancellato;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Date getNascita() {
        return nascita;
    }

    public void setNascita(Date nascita) {
        this.nascita = nascita;
    }

    public String getGrouptitle() {
        return grouptitle;
    }

    public void setGrouptitle(String grouptitle) {
        this.grouptitle = grouptitle;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public int getTmpidgroupdelete() {
        return tmpidgroupdelete;
    }

    public void setTmpidgroupdelete(int tmpidgroupdelete) {
        this.tmpidgroupdelete = tmpidgroupdelete;
    }

    public Timestamp getLast_login() {
        return last_login;
    }

    public void setLast_login(Timestamp last_login) {
        this.last_login = last_login;
    }

    public Timestamp getLast_logout() {
        return last_logout;
    }

    public void setLast_logout(Timestamp ultimo_refresh) {
        this.last_logout = ultimo_refresh;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /** Creates a new instance of User */
    public User() {
        //gruppi = new ArrayList<Gruppo>();
        id = -1;
    }

    public boolean save() {
        try {
            DBUser db = new DBUser();
            if (db.saveUser(this)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public String login() {

        /*try {
        DBUser db = new DBUser();
        if (db.login(this)) {
        return "success";
        } else {
        return "failure";
        }
        } catch (Exception e) {
        return "failure";
        }*/
        throw new NumberFormatException();

    }

    public String logout() {
        try {
            DBUser db = new DBUser();
            db.logout(id);
            db.close();
            return "success";
        } catch (Exception e) {
            return "failure";
        }
    }

    public String addGroup() {
        if (!"".equals(grouptitle)) {
            DBGroup g = new DBGroup();
            g.createGroup(this);
            this.setGrouptitle("");
            gruppi = null;
            g.loadGroup(this);
            return "success";
        } else {
            return "failure";
        }
    }

    public String editGroup(int group_id) {
        if (!"".equals(grouptitle)) {
            for (Group g : gruppi) {
                if (g.getId_gruppo() == group_id) {
                    g.setTitolo(this.grouptitle);
                    g.setDescription(this.description);
                    if (g.applyChanges()) {
                        return "backto_posts";
                    } else {
                        return "failure";
                    }
                }
            }
        } else {
            return "failure";
        }
        return "failure";
    }

    public void setAttributeId(ActionEvent event) {
        this.tmpidgroupdelete = ((Integer) event.getComponent().getAttributes().get("cacca")).intValue();
    }

    public String deleteGroup(int id_group) {
        DBGroup g = new DBGroup();
        if (g.deleteGroup(id_group)) {
            gruppi = null;
            g.loadGroup(this);
            return "back_to_grp_list";
        } else {
            return "failure";
        }
    }

    public boolean isAdminGroup(int id_group) {
        DBGroup db = new DBGroup();
        db.loadGroup(this);

        for (Group g : gruppi) {
            if ((g.getId_gruppo() == id_group) && (g.getId_admin() == this.id)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdminLevel2(int id_group) {
        DBGroup db = new DBGroup();
        return db.isAdminLevel2(this, id_group);
    }

    public String promoteAdminL2(int id_group) {
        DBGroup db = new DBGroup();
        System.out.println(" >>>> 0");
        return db.promoteAdminL2(this, id_group);
    }

    public boolean dePromoteAdminL2(int id_group) {
        DBGroup db = new DBGroup();
        return db.dePromoteAdminL2(this, id_group);
    }

    public boolean isInGroup(int id_group) {
        DBGroup db = new DBGroup();

        return db.isInGroup(this.id, id_group);

    }

    public boolean canDeleteFile(int id_group, int id_owner_file) {
        DBUser db = new DBUser();

        if (id_owner_file == this.id) {
            return true;
        }

        boolean admin_level_uno = isAdminGroup(id_group);

        if (admin_level_uno) {
            return true;
        }

        boolean temp = db.isAdminLevelDue(id_group, this);
        return temp;
    }

    public String deInvite(int id_group) {
        DBGroup db = new DBGroup();
        db.deinviteUser(this.id, id_group);
        return "ok";
    }

    public String date() {
        return Calendar.getInstance().getTimeInMillis() + "";
    }

    public String deleteFile(int id_file) {
        System.out.println("Tento di eliminare il file");
        DBUser db = new DBUser();
        return db.deleteFile(id_file) ? "ok" : "failure";
    }
}
