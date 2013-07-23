<%@ page language="java" import="java.util.*, com.airogami.persistence.entities.Account" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Airogami Home Page</title>
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
    <h2>Airogami Home Page</h2>
    <a href="Signup.html">Signup</a> <a href="SigninAndOut.html">SigninAndOut</a>
    <p><% 
    Account account = (Account)request.getSession(true).getAttribute("account");
    if(account != null){
        out.print("Welcome " + account.getFullName());
    }
    else{
        out.print("Please signin");
    }
     %> </p>
     <% if(account != null) {%>
     <h3>Account:</h3>
     <a href="EditAccount.jsp"><button>Edit Account</button></a>
     <a href="EditAuthenticate.jsp"><button>Edit Authenticate</button></a>
     <a href="ViewAccount.jsp"><button>View Account</button></a>
     <h3>Plane:</h3>
     <a href="SendPlane.jsp"><button>Send Plane</button></a>
     <a href="ObtainPlanes.jsp"><button>Obtain Planes</button></a>
     <a href="LikeDeletePlane.jsp"><button>Like Delete Plane</button></a>
     <a href="PickupBothThrowPlane.jsp"><button>Pickup Both and Throw Plane</button></a>
     <%} %>
     </div>
  </body>
</html>
