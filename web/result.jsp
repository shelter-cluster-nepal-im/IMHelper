<%-- 
    Document   : result
    Created on : 24-Nov-2015, 11:55:18
    Author     : Gaurab Pradhan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (null == session.getAttribute("userName")) {
        response.sendRedirect("index.jsp");
    } 
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="shortcut icon" href="images/favicon.ico" />

        <title>IM Helper - Upload Success</title>
    </head>
    <body>
        <h3>${requestScope["message"]}</h3>

    </body>
</html>
