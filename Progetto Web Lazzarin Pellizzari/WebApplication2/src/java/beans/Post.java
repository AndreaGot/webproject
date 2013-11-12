package beans;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Post {

    private int id_post;
    private Timestamp pubDate;
    private String returnDate;
    private String testo;
    private int id_user;
    private String username;
    private String localita;
    private int id_group;

    public String getLocalita() {
        return localita;
    }

    public void setLocalita(String localita) {
        this.localita = localita;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId_group() {
        return id_group;
    }

    public void setId_group(int id_group) {
        this.id_group = id_group;
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public Timestamp getPubDate() {
        return pubDate;
    }

    public void setPubDate(Timestamp pubDate) {
        this.pubDate = pubDate;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public String getReturnDate() {
        SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy H:m:s");
        return df.format(pubDate);
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
