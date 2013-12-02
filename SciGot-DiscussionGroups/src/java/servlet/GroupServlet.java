/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import db.Group;
import db.DBManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;


/**
 *
 * @author ANDre1
 */
public class GroupServlet extends HttpServlet {
    private String userid;
    private DBManager manager;
    List<Group> groups;

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
        System.out.println(request.getParameter("userid"));
        
        
        try {
            groups = manager.trovaGruppo(request);
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
            out.println("<title>Servlet GroupServlet</title>");
            out.println("</head>");
            out.println("<body>");
            
            
            out.println("<div class='panel panel-default' id='TitoloGruppo'>");
            out.println("I MIEI GRUPPI");
            out.println("</div>");
            
            out.println("<div class='content'>");
            for (Group g : groups) {
                out.println("<div class='gruppo_blocco'>");
                out.println("<div class='nome_gruppo'>");
                out.println("<h3>" + g.nome + "</h3>");
                out.println("</div>");
                out.println("<div class='gestisci_gruppo'>");
                out.println("<div class='vedi_gruppo'>");
                out.println("<form action='VediGruppoServlet' method='POST' >"
                        + "<input type='hidden' name='view' value='" + g.id + "'>"
                        + "<input type='submit' value='Vedi Gruppo'>"
                        + "</form>");
                out.println("</div>");
                if ((session.getAttribute("userid").toString()).equals(g.proprietario.toString())==true) {
                    out.println("<div class='amministra_gruppo'>");
                    out.println("<form action='AmministraGruppoServlet' method='POST' >"
                            + "<input type='hidden' name='id' value='" + g.id + "'>"
                            + "<input type='submit' value='Amministra'>"
                            + "</form>");
                    out.println("</div>");
                }
                out.println("</div>");
                out.println("</div>");
                
            }
            
            out.println("</div>");
            out.println("</div>");
            
            

            out.println("<a href='LoginServlet'> Torna alla Home </a>");
            
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
