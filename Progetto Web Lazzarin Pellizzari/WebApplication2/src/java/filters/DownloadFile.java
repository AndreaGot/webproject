package filters;

import beans.Posts;
import beans.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DownloadFile implements Filter {

    private FilterConfig filterConfig = null;

    public DownloadFile() {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        //DEBUG:System.out.println("Sono dentro al filtro....");
        try {
            HttpSession session = httpRequest.getSession(false);

            String url = httpRequest.getRequestURL().toString();

            int lastSlash = url.lastIndexOf("/");

            if (lastSlash == -1) {
            } else {

                //String fileRequested = url.substring(lastSlash + 1);
                int prevLastSlash = url.substring(0, lastSlash).lastIndexOf("/");
                String groupId = url.substring(prevLastSlash + 1, lastSlash);

                //DEBUG:System.out.println(fileRequested);
                //DEBUG:System.out.println(groupId);

                if (session != null) {
                    //DEBUG:System.out.println("Session is NOT null");
                    User utente = (User) session.getAttribute("user");
                    Posts posts = (Posts) session.getAttribute("posts");
                    if ((utente != null) && (posts != null)) {
                        if (posts.getCurrentIdGroup() == 0) {
                            httpResponse.sendRedirect(httpRequest.getContextPath() + "/faces/forum/main.jsp");
                        } else {
                            //DEBUG:System.out.println(" >>> " + posts.getCurrentIdGroup());
                            boolean c = utente.isInGroup(Integer.parseInt(groupId));
                            //DEBUG:System.out.println(" >>> " + c);
                            //DEBUG:System.out.flush();
                            if (c) {
                                chain.doFilter(request, response);
                                //DEBUG:System.out.println("Utente nel gruppo");
                            } else {
                                //DEBUG:System.out.println("Utente NON nel gruppo");
                                httpResponse.sendRedirect(httpRequest.getContextPath() + "/faces/forum/main.jsp");
                            }
                        }
                    } else {
                        httpResponse.sendRedirect(httpRequest.getContextPath() + "/faces/forum/main.jsp");
                    }
                } else {
                    //DEBUG:System.out.println("Session is null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    }

    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("DownloadFile()");
        }
        StringBuilder sb = new StringBuilder("DownloadFile(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }
}
