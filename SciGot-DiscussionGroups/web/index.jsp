<%-- 
    Document   : index
    Created on : 11-nov-2013, 13.43.43
    Author     : ANDre1
--%>

<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link href="bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css">
              <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>

        <div class="panel panel-danger">
            <div class="panel-heading">
                <h3 class="panel-title">Esegui il login</h3>
            </div>
            <div class="panel-body">
                <form name="form1" action="LoginServlet" method="POST">
                    <div class="username">
                        <div id="username_icon">
                            <span class="glyphicon glyphicon-user"></span>
                        </div>
                        <div id="username_textbox">
                            <input type="text" name="username"   value="user" />
                        </div>
                    </div>
                    <div class="password">
                        <div id="password_icon">
                            <span class="glyphicon glyphicon-lock"></span>
                        </div>
                        <div id="password_textbox">
                            <input type="password" name="password" autocomplete="off" value="pass" />
                        </div>
                    </div>

                    <%
                        if (request.getAttribute("message") == null) {
                            out.println(" ");
                        } else {
                            out.println(request.getAttribute("message"));

                        }

                    %> 


                    <div class="submit_button">
                        <input type="submit" value="Accedi" name="ok" />
                    </div>
                </form>
            </div>
        </div>


    </div>


















</body>


</html>
