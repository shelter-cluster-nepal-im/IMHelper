<%-- 
    Document   : setting
    Created on : 25-Nov-2015, 21:35:58
    Author     : Gaurab Pradhan
--%>

<%@page import="java.io.*"%>
<%@page import="Util.Paths"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            <title>IM Helper - Setting</title>
            <link rel="stylesheet" type="text/css" href="css/style.css" />
<!--             <script type="text/javascript">
            var observe;
            if (window.attachEvent) {
                observe = function (element, event, handler) {
                    element.attachEvent('on' + event, handler);
                };
            } else {
                observe = function (element, event, handler) {
                    element.addEventListener(event, handler, false);
                };
            }
            function init() {
                var text = document.getElementById('text');
                function resize() {
                    text.style.height = 'auto';
                    text.style.height = text.scrollHeight + 'px';
                }
                /* 0-timeout to get the already changed text */
                function delayedResize() {
                    window.setTimeout(resize, 0);
                }
                observe(text, 'change', resize);
                observe(text, 'cut', delayedResize);
                observe(text, 'paste', delayedResize);
                observe(text, 'drop', delayedResize);
                observe(text, 'keydown', delayedResize);

                text.focus();
                text.select();
                resize();
            }
        </script>-->
            
    </head>
    <!--<body onload="init();">-->
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

        <form name="settingform" method="post" id="commentForm1">
           
             <fieldset>
                                            <legend><h3>Settings</h3></legend> </br>
                                            
       
 <p align="right"><input type="button" name="buttonEdit" value="Edit" onclick="document.settingform.settingFile.disabled = false"/> &nbsp;                                                               
     <input type="submit" src="images/save.png" value="Save" name="buttonSave"/></p>
             <p>         <textarea name="settingFile" rows="10" cols="100" disabled>
            <%
                    BufferedReader br = null;
                    try {
                        String sCurrentLine;
                        String path = Paths.getPropertiesFile();
                        File file = new File(path);
                        br = new BufferedReader(new FileReader(file));
                        while ((sCurrentLine = br.readLine()) != null) {
                            if(!sCurrentLine.startsWith("            ")){
            %>
<%=sCurrentLine%>
                <%  }}
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (br != null) {
                                br.close();
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                %>
            </textarea>
             </p>
            <%
                String button = request.getParameter("buttonSave");
                if (button != null) {
                    try {
                        String content = request.getParameter("settingFile").trim();
                        if(content != null){
                        content = content.replaceAll("(\\r|\\n|\\r\\n)+", "\n");
                        String path = Paths.getPropertiesFile();
                        File file = new File(path);
                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);
//            txtProp.setText(null);
                        bw.write(content);
                        bw.close();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    response.sendRedirect("setting.jsp");
                }
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
