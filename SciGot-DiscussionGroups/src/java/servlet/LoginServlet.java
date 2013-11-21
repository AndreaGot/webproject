/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//ciao sono bomba no 2,3 3
package servlet;

import db.DBManager;
import db.Invito;
import db.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ANDre1
 */
public class LoginServlet extends HttpServlet {

    private DBManager manager;
    private List<Invito> inviti;

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

        HttpSession session = request.getSession(true);
        
        
        try {
            inviti = manager.trovaInvito(request);
        } catch (SQLException ex) {
            Logger.getLogger(GroupServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

         DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd 'alle ore' HH:mm:ss");
        Date date = new Date();
        

        Cookie cookie = new Cookie("date_cookie" + session.getAttribute("userid"), (dateFormat.format(date)));
        cookie.setMaxAge(604800);
        response.addCookie(cookie);

        Cookie[] cookies = request.getCookies();
        Boolean trovato = false;

        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            
            if (inviti.size()<=0) {
                out.println("<h1> nessun invito! </h1>");
            } else {
                for (Invito i : inviti) {
                    out.println("Un invito da " + i.owner + " per il gruppo " + i.nomeGruppo);
                    out.println("<form action='InvitoRispostaServlet' method='POST'>");
                    out.println("<input type='hidden' name='idgruppo' value='" + i.idGruppo + "'>");
                    out.println("<input type='submit' name='risposta' value='Accetta'>");
                    out.println("<input type='submit' name='risposta' value='Rifiuta'>");
                    out.println("</form>");
                    out.println("<br>");
                    out.println("<br>");

                }
            }
            
            out.println("<h1>Ciao, " + session.getAttribute("user") + "." + "</h1>");

            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (("date_cookie" + session.getAttribute("userid")).equals(cookie.getName())) {
                    out.println("Il tuo ultimo accesso risale alla data " + cookie.getValue());
                    trovato = true;
                    break;
                }
            }

            if (!trovato) {
                out.println("Questo è il tuo primo accesso!");
            }
            
          

            out.println("<form action='GroupServlet' method = 'POST'>");
            out.println("<input type='submit' value='I tuoi gruppi'>");
            out.println("</form>");
            out.println("<form action='CreaGruppoServlet' method='POST'>");
            out.println("<input type='submit' value='Crea un gruppo'>");
            out.println("</form>");
            out.println("<form action='LogoutServlet' method='POST'>");
            out.println("<input type='submit' value='Logout'>");
            out.println("</form>");
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
        HttpSession session = request.getSession(true);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // controllo nel DB se esiste un utente con lo stesso username + password
        User user;
       
        
        try {
            user = manager.authenticate(username, password);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }

        if (user == null) {
            
        request.setAttribute("message", "Credenziali errate! per favore, riprova!");
        //rimando al login

        RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
        rd.forward(request, response);
            
        } else {

            // imposto l'utente connesso come attributo di sessione
            // per adesso e' solo un oggetto String con il nome dell'utente, ma posso metterci anche un oggetto User
            // con, ad esempio, il timestamp di login
            
            
            
            
            session.setAttribute("user", user.nome_completo);
            session.setAttribute("userid", user.id);
           

           
            processRequest(request, response);

        }
    }
}
