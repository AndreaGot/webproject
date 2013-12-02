/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import db.DBManager;
import java.io.*;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ANDre1
 */
public class UploadFileServlet extends HttpServlet {

    private String dirBase;
    private String folder;
    private String dirPath;
    private DBManager manager;

    @Override
    public void init() throws ServletException {
        // inizializza il DBManager dagli attributi di Application
        this.manager = (DBManager) super.getServletContext().getAttribute("dbmanager");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);


        folder = session.getAttribute("idgruppo").toString();
        //dirBase = "/Users/ANDre1/Apache_Tomcat/files/";
        dirPath = getServletContext().getRealPath("/") + folder;
        //dirPath = dirBase + folder;

        File theDir = new File(dirPath);

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + folder);
            boolean result = theDir.mkdir();

            if (result) {
                System.out.println("DIR created");
            }
        }
//

        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println(" <link href='bootstrap/css/bootstrap.css' rel='stylesheet' type='text/css' >");
        out.println("<title>Servlet Upload</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("Demo Upload Servlet using MultipartRequest");
        out.println("<br>");
        try {
            // Use an advanced form of the constructor that specifies a character
            // encoding of the request (not of the file contents) and a file
            // rename policy.
            MultipartRequest multi = new MultipartRequest(request, dirPath, 10 * 1024 * 1024, "ISO-8859-1", new DefaultFileRenamePolicy());
            out.println("PARAMS:");
            out.println("<br>");
            Enumeration params = multi.getParameterNames();
            while (params.hasMoreElements()) {
                String name = (String) params.nextElement();
                String value = multi.getParameter(name);
                out.println(name + "=" + value);
                out.println("<br>");
            }
            out.println();
            out.println("<br>");
            out.println("FILES:");
            out.println("<br>");
            Enumeration files = multi.getFileNames();

            while (files.hasMoreElements()) {
                String name = (String) files.nextElement();
                String filename = multi.getFilesystemName(name);
                String originalFilename = multi.getOriginalFileName(name);
                String type = multi.getContentType(name);
                File f = multi.getFile(name);
                out.println("name: " + name);
                out.println("<br>");
                out.println("filename: " + filename);
                out.println("<br>");
                out.println("originalFilename: " + originalFilename);
                out.println("<br>");
                out.println("type: " + type);
                out.println("<br>");
                if (f != null) {
                    out.println("f.toString(): " + f.toString());
                    out.println("<br>");
                    out.println("f.getName(): " + f.getName());
                    out.println("<br>");
                    out.println("f.exists(): " + f.exists());
                    out.println("<br>");
                    out.println("f.length(): " + f.length());
                    out.println("<br>");
                    out.println("<br>");
                    out.println("<br>");
                }


                String indirizzo;
                String primaparte;
                String fileintero;

                primaparte = getServletContext().getRealPath("/");
                fileintero = f.toString();
                indirizzo = fileintero.substring(primaparte.length() - 1);

                try {
                    manager.inserisciFile(request, indirizzo, f.getName());
                } catch (SQLException ex) {
                    Logger.getLogger(UploadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
                }





            }
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

        } catch (Exception lEx) {

            this.getServletContext().log(lEx, "error reading or saving file");

        } finally {
            out.close();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
