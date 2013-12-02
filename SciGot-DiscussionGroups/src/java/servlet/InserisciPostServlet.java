/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import db.DBManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 *
 * @author ascia_000
 */
public class InserisciPostServlet extends HttpServlet {

    private DBManager manager;
    private boolean inserito = false;
    private int string;

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

        // string to split
        String str = request.getParameter("contenuto");
        String[] array = new String[500];

        Integer i;
        i = 0;

        for (i = 0; i < 500; i++) {
            array[i] = "";
        }


        Scanner s = new Scanner(str);
        String post = "";
        String link = null;
        i = 0;
        while (s.hasNext()) {
            array[i] = s.next();
            System.out.println(array[i]);
            i++;
        }
        i = 0;
        for (i = 0; i < array.length; i++) {
            if (array[i].equals("")) {
                break;
            }
            if (array[i].startsWith("$$")) {
                try {
                    array[i] = array[i].replace("$$", "");
                    link = manager.trovaFileLink(request, array[i]);
                } catch (SQLException ex) {
                    Logger.getLogger(InserisciPostServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (link == null) {
                    post = post + " <a href='http://" + array[i] + "'>" + array[i] + "</a>";
                } else {
                    post = post + " <form action='DownloadFileServlet' method='POST'> <input type='hidden' name='link' value='" + link + "'> <input type='submit' value='" + array[i] + "'> </form>";

                }

            } else if (array[i].startsWith("http://")) {
                post = post + " <a href='" + array[i] + "'>" + array[i] + "</a>";
            } else if (array[i].startsWith("www.")) {
                post = post + " <a href='http://" + array[i] + "'>" + array[i] + "</a>";
            } else {
                post = post + " " + array[i];
            }

        }
        System.out.println(post);

        try {
            inserito = manager.inserisciPost(request, post);


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
            out.println("<title>Servlet InserisciPostServlet</title>");
            out.println("</head>");
            out.println("<body>");


            out.println("<div class='alert alert-info'>");
            out.println("<strong>");
            out.println(" Commento inserito con successo!");
            out.println("</strong>");
            out.println("</div>");


            out.println("<div class='tasti_indietro'>");
            out.println("<div class='torna_gruppo'>");
            out.println("<form action='VediGruppoServlet' method='POST' >");
            out.println("<input type='hidden' name='view' value='" + request.getParameter("passaID") + "'>"
                    + "<input type='submit' value='Torna al gruppo'>"
                    + "</form>");
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
