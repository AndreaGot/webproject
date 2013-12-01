/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ANDre1
 */
public class InvitaUtenteServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println(" <link href='bootstrap/css/bootstrap.css' rel='stylesheet' type='text/css' >");
            out.println("<title>Servlet InvitaUtenteServlet</title>");
            out.println("</head>");
            out.println("<body>");

            out.println("<div class='panel panel-default' id='TitoloGruppo'>");
            out.println("INVITA UTENTE");
            out.println("</div>");

            out.println("<div class='content'>");

            out.println("<form action='RisultatoInvitoServlet' method='POST'>");
            out.println("<input type='hidden' name='idgruppo' value='" + request.getParameter("id") + "'>");
            out.println("<div class='modifica_textbox'>");
            out.println("<input type='text' name='nome' value='Username utente'>");
            out.println("</div>");
            out.println("<div class='modifica_bottone'>");
            out.println("<input type='submit' value='Invita utente'>");
            out.println("</div>");
            out.println("</form>");

            out.println("<div class='bottoni_indietro'>");
            out.println("<div class='torna_gruppo'>");
            out.println("<form action='GroupServlet' method = 'POST'>");
            out.println("<input type='submit' value='Torna ai tuoi gruppi'>");
            out.println("</form>");
            out.println("</div>");
            out.println("<div class='torna_home'>");
            out.println("<a href='LoginServlet'> Torna alla Home </a>");
            out.println("</div>");
            out.println("</div>");
            out.println("</div>");
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
