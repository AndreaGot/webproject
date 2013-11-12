package beans.validator;

import database.DBUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterValidation {

    private String username = "";
    private String email = "";
    private String passw1 = "";
    private String passw2 = "";
    private Date nascita = new Date();
    private String indirizzo = "";
    private String citta;
    private String provincia;
    private String stato;
    private List<String> errorMessage;
    private String errorMessages = "";
    private String userNotUnique;
    private String userEmpty;
    private String emailInvalid;
    private String passNotEqual;
    private String nascitaError;
    private String indirizzoError;
    private String cittaError;
    private String provinciaError;
    private String statoError;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIndirizzoError() {
        return indirizzoError;
    }

    public void setIndirizzoError(String indirizzoError) {
        this.indirizzoError = indirizzoError;
    }

    public String getNascitaError() {
        return nascitaError;
    }

    public void setNascitaError(String nascitaError) {
        this.nascitaError = nascitaError;
    }

    public String getCittaError() {
        return cittaError;
    }

    public void setCittaError(String cittaError) {
        this.cittaError = cittaError;
    }

    public String getProvinciaError() {
        return provinciaError;
    }

    public void setProvinciaError(String provinciaError) {
        this.provinciaError = provinciaError;
    }

    public String getStatoError() {
        return statoError;
    }

    public void setStatoError(String statoError) {
        this.statoError = statoError;
    }

    public String getPassNotEqual() {
        return passNotEqual;
    }

    public void setPassNotEqual(String passNotEqual) {
        this.passNotEqual = passNotEqual;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public String getEmailInvalid() {
        return emailInvalid;
    }

    public void setEmailInvalid(String emailInvalid) {
        this.emailInvalid = emailInvalid;
    }

    public String getUserNotUnique() {
        return userNotUnique;
    }

    public void setUserNotUnique(String userNotUnique) {
        this.userNotUnique = userNotUnique;
    }

    public String getUserEmpty() {
        return userEmpty;
    }

    public void setUserEmpty(String userEmpty) {
        this.userEmpty = userEmpty;
    }

    public String getErrorMessages() {
        StringBuilder out = new StringBuilder();
        out.append("<div style=\"color: red; font-weight: bold;\">");
        if (errorMessage != null) {
            for (String o : errorMessage) {
                out.append(o);
                out.append("<br>");
            }
        }
        out.append("</div>");
        return out.toString();
    }

    public void setErrorMessages(String errorMessages) {
        this.errorMessages = errorMessages;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo.trim();
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

    public String getPassw1() {
        return passw1;
    }

    public void setPassw1(String passw1) {
        this.passw1 = passw1.trim();
    }

    public String getPassw2() {
        return passw2;
    }

    public void setPassw2(String passw2) {
        this.passw2 = passw2.trim();
    }

    public String validate() {
        errorMessage = new ArrayList<String>();
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(email);
        DBUser user = new DBUser();
        if ("".equals(username)) {
            errorMessage.add(userEmpty);
        } else if (!user.uniqueUsername(username)) {
            errorMessage.add(userNotUnique);
        } else if (!m.matches()) {
            errorMessage.add(emailInvalid);
        } else if ("".equals(passw1)) {
            errorMessage.add(passNotEqual);
        } else if (!passw1.equals(passw2)) {
            errorMessage.add(passNotEqual);
        } else if ("".equals(indirizzo)) {
            errorMessage.add(indirizzoError);
        } else if ((nascita.compareTo(new Date(System.currentTimeMillis()))) >= 0) {
            errorMessage.add(nascitaError);
        } else if ("".equals(citta)) {
            errorMessage.add(cittaError);
        } else if ("".equals(provincia)) {
            errorMessage.add(provinciaError);
        } else if ("".equals(stato)) {
            errorMessage.add(statoError);
        }

        if (errorMessage.isEmpty()) {
            //aggiungo l'utente
            user.saveUser(username, passw1, email, indirizzo, citta, provincia, stato, nascita);
            return "success";
        } else {
            //non posso aggiungere l'utente
            return "failure";
        }
    }
}