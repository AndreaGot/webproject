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
import javax.servlet.http.HttpSession;

/**
 *
 * @author ANDre1
 */
public class InvitoRispostaServlet extends HttpServlet {

    private DBManager manager;

    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
    }

    // Effettuare la chiamata a due query:
    // Una si occupa del flag dell'invito come 1 (accettato)
    // Una si occupa dell'inserimento dell'utente nella tabella gruppo_utente
    // In seguito la richiesta non comparirà più perchè è stata presa una decisione, mentre apparirà il gruppo
    // Effettuare la chiamata a una query che setta semplicemente il flag della richiesta come 0, rifiutando la proposta.
    // In seguito la richiesta non comparirà più perchè è stata presa una decisione
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

        Boolean accettato = false;
        String risposta = request.getParameter("risposta");
        String idgruppo = request.getParameter("idgruppo");

        if ("Accetta".equals(risposta)) {
            try {
                // L'invito è stato accettato
                manager.settaInvito(request, idgruppo, "1");
                manager.inserisciUtente(request, idgruppo);

            } catch (SQLException ex) {
                Logger.getLogger(InvitoRispostaServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            accettato = true;
        } else if ("Rifiuta".equals(risposta)) {
            // Settare invito come rifiutato
            try {
                // L'invito è stato accettato

                manager.settaInvito(request, idgruppo, "2");

            } catch (SQLException ex) {
                Logger.getLogger(InvitoRispostaServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println(" <link href='bootstrap/css/bootstrap.css' rel='stylesheet' type='text/css' >");
            out.println("<title>Servlet InvitoRispostaServlet</title>");
            out.println("</head>");
            out.println("<body>");
            if (accettato) {
                out.println("<div class='alert alert-info'>");
                out.println("<strong>");
                out.println(" Invito accettato!");
                out.println("</strong>");

                out.println("Benvenuto nel gruppo!");
                out.println("</div>");

            } else {

                out.println("<div class='alert alert-danger'>");
                out.println("<strong>");
                out.println("Invito rifiutato");
                out.println("</strong>");

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
