<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
    if (null == session.getAttribute("userName")) {
        response.sendRedirect("index.jsp");
    } 
%>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link rel="shortcut icon" href="images/favicon.ico" />

            <title>IM Helper</title>
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
                                <ul id="horizontal-list">
                                    <li><a href="clean.jsp" class="clean" title="Clean & Consolidation" /></a></li>
                                    <li><a href="split.jsp" class="split" title="Split" /></a></li>
                                    <li><a href="hdx.jsp" class="hdx" title="HDX" /></a></li>
                                    <li><a href="setting.jsp" class="settings" title="Settings" /></a></li>
                                </ul>
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
