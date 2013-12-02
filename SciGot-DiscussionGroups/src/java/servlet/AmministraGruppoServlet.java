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
import javax.servlet.http.HttpSession;

/**
 *
 * @author ANDre1
 */
public class AmministraGruppoServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            HttpSession session = request.getSession(false);
            session.setAttribute("idgruppo", request.getParameter("id"));


            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println(" <link href='bootstrap/css/bootstrap.css' rel='stylesheet' type='text/css' >");
            out.println("<title>Servlet AmministraGruppoServlet</title>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<div class='panel panel-default' id='TitoloGruppo'>");
            out.println("AMMINISTRA GRUPPO");
            out.println("</div>");
            out.println("<div class='content'>");
            
            out.println("<div class='bottoni_amministra'>");
            out.println("<form action='CambiaNomeServlet' method='POST'>");
            out.println("<input type='hidden' name='id' value='" + request.getParameter("id") + "'>");
            out.println("<div class='modifica_amministra'>");
            out.println("<input type='submit' value='Modifica nome'>");
            out.println("</div>");
            out.println("</form>");

            out.println("<form action='InvitaUtenteServlet' method='POST'>");
            out.println("<input type='hidden' name='id' value='" + request.getParameter("id") + "'>");
            out.println("<div class='invita_amministra'>");
            out.println("<input type='submit' value='Invita utente'>");
            out.println("</div>");
            out.println("</form>");

            out.println("<form action='CreaReportServlet' method='POST'>");
            out.println("<input type='hidden' name='id' value='" + request.getParameter("id") + "'>");
            out.println("<div class='creareport_amministra'>");
            out.println("<input type='submit' value='Crea report'>");
            out.println("</div>");
            out.println("</form>");
            
            out.println("</div>");

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
