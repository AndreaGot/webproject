/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import db.DBManager;
import db.Group;
import db.Invito;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
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
public class InvitiServlet extends HttpServlet {

    private String userid;
    private DBManager manager;
    List<Invito> inviti;

    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);


        try {
            inviti = manager.trovaInvito(request);
        } catch (SQLException ex) {
            Logger.getLogger(GroupServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet InvitiServlet</title>");
            out.println("</head>");
            out.println("<body>");

            if (inviti.size()<=0) {
                out.println("<h1> nessun invito! </h1>");
            } else {
                for (Invito i : inviti) {
                    out.println("Un invito da " + i.owner + " per il gruppo " + i.nomeGruppo);
                    out.println("<form action='InvitoRispostaServlet' method='POST'>");
                    out.println("<input type='hidden' name='idgruppo' value='" + i.idGruppo + "'>");
                    out.println("<input type='submit' name='risposta' value='Accetta'>");
                    out.println("<input type='submit' name='risposta' value='Rifiuta'>");
                    out.println("</form>");
                    out.println("<br>");
                    out.println("<br>");

                }
            }
            out.println(session.getAttribute("userid"));
            out.println("ciao");
            out.println("<h1>Servlet InvitiServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        userid = request.getParameter("userid");
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
