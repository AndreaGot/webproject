/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

/**
 *
 * @author ANDre1
 */
public class Invito {

    public String nomeGruppo;
    public String accettato;
    public String owner;

    public String getNomeGruppo() {
        return nomeGruppo;
    }

    public void setNomeGruppo(String id) {
        this.nomeGruppo = id;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    
        public String getAccettato() {
        return accettato;
    }

    public void setAccettato(String accettato) {
        this.accettato = accettato;
    }
}
