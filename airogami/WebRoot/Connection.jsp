<%@ page language="java" import="java.util.*, com.airogami.presentation.logic.User" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Collection</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style type="text/css">
    .container{
    margin:auto;
    width:400px;
    }
    </style>
  </head>
  
  <body>
  <div class="container">
    <h2>Connection</h2>
    <p><% 
    User user = (User)request.getSession(true).getAttribute("user");
    if(user != null){
        out.print("Welcome " + user.getAccountId());
    }
    else{
        out.print("Please signin");
    }
     %> </p>
     <% if(user != null) {%>
     <button onclick="connection()">Send</button>
    <div id='info'></div>
    <%} %>
    <script type = "text/javascript" src="jses/connection.js">
    </script>
  </div>
  </body>
</html>
