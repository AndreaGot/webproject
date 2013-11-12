<%-- 
    Document   : index
    Created on : 11-nov-2013, 13.43.43
    Author     : ANDre1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Esegui il login</h1>

        <form name="form1" action="LoginServlet" method="POST">

            <input type="text" name="username" value="user" />
            <input type="text" name="password" value="pass" />
            <input type="submit" value="ok" name="ok" />

        </form>
    </body>
</html>
