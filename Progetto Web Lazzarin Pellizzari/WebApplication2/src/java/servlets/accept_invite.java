package servlets;

import java.sql.PreparedStatement;
import database.Mysql;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class accept_invite extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String ip = request.getServerName();
        ip += ":" + request.getServerPort();
        try {

            out.println("<html>");
            out.println("<head>");
            out.println("<title></title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<body style=\"font-size: 12px; background-color: #4169e1; color: white;\">");
            out.println("Pagina di accettazione degli inviti<br><br>");

            String invite_id = request.getParameter("invite");

            if (invite_id != null) {

                Connection conn = Mysql.getInstance();
                PreparedStatement stmt = conn.prepareStatement("UPDATE member_of SET accepted = 1, hash = \"accepted\" WHERE hash = ?");
                stmt.setString(1, invite_id);
                int updated = stmt.executeUpdate();

                if (updated != 0) {
                    out.println("Invito accettato con successo.<br><br><a style=\"color: white\" href=\"http://" + ip + "/WebApplication2/faces/forum/main.jsp\">Torna alla pagina iniziale</a>");
                } else {
                    out.println("Non è stato possibile accettare l'invito.<br>Forse hai fornito un invito non valido oppure hai già accettato questo invito.<br><br><a style=\"color: white\"href=\"http://" + ip + "/WebApplication2/faces/forum/main.jsp\">Torna alla pagina iniziale</a>");
                }

            } else {
                out.println("Impossibile verificare il codice di invito fornito!");
            }


            out.println("</body>");
            out.println("</html>");
        } catch (SQLException ex) {
            Logger.getLogger(accept_invite.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
