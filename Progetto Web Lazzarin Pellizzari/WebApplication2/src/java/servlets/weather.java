package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class weather extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            String localita = request.getParameter("localita");
            System.out.println(java.net.URLEncoder.encode(localita, "UTF-8"));


            URL url = new URL("http://www.google.com/ig/api?weather=" + java.net.URLEncoder.encode(localita, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() != 200) {
                throw new IOException(conn.getResponseMessage());
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            rd.close();
            conn.disconnect();

            StringReader reader = new StringReader(sb.toString());
            InputSource is = new InputSource(reader);
            Document doc = db.parse(is);

            NodeList nodeList = doc.getElementsByTagName("current_conditions");
            Node firstNode = nodeList.item(0);

            String temperatura = "Not available";
            String umidita = "Not available";
            String pathImg = "";

            if (firstNode != null) {
                System.out.println("nodo NOT null");
                NodeList lista = firstNode.getChildNodes();
                for (int i = 0; i < lista.getLength(); i++) {
                    if ("temp_c".equals(lista.item(i).getNodeName())) {
                        temperatura = lista.item(i).getAttributes().getNamedItem("data").getNodeValue() + "° C";
                    } else if ("humidity".equals(lista.item(i).getNodeName())) {
                        umidita = lista.item(i).getAttributes().getNamedItem("data").getNodeValue();
                    } else if ("icon".equals(lista.item(i).getNodeName())) {
                        pathImg = "http://www.google.it/" + lista.item(i).getAttributes().getNamedItem("data").getNodeValue();
                    }

                }

                out.println("<table border=1>"
                        + "<tr>"
                        + "<td colspan=\"2\">"
                        + "Meteo utente"
                        + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td rowspan=\"3\">"
                        + "<img src=\"" + pathImg + "\">"
                        + "</td>"
                        + "<td>"
                        + localita
                        + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td>"
                        + "Temperatura: " + temperatura
                        + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td>"
                        + "Umidita': " + umidita.substring(umidita.indexOf(":") + 1).trim()
                        + "</td>"
                        + "</tr>"
                        + "</table>");
            } else {
                out.println("<table border=1>"
                        + "<tr>"
                        + "<td colspan=\"2\">"
                        + "Meteo utente"
                        + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td rowspan=\"2\">"
                        + "n/a"
                        + "</td>"
                        + "<td>"
                        + "Temperatura: n/a"
                        + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td>"
                        + "Umidita': n/a"
                        + "</td>"
                        + "</tr>"
                        + "</table>");
            }




        } catch (Exception ex) {
            Logger.getLogger(weather.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
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
