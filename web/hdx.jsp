<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="Servlet.SplitServlet"%>
<%@page import="Util.*"%>
<%@page import="java.io.*"%>
<%@page import="java.sql.*"%>
<%
    if (null == session.getAttribute("userName")) {
        response.sendRedirect("index.jsp");
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link rel="shortcut icon" href="images/favicon.ico" />
            <title>IM Helper - HDX DB</title>
            <link rel="stylesheet" type="text/css" href="css/style.css" />
    </head>
    <body>
        <div id="wrapper">
            <div id="inner-page">

                <div id="header">
                    <div id="header-image"><img src="images/logo.png" /></div>
                </div>

                <div id="menu">
                    <ul>
                        <li><a href="HomePage.jsp" title="Home"><img src="images/home.png" /></a></li>
                        <li><a href="logout.jsp" title="Logout"><img src="images/logout.png" /></a></li>
                    </ul>
                </div>

                <div id="content">
                    <div class="box">
                        <h2>SC IM Data Processing</h2>

                        <div id="menu-outer">
                            <div class="table">


                                <!-- Form starts -->

                                <div class="field"> 
                                    <form name="hdxform" action="HDXServlet" method="post" id="commentForm1">

                                        <fieldset>
                                            <legend><h3>HDX</h3></legend> </br>
                                            <!--                 <div id='file_browse_wrapper'>
                                            
                                            <input type='file' id='file_browse'>
                                            
                                                                                                        </div>-->


                                        <input type="submit" name="submit" value="HDX Generator" name="hdxBtn" id="hdx"/> </br>
                                                                                    
                                            <%    ArrayList<String> outputLog = (ArrayList<String>) session.getAttribute("log");
                                                if (outputLog != null) { %>
<textarea name="outputLog" rows="10" cols="100"><%  for (int i = 0; i < outputLog.size(); i++) {%>
<%=outputLog.get(i)%>
                                                <%    }
                                                    outputLog.clear();
                                                %>
                                            </textarea>
                                            <% } else {
                                            %>
                                            <textarea name="outputLog" rows="10" cols="100">
                                            </textarea>
                                            <%   }
                                            %>
                                        </fieldset>

                                    </form>
                                </div>

                                <div id="field" class="clearfix">

                                    <!-- Form ends --> 

                                </div>
                            </div>

                        </div>
                    </div>


                    <div id="footer">© 2015 </br>
                        Developed by: Shelter Cluster Nepal - IM Team
                    </div>

                </div>
            </div>
    </body>
</html>
