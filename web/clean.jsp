<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="Util.DBConnection"%>
<%@page import="java.sql.*"%>
<%@page import="Util.*"%>
<%@page import="java.io.*"%>
<%
    if (null == session.getAttribute("userName")) {
        response.sendRedirect("index.jsp");
    }
%>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>IM Helper - Clean and Consolidation</title>
            <link rel="shortcut icon" href="images/favicon.ico" />
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
                                    <form id="msform" method="post">

                                        <fieldset>
                                            <legend><h3>Clean and Consolidation</h3></legend> </br>
 <div class="tabs">
    
   <div class="tab">
       <input type="radio" id="clean" name="tab-group-1" checked>
       <label for="clean">Clean</label>
       <div class="content">
           <input type="submit" name="clean" value="Clean"/><br/><br/>
           <%
                        String button = request.getParameter("clean");
                        if (button != null) {
                            PropertiesUtil.loadPropertiesFile();
                            String cleanPara = PropertiesUtil.getCleanParam();
                            String cleanPath = PropertiesUtil.getCleanPath();
                            String pythonScriptPath = cleanPara + " \"" + cleanPath + "\"";
                            System.out.println(pythonScriptPath);
                            Runtime clean_rt = Runtime.getRuntime();
                            Process clean_pr = clean_rt.exec(pythonScriptPath);
                            BufferedReader clean_bfr = new BufferedReader(new InputStreamReader(clean_pr.getInputStream()));
                            String line = "";
                    %>
                    <textarea name="outputLog" rows="6" cols="75">
<%=pythonScriptPath%>
                        <% while ((line = clean_bfr.readLine()) != null) {
                                System.out.println(line);
                        %>
<%=line%>
                        <%--<%=line%>--%>
                        <% Thread.sleep(1000);
                } %>
                    </textarea>
                    <%
                        } else { %>
                        <textarea name="outputLog" rows="6" cols="75"></textarea>        
                     <%   }
                    %>
       </div> 
   </div>
    
   <div class="tab">
       <input type="radio" id="consolidation" name="tab-group-1">
       <label for="consolidation">Consolidation</label>
       
       <div class="content">
           <input type="text" name="filename" placeholder="DatabaseV5.0_06_12_2015.xlsx" />
          <input type="submit" name="cons" value="Cons"/><br/><br/>
          <%
                        String conButton = request.getParameter("cons");
                        String fname = request.getParameter("filename");
                        if (conButton != null) {
                            if (conButton != null) {
                            if (fname != "") {
                                PropertiesUtil.loadPropertiesFile();
                                String consPara = PropertiesUtil.getConsParam();
                                String cleanPath = PropertiesUtil.getCleanPath();
                                String consDBPath = PropertiesUtil.getConsDBPath();
                                String pythonScriptPath = consPara + " \"" + cleanPath + "\" --db" + " \"" + consDBPath + fname + "\"";
                                System.out.println(pythonScriptPath);
                                Runtime con_rt = Runtime.getRuntime();
                                Process con_pr = con_rt.exec(pythonScriptPath);
                                BufferedReader con_bfr = new BufferedReader(new InputStreamReader(con_pr.getInputStream()));
                                String line = "";
                    %>
                    <textarea name="outputLog" rows="6" cols="75">
<%=pythonScriptPath%>
                        <% while ((line = con_bfr.readLine()) != null) {
                                System.out.println(line);
                        %>
<%=line%>
                        <%--<%=line%>--%>
                        <% Thread.sleep(1000);
                } %>
                    </textarea>
                    <%
                            }
                        }
} else { %>
<textarea name="outputLog" rows="6" cols="75"></textarea> 
<%}
                    %>
       </div> 
       
   </div>
   <div class="tab">
       <input type="radio" id="pg" name="tab-group-1">
       <label for="pg">Insert Into Postgresql Database</label>
       
       <div class="content">
           <!--<input type="text" name="filename"/>-->
           <input type="submit" name="insert" value="Insert"/><br/><br/>
          <%
                        String insertButton = request.getParameter("insert");
                        //String filename = request.getParameter("filename");
                        String insertDis = "";
                        String insertTrain= "";
                        String disCount = "";
                        String TrainCount = "";
                        if (insertButton != null) {
                                PropertiesUtil.loadPropertiesFile();
                                insertDis = PropertiesUtil.getInsertDistribution();
                                insertTrain = PropertiesUtil.getInsertTraining();
                                System.out.println(insertDis);
                                Runtime pg_rt = Runtime.getRuntime();
                                Process pg_pr = pg_rt.exec(insertDis);
                                BufferedReader pg_bfr = new BufferedReader(new InputStreamReader(pg_pr.getInputStream()));
                                String line = "";
                                while ((line = pg_bfr.readLine()) != null) {
                                System.out.println(line);
                                Thread.sleep(1000);
                                }
                                Connection con = DBConnection.getConnection();
                                if (con != null) {
                                    Statement s = con.createStatement();
                                    ResultSet r = s.executeQuery("SELECT COUNT(*) AS rowcount FROM " + PropertiesUtil.getDbTable());
                                    r.next();
                                    int count = r.getInt("rowcount");
                                    r.close();
                                    disCount = "Total Data for Distribution : " + String.valueOf(count);
                                }
                                 System.out.println(insertTrain);
                                 Runtime rt1 = Runtime.getRuntime();
                                 Process pr1 = rt1.exec(insertTrain);
                                 BufferedReader bfr1 = new BufferedReader(new InputStreamReader(pr1.getInputStream()));
                                 String line1 = "";
                                 while ((line1 = bfr1.readLine()) != null) {
                                 System.out.println(line1);
                                 Thread.sleep(1000);
                                }
                                if (con != null) {
                                    Statement s = con.createStatement();
                                    ResultSet r = s.executeQuery("SELECT COUNT(*) AS rowcount FROM " + PropertiesUtil.getDbTable1());
                                    r.next();
                                    int count = r.getInt("rowcount");
                                    r.close();
                                    TrainCount = "Total Data for Training     : " + String.valueOf(count);
                                    con.close();
                                }
                        }
                    %>
                    <textarea name="outputLog" rows="6" cols="75">
<%=insertDis%>
<%=disCount%>
<%=insertTrain%>
<%=TrainCount%>

                    </textarea>
       </div> 
       
   </div>

</div>
 
                                                                                                        
                                       
                                        
                                        
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
