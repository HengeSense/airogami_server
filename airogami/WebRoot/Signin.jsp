<%@ page language="java" import="java.util.*, com.airogami.presentation.logic.User,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
    <title>View Account</title>
	
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    
    <!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
    <style type="text/css">
    .container{
    margin:auto;
    width:300px;
    }
    </style>
  </head>
  <body>
    <div class = "container">    
     <p><% 
     User user = new User(1, null);
     user.setAccountId(1);
     request.getSession(true).setAttribute("user", user);
     out.print("setting user successfully!" );
     %> </p>
   
    </div>
   
  </body>
</html>
