/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import db.DBManager;
import db.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class CreaReportServlet extends HttpServlet {

    private DBManager manager;
    List<User> user = new ArrayList<User>();

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
        
        String posts = "";
        String data = "";
        
        try {
            user = manager.trovaUtente(request);
            // step 1: creation of a document-object
        } catch (SQLException ex) {
            Logger.getLogger(CreaReportServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        Document document = new Document();

        response.setContentType("application/pdf"); // Code 1

        try {
            PdfWriter.getInstance(document,
                    response.getOutputStream()); // Code 2
            document.open();

            // Code 3
            PdfPTable table = new PdfPTable(3);
            table.addCell("Nome utente");
            table.addCell("Post");
            table.addCell("Ultimo post");
            table.addCell(" ");
            table.addCell(" ");
            table.addCell(" ");
            for (User u : user) {
                table.addCell(u.getUserName());
                try {
                    posts = manager.numeroPost(request, u.getId());
                    data = manager.dataPost(request, u.getId());
                    
                } catch (SQLException ex) {
                    Logger.getLogger(CreaReportServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                table.addCell(posts);
                table.addCell(data);
            }

            // Code 4
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
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
