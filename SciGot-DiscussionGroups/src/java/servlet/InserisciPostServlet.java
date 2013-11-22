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

        // string to split
        String str = request.getParameter("contenuto");

        //convert the String into a Array
        char[] temp = str.toCharArray();
        char[] notlink;
        char[] link;
        char[]appoggio;
        String notlink2;
        notlink = new char[2000];
        link = new char[2000];
        appoggio = new char[200];
        int i,r;
        int z = 0;

        int countdollar = 0;

        for (i = 0; i < temp.length - 1; i++) {

            if (temp[i] == '$') {
                if (temp[i + 1] == '$') {
                    countdollar = countdollar + 1;
                    while (countdollar % 2 == 1) {

                        if (temp[z] == '$') {
                            if (temp[z + 1] == '$') {
                                countdollar = countdollar + 1;

                            }

                        }
                        if (temp[z] != '$') {
                            notlink[z] = temp[z];
                            z++;
                        }
                    }

                    while (countdollar % 2 == 0) {

                        if (temp[z] == '$') {
                            if (temp[z + 1] == '$') {
                                countdollar = countdollar + 1;

                            }

                        }
                        if (temp[z] != '$') {  
                            link[z] = temp[z];
                            z++;
                        }
                    }
                i = i + 1;
                z = z + 2;
                }
                
            }
        }
        
        int lunga=temp.length;
        System.out.println(lunga);
       int y;
       int g=0; 
        for(y=lunga-1;temp[y]!='$';y--){
            appoggio[g]=temp[y];
           g++;
       }
        
        notlink2= Arrays.toString(appoggio);
        
        String reverse = new StringBuffer(notlink2).reverse().toString();
        System.out.println(reverse);
        
        
        /*  try {
         inserito = manager.inserisciPost(request);
            
            
         } catch (SQLException ex) {
         Logger.getLogger(RisultatoCreaGruppo.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet InserisciPostServlet</title>");
            out.println("</head>");
            out.println("<body>");
for (r=0;r<temp.length;r++){
out.println(link[r]);
}

for (r=0;r<temp.length;r++){
out.println(notlink[r]);
}
            out.println("<h1>Servlet InserisciPostServlet at " + request.getContextPath() + "</h1>");
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
