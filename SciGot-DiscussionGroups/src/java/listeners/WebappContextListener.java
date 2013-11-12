/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import db.DBManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 *
 * @author ANDre1
 */
public class WebappContextListener implements ServletContextListener {

 @Override
 public void contextInitialized(ServletContextEvent sce) {

    try {

        DBManager manager = new DBManager();
        sce.getServletContext().setAttribute("dbmanager", manager);//pubblico l'attributo per il context

    } 
    catch (SQLException ex) {
        Logger.getLogger(getClass().getName()).severe(ex.toString());
        throw new RuntimeException(ex);

    } catch (Exception ex) {
         Logger.getLogger(WebappContextListener.class.getName()).log(Level.SEVERE, null, ex);
     }

 }

 @Override
 public void contextDestroyed(ServletContextEvent sce) {
 // Il database Derby deve essere "spento" tentando di connettersi al database con shutdown=true
    DBManager.shutdown();
 }

}