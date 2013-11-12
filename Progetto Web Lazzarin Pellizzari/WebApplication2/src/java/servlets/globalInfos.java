package servlets;

import database.Mysql;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class globalInfos extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element root = doc.createElement("root");
            doc.appendChild(root);

            StringTokenizer st = new StringTokenizer(request.getRequestURI(), "/");
            String s1 = "", s2 = "", s3 = "";

            while (st.hasMoreTokens()) {
                s3 = s2;
                s2 = s1;
                s1 = st.nextToken();
            }

            System.out.println(request.getRequestURI() + " " + s3 + " " + s2 + " " + s1);

            int usefulnumber = 0;

            try {
                usefulnumber = Integer.parseInt(s1);
            } catch (NumberFormatException e) {
                s2 = "errore";
            }

            if ("group".equals(s2)) {
                Element group = doc.createElement("group");
                group.setAttribute("id", "" + usefulnumber);


                PreparedStatement stmt = Mysql.getInstance().prepareStatement("SELECT * FROM users_group INNER JOIN users ON id = id_user WHERE id_group = ? AND cancellato = 0");

                stmt.setInt(1, usefulnumber);

                ResultSet res = stmt.executeQuery();
                while (res.next()) {
                    Element user = doc.createElement("user");

                    Element username = doc.createElement("username");
                    username.appendChild(doc.createTextNode(res.getString("username")));

                    Element nascita = doc.createElement("data_nascita");
                    nascita.appendChild(doc.createTextNode(res.getDate("nascita").toString()));

                    Element residenza = doc.createElement("residenza");
                    residenza.appendChild(doc.createTextNode(res.getString("indirizzo") + ", " + res.getString("citta") + ", " + res.getString("provincia") + ", " + res.getString("stato")));

                    Element loggedIn = doc.createElement("loggedIn");
                    loggedIn.appendChild(doc.createTextNode(res.getTimestamp("last_login").after(res.getTimestamp("last_logout")) ? "true" : "false"));

                    user.appendChild(username);
                    user.appendChild(nascita);
                    user.appendChild(residenza);
                    user.appendChild(loggedIn);

                    group.appendChild(user);

                }

                root.appendChild(group);



            } else if ("user".equals(s2)) {

                PreparedStatement stmt = Mysql.getInstance().prepareStatement("SELECT DISTINCT id_user, username, indirizzo, citta, provincia, stato, nascita, last_login, last_logout, cancellato FROM users_group INNER JOIN users ON id = id_user where id_group IN (SELECT id_group FROM users_group WHERE id_user = ?) AND id_user != ?");

                stmt.setInt(1, usefulnumber);
                stmt.setInt(2, usefulnumber);


                ResultSet res = stmt.executeQuery();
                while (res.next()) {
                    Element user = doc.createElement("user");

                    Element username = doc.createElement("username");
                    username.appendChild(doc.createTextNode(res.getString("username")));

                    Element nascita = doc.createElement("data_nascita");
                    nascita.appendChild(doc.createTextNode(res.getDate("nascita").toString()));

                    Element residenza = doc.createElement("residenza");
                    residenza.appendChild(doc.createTextNode(res.getString("indirizzo") + ", " + res.getString("citta") + ", " + res.getString("provincia") + ", " + res.getString("stato")));

                    Element loggedIn = doc.createElement("loggedIn");
                    loggedIn.appendChild(doc.createTextNode(res.getTimestamp("last_login").after(res.getTimestamp("last_logout")) ? "true" : "false"));

                    user.appendChild(username);
                    user.appendChild(nascita);
                    user.appendChild(residenza);
                    user.appendChild(loggedIn);

                    root.appendChild(user);

                }

            } else {
                Element temp = doc.createElement("error");
                temp.appendChild(doc.createTextNode("Wrong or missing parameter!!!"));
                root.appendChild(temp);
            }


            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();

            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);

            trans.transform(source, result);
            String xmlString = sw.toString();

            //print xml
            System.out.println("Here's the xml:\n\n" + xmlString);
            out.println(xmlString);

        } catch (SQLException ex) {
            Logger.getLogger(globalInfos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(globalInfos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(globalInfos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
