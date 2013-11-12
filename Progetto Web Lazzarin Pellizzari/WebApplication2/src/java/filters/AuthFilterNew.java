package filters;

import beans.User;
import java.io.IOException;
import javax.faces.application.ViewExpiredException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthFilterNew implements Filter {

    private FilterConfig filterConfig = null;

    public AuthFilterNew() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            HttpSession session = httpRequest.getSession(false);
            String requestPath = httpRequest.getPathInfo();
            //prelevo il bean User, per verificare se l'utente ha già effettuato login
            User utente = (User) session.getAttribute("user");
            //se l'utente che ho prelevato è NULL allora nessuno ha ancora effettuato login, quindi redirigo alla pagina principale
            if ((utente == null) || (utente.getUsername() == null)) {

                httpResponse.sendRedirect(httpRequest.getContextPath() + "/faces/welcomeJSF.jsp");
            } else {
                chain.doFilter(request, response);
            }

        } catch (ViewExpiredException e) {
            System.out.println("ViewExpiredException...");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/faces/welcomeJSF.jsp");
            /*RequestDispatcher rd = request.getRequestDispatcher("/faces/welcomeJSF.jsp");
            rd.forward(request, response);*/
        } catch (Exception e) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/faces/welcomeJSF.jsp");
            //RequestDispatcher rd = request.getRequestDispatcher("/faces/welcomeJSF.jsp");
            //rd.forward(request, response);
        }
    }

    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
        }
    }

    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("AuthFilterNew()");
        }
        StringBuffer sb = new StringBuffer("AuthFilterNew(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
}