/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;


import java.io.Serializable;


/**
 *
 * @author ANDre1
 */
public class User implements Serializable {

    private String username;
    private String password;
    public String nome_completo;
    public String id;

    public String getUserName() {
        return username;
    }

    public void setUserName(String name) {
        this.username = name;
    }

    public String getName() {
        return nome_completo;
    }

    public void setName(String name) {
        this.nome_completo = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
        public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}