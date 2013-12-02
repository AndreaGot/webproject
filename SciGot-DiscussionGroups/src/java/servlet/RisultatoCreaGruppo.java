/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import db.DBManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ANDre1
 */
public class RisultatoCreaGruppo extends HttpServlet {

    private DBManager manager;
    private Boolean fatto = false;
    private Boolean fatto2 = false;

    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String IDGruppo;

        try {
            fatto = manager.inserisciGruppo(request);
            IDGruppo = manager.trovaIdDaGruppo(request);
            fatto2 = manager.inserisciUtente(request, IDGruppo);

        } catch (SQLException ex) {
            Logger.getLogger(RisultatoCreaGruppo.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println(" <link href='bootstrap/css/bootstrap.css' rel='stylesheet' type='text/css' >");
            out.println("<title>Servlet RisultatoCreaGruppo</title>");
            out.println("</head>");
            out.println("<body>");

            if (fatto && fatto2) {
                out.println("<div class='alert alert-info'>");
                out.println("<strong>");
                out.println("Ben fatto!");
                out.println("</strong>");

                out.println("Gruppo " + request.getParameter("creaGruppoTextbox") + " creato");
                out.println("</div>");
            } else {
                
                out.println("<div class='alert alert-danger'>");
                out.println("<strong>");
                out.println(" Gruppo non creato");
                out.println("</strong>");
                out.println("Controlla che non sia già esistente :( ");
                out.println("</div>");
            }

            out.println("<a href='LoginServlet'> Torna alla Home </a>");

            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
