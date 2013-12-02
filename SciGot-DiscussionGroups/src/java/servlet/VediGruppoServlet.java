/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import db.DBManager;
import db.Group;
import db.Post;
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


public class VediGruppoServlet extends HttpServlet {

    private String userid;
    private DBManager manager;
    List<Post> posts;

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

        HttpSession session = request.getSession(false);

        try {
            posts = manager.trovaPost(request);
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

            out.println(" <link href='bootstrap/css/bootstrap.css' rel='stylesheet' type='text/css' >");
            out.println("<title>Servlet VediGruppoServlet</title>");
            out.println("</head>");

            out.println("<body>");
            out.println("<div>");
            out.println("<div class='panel panel-default' id='TitoloGruppo'>");
            out.println("Gruppo");
            out.println("</div>");
            out.println("<div class='content'>");

            out.println("<table class='table table-striped'>");

            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>");
            out.println("UTENTE");
            out.println("</th>");
            out.println("<th>");
            out.println("MESSAGGIO");
            out.println("</th>");
            out.println("<th>");
            out.println("DATA");
            out.println("</th>");
            out.println("</tr>");
            out.println("</thead");
            out.println("<tbody>");
            for (Post p : posts) {

                out.println("<tr class='active'>");
                out.println("<td>");
                out.println(p.Autore);
                out.println("</td>");
                
                out.println("<td class='contenuto'>");
                out.println(p.contenuto);
                out.println("</td>");
                
                out.println("<td>");
                out.println(p.data);
                out.println("</td>");
                out.println("</tr>");

            }
            out.println("</tbody>");
            out.println("</table>");

            out.println("<form action='InserisciPostServlet' method='POST'>");

            out.println("<div class='inserisci_commento'>");
            out.println("<input type='text' name='contenuto'   value='Scrivi il tuo commento' autocomplete='off' />");
            out.println("</div>"); 
            out.println("<div class='aggiungi_button'>");
            out.println("<input type='submit' name='AggiungiPost' value='Aggiungi'/>");
            out.println("</div>");
            out.println("<input type='hidden' name='passaID' value='" + request.getParameter("view") + "'>");
            out.println("</form>");

            session.setAttribute("idgruppo", request.getParameter("view"));

            out.println("<form enctype='multipart/form-data' method='POST' action='UploadFileServlet'>");
            out.println("<div class='upload_button'>");
            out.println("<input type='submit' value='Upload'>");
            out.println("</div>");

            out.println("<div class='sceglifile'>");
            out.println("<input class='scegli_button' type='file' name='file'>");
            out.println("</div>");

            out.println("</form>");
            out.println("</div>");

             out.println("<div class='tasti_indietro'>");
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
