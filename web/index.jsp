<%-- 
    Document   : index
    Created on : 21-Nov-2015, 16:51:20
    Author     : Gaurab Pradhan
--%>
<%@page import="java.io.File"%>
<%@page import="Util.Paths"%>
<%@page import="Util.PropertiesUtil"%>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%PropertiesUtil.loadPropertiesFile();%>
<%PropertiesUtil.loadLog4j();%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <link rel="shortcut icon" href="images/favicon.ico" />
        <title>IM Helper - Login Page</title>
        <link rel="stylesheet" type="text/css" href="css/login.css"/>
        <script src="js/jquery.js" type="text/javascript"></script>
        <script src="js/jquery.validate.js" type="text/javascript"></script>
        <script src="js/jquery.metadata.js" type="text/javascript"></script>

        <script type="text/javascript">
            $(document).ready(function () {
                $("#commentForm1").validate({meta: "validate"});
                required: 'Enter this!'
            });
        </script>
        <style type="text/css">
            form { width: 500px; }
            form label { width: 250px; }
            form label.error, 
            form input.submit { color: red; font-size: 20px; }
        </style>
    </head>
    <body>
        <div class="wrapper">
            <!--maindiv wrapper starts-->
            <div id="border-top" class="h_green">
                <!--header starts-->
                <div id="header-top">

                    <div id="logo">

                    </div>
                </div>    
            </div>
            <div id="contentbox">
                <div style="background:none;border:none;" id="menubox">
                    <div id="menuheader">
                        <div id="message">
                            <p style="text-align:center; padding-top:10px;" class="message_text"> </p>

                        </div>
                        <div id="user_name">
                        </div>
                    </div>
                </div>
                <div class="loginbox">
                    <div style="width:100%!important;" class="rightsection">


                        <form name="loginform" action="LoginServlet" method="post" id="commentForm1">

                            <div class="loginwrapper">

                                <div class="rightboxwrapper">

                                    <table cellspacing="0" cellpadding="0" border="0" align="center" class="loginform">
                                        <tbody><tr class="loginheader">
                                                <th style="color:#0588ab; font-size:12px; font-family:Helvetica; text-transform:uppercase; height:35px;"> <h2>Login</h2></th>
                                            </tr>
                                            <tr>
                                                <td style="color:#0588ab; font-size:12px; font-family:Helvetica; text-transform:uppercase; height:25px;">User ID </td></tr>
                                            <tr>

                                                <td style="color:#0588ab; font-size:12px; font-family:Helvetica; text-transform:uppercase; height:25px;"><input type="text" id="username" name="username" class="{validate:{required:true, messages:{required:'*'}}}"></td>
                                            </tr>
                                            <tr>
                                                <td style="color:#0588ab; font-size:12px; font-family:Helvetica; text-transform:uppercase; height:25px;">Password</td></tr>
                                            <tr>
                                                <td style="color:#0588ab; font-size:12px; font-family:Helvetica; text-transform:uppercase; height:25px;"><input type="password" id="password" name="password" class="{validate:{required:true, messages:{required:'*'}}}"></td>
                                            </tr>

                                            <tr>
                                                <td colspan="2">
                                                    <input value="Login" class="btn"  style="width:60px;" type="submit"/>
                                                    <input value="Reset" class="btn" style="width:60px; margin-left:10px;" type="reset"/>
                                                </td>


                                            </tr>
                                            <tr><td><p style="color: red">${message}</p><%session.removeAttribute("message");%></td></tr>
                                        </tbody></table>
                                </div>
                                <div class="imagewrapper">
                                    <!--<img width="323px" height="262px" src="images/im.jpg">-->     

                                </div>
                            </div> 
                            <div style="clear:both; color:fff">

                            </div>
                        </form>
                    </div>
                    <div class="cleared"></div>

                </div>

                <div id="footerwrapper"><!--Div for the Footer -->
                    <div class="copyright">
                        <p>Copyright &copy; 2015 Shelter Cluster Nepal All rights reserved.</p>
                    </div><!--ended Footer Div-->
                </div>   
            </div>
        </div>

    </body>
</html>
